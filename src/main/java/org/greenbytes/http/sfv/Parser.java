package org.greenbytes.http.sfv;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the "Structured Field Values" Parser.
 *
 * @see <a href=
 *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#text-parse">Section
 *      4.2 of draft-ietf-httpbis-header-structure-18</a>
 */
public class Parser {

    private final CharBuffer input;
    // private final List<Integer> startPositions;

    /**
     * Creates {@link Parser} for the given input.
     * 
     * @param input
     *            single field line
     * @throws IllegalArgumentException
     *             for non-ASCII characters
     */
    public Parser(String input) {
        this(Collections.singletonList(Objects.requireNonNull(input, "input must not be null")));
    }

    /**
     * Creates {@link Parser} for the given input.
     * 
     * @param fieldLines
     *            field lines
     * @throws IllegalArgumentException
     *             for non-ASCII characters or empty input
     */
    public Parser(Iterable<String> fieldLines) {
        StringBuilder sb = null;
        String str = null;
        for (String s : Objects.requireNonNull(fieldLines, "fieldLines must not be null")) {
            if (str == null) {
                str = checkASCII(s);
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(str);
                }
                sb.append(",").append(checkASCII(s));
            }
        }
        if (str == null && sb == null) {
            throw new IllegalArgumentException("empty input");
        }
        this.input = CharBuffer.wrap(sb != null ? sb : str);
        // this.startPositions = Collections.singletonList(0);
    }

    private static String checkASCII(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 0x00 || c > 0x7f) {
                throw new IllegalArgumentException(String
                        .format("Invalid character in field line at position %d: '%c' (0x%04x) (input: %s)", i, c, (int) c, value));
            }
        }
        return value;
    }

    private NumberItem<? extends Object> internalParseBareIntegerOrDecimal() {
        boolean isDecimal = false;
        int sign = 1;
        StringBuilder inputNumber = new StringBuilder(20);

        if (checkNextChar('-')) {
            sign = -1;
            advance();
        }

        if (!checkNextChar("0123456789")) {
            throw new IllegalArgumentException("illegal start for Integer or Decimal: '" + input + "'");
        }

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = peek();
            if (Utils.isDigit(c)) {
                inputNumber.append(c);
                advance();
            } else if (!isDecimal && c == '.') {
                if (inputNumber.length() > 12) {
                    throw new IllegalArgumentException("illegal position for decimal point in Decimal at " + inputNumber.length());
                }
                inputNumber.append(c);
                isDecimal = true;
                advance();
            } else {
                done = true;
            }
            if (inputNumber.length() > (isDecimal ? 16 : 15)) {
                throw new IllegalArgumentException((isDecimal ? "Decimal" : "Integer") + " too long: " + inputNumber.length());
            }
        }

        if (!isDecimal) {
            long l = Long.parseLong(inputNumber.toString());
            return IntegerItem.valueOf(sign * l);
        } else {
            int dotPos = inputNumber.indexOf(".");
            int fracLen = inputNumber.length() - dotPos - 1;

            if (fracLen < 1) {
                throw new IllegalArgumentException("decimal number must not end in '.'");
            } else if (fracLen == 1) {
                inputNumber.append("00");
            } else if (fracLen == 2) {
                inputNumber.append("0");
            } else if (fracLen > 3) {
                throw new IllegalArgumentException(
                        "maximum number of fractional digits is 3, found: " + fracLen + ", in: " + inputNumber);
            }

            inputNumber.deleteCharAt(dotPos);
            long l = Long.parseLong(inputNumber.toString());
            return DecimalItem.valueOf(sign * l);
        }
    }

    private NumberItem<? extends Object> internalParseIntegerOrDecimal() {
        NumberItem<? extends Object> result = internalParseBareIntegerOrDecimal();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private StringItem internalParseBareString() {

        if (getOrEOF() != '"') {
            throw new IllegalArgumentException("must start with double quote: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(input.length());

        while (hasRemaining()) {
            char c = get();
            if (c == '\\') {
                c = getOrEOF();
                if (c != '"' && c != '\\') {
                    throw new IllegalArgumentException("invalid escape sequence at " + input.position());
                }
                outputString.append(c);
            } else {
                if (c == '"') {
                    return StringItem.valueOf(outputString.toString());
                } else if (c < 0x20 || c >= 0x7f) {
                    throw new IllegalArgumentException("invalid character at " + input.length());
                } else {
                    outputString.append(c);
                }
            }
        }

        throw new IllegalArgumentException("closing double quote missing");
    }

    private StringItem internalParseString() {
        StringItem result = internalParseBareString();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private TokenItem internalParseBareToken() {

        char c = getOrEOF();
        if (c != '*' && !Utils.isAlpha(c)) {
            throw new IllegalArgumentException("must start with ALPHA or *: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(input.length());
        outputString.append(c);

        boolean done = false;
        while (hasRemaining() && !done) {
            c = peek();
            if (c <= ' ' || c >= 0x7f || "\"(),;<=>?@[\\]{}".indexOf(c) >= 0) {
                done = true;
            } else {
                advance();
                outputString.append(c);
            }
        }

        return TokenItem.valueOf(outputString.toString());
    }

    private TokenItem internalParseToken() {
        TokenItem result = internalParseBareToken();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private static Base64.Decoder BASE64DECODER = Base64.getDecoder();

    private ByteSequenceItem internalParseBareByteSequence() {
        if (getOrEOF() != ':') {
            throw new IllegalArgumentException("must start with colon: " + input);
        }

        StringBuilder outputString = new StringBuilder(input.length());

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = get();
            if (c == ':') {
                done = true;
            } else {
                // TODO: check validity here?
                outputString.append(c);
            }
        }

        if (!done) {
            throw new IllegalArgumentException("must end with colon: :" + outputString);
        }

        // should throw on invalid input
        return ByteSequenceItem.valueOf(BASE64DECODER.decode(outputString.toString()));
    }

    private ByteSequenceItem internalParseByteSequence() {
        ByteSequenceItem result = internalParseBareByteSequence();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private BooleanItem internalParseBareBoolean() {

        if (getOrEOF() != '?') {
            throw new IllegalArgumentException("must start with question mark: " + input);
        }

        char c = getOrEOF();

        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("expected 0 or 1 in boolean: " + input);
        }

        return BooleanItem.valueOf(c == '1');
    }

    private BooleanItem internalParseBoolean() {
        BooleanItem result = internalParseBareBoolean();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private String internalParseKey() {

        char c = getOrEOF();
        if (c != '*' && !Utils.isLcAlpha(c)) {
            throw new IllegalArgumentException("must start with LCALPHA or *: " + input);
        }

        StringBuilder result = new StringBuilder();
        result.append(c);

        boolean done = false;
        while (hasRemaining() && !done) {
            c = peek();
            if (Utils.isLcAlpha(c) || Utils.isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*') {
                result.append(c);
                advance();
            } else {
                done = true;
            }
        }

        return result.toString();
    }

    private Parameters internalParseParameters() {

        LinkedHashMap<String, Item<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = peek();
            if (c != ';') {
                done = true;
            } else {
                advance();
                removeLeadingSP();
                String name = internalParseKey();
                Item<? extends Object> value = BooleanItem.valueOf(true);
                if (peek() == '=') {
                    advance();
                    value = internalParseBareItem();
                }
                result.put(name, value);
            }
        }

        return Parameters.valueOf(result);
    }

    private Item<? extends Object> internalParseBareItem() {
        if (!hasRemaining()) {
            throw new IllegalArgumentException("empty string");
        }

        char c = peek();
        if (Utils.isDigit(c) || c == '-') {
            return internalParseBareIntegerOrDecimal();
        } else if (c == '"') {
            return internalParseBareString();
        } else if (c == '?') {
            return internalParseBareBoolean();
        } else if (c == '*' || Utils.isAlpha(c)) {
            return internalParseBareToken();
        } else if (c == ':') {
            return internalParseBareByteSequence();
        } else {
            throw new IllegalArgumentException("unknown type: " + input);
        }
    }

    private Item<? extends Object> internalParseItem() {
        Item<? extends Object> result = internalParseBareItem();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private Item<? extends Object> internalParseItemOrInnerList() {
        return peek() == '(' ? internalParseInnerList() : internalParseItem();
    }

    private List<Item<? extends Object>> internalParseOuterList() {
        List<Item<? extends Object>> result = new ArrayList<>();

        while (hasRemaining()) {
            result.add(internalParseItemOrInnerList());
            removeLeadingSP();
            if (!hasRemaining()) {
                return result;
            }
            if (get() != ',') {
                throw new IllegalArgumentException("expected COMMA, got: " + input);
            }
            removeLeadingSP();
            if (!hasRemaining()) {
                throw new IllegalArgumentException("found trailing COMMA in list");
            }
        }

        // Won't get here
        return result;
    }

    private List<Item<? extends Object>> internalParseBareInnerList() {

        char c = getOrEOF();
        if (c != '(') {
            throw new IllegalArgumentException("inner list must start with '(': " + input);
        }

        List<Item<? extends Object>> result = new ArrayList<>();

        boolean done = false;
        while (hasRemaining() && !done) {
            removeLeadingSP();

            c = input.charAt(0);
            if (c == ')') {
                advance();
                done = true;
            } else {
                Item<? extends Object> item = internalParseItem();
                result.add(item);

                c = peek();
                if (c != ' ' && c != ')') {
                    throw new IllegalArgumentException("expected SP or ')': " + input);
                }
            }

        }

        if (!done) {
            throw new IllegalArgumentException("inner list must end with ')': " + input);
        }

        return result;
    }

    private InnerList internalParseInnerList() {
        List<Item<? extends Object>> result = internalParseBareInnerList();
        Parameters params = internalParseParameters();
        return InnerList.valueOf(result).withParams(params);
    }

    private Dictionary internalParseDictionary() {

        LinkedHashMap<String, Item<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (hasRemaining() && !done) {

            Item<? extends Object> member;

            String name = internalParseKey();

            if (peek() == '=') {
                advance();
                member = internalParseItemOrInnerList();
            } else {
                member = BooleanItem.valueOf(true).withParams(internalParseParameters());
            }

            result.put(name, member);

            removeLeadingSP();
            if (hasRemaining()) {
                char c = get();
                if (c != ',') {
                    throw new IllegalArgumentException("Expected COMMA in dictionary, found: '" + c + "'");
                }
                removeLeadingSP();
                if (!hasRemaining()) {
                    throw new IllegalArgumentException("Trailing COMMA in dictionary");
                }
            } else {
                done = true;
            }
        }

        return Dictionary.valueOf(result);
    }

    // protected methods unit testing

    protected static IntegerItem parseInteger(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseIntegerOrDecimal();
        if (!(result instanceof IntegerItem)) {
            throw new IllegalArgumentException("string parsed as Integer '" + input + "' is a Decimal");
        } else {
            p.assertEmpty("extra characters in string parsed as Integer");
            return (IntegerItem) result;
        }
    }

    protected static DecimalItem parseDecimal(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseIntegerOrDecimal();
        if (!(result instanceof DecimalItem)) {
            throw new IllegalArgumentException("string parsed as Decimal '" + input + "' is an Integer");
        } else {
            p.assertEmpty("extra characters in string parsed as Decimal");
            return (DecimalItem) result;
        }
    }

    // public instance methods

    /**
     * Implementation of "Parsing a List"
     *
     * @return result of parse as {@link OuterList}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-list">Section
     *      4.2.1 of draft-ietf-httpbis-header-structure-18</a>
     */
    public OuterList parseList() {
        removeLeadingSP();
        List<Item<? extends Object>> result = internalParseOuterList();
        removeLeadingSP();
        assertEmpty("extra characters in string parsed as List");
        return OuterList.valueOf(result);
    }

    /**
     * Implementation of "Parsing a Dictionary"
     *
     * @return result of parse as {@link Dictionary}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-dictionary">Section
     *      4.2.2 of draft-ietf-httpbis-header-structure-18</a>
     */
    public Dictionary parseDictionary() {
        removeLeadingSP();
        Dictionary result = internalParseDictionary();
        removeLeadingSP();
        assertEmpty("extra characters in string parsed as Dictionary");
        return result;
    }

    /**
     * Implementation of "Parsing an Item"
     *
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-bare-item">Section
     *      4.2.3 of draft-ietf-httpbis-header-structure-18</a>
     */
    public Item<? extends Object> parseItem() {
        removeLeadingSP();
        Item<? extends Object> result = internalParseItem();
        removeLeadingSP();
        assertEmpty("extra characters in string parsed as Item");
        return result;
    }

    // static public methods

    /**
     * Implementation of "Parsing a List" (assuming no extra characters left in
     * input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link OuterList}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-list">Section
     *      4.2.1 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static OuterList parseList(String input) {
        Parser p = new Parser(input);
        List<Item<? extends Object>> result = p.internalParseOuterList();
        p.assertEmpty("extra characters in string parsed as List");
        return OuterList.valueOf(result);
    }

    /**
     * Implementation of "Parsing an Item Or Inner List" (assuming no extra
     * characters left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-item-or-list">Section
     *      4.2.1.1 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static Item<? extends Object> parseItemOrInnerList(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseItemOrInnerList();
        p.assertEmpty("extra characters in string parsed as Item or Inner List");
        return result;
    }

    /**
     * Implementation of "Parsing an Inner List" (assuming no extra characters
     * left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link InnerList}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-item-or-list">Section
     *      4.2.1.2 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static InnerList parseInnerList(String input) {
        Parser p = new Parser(input);
        InnerList result = p.internalParseInnerList();
        p.assertEmpty("extra characters in string parsed as Inner List");
        return result;
    }

    /**
     * Implementation of "Parsing a Dictionary" (assuming no extra characters
     * left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link Dictionary}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-dictionary">Section
     *      4.2.2 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static Dictionary parseDictionary(String input) {
        Parser p = new Parser(input);
        Dictionary result = p.internalParseDictionary();
        p.assertEmpty("extra characters in string parsed as Dictionary");
        return result;
    }

    /**
     * Implementation of "Parsing an Item" (assuming no extra characters left in
     * input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-bare-item">Section
     *      4.2.3 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static Item<? extends Object> parseItem(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseItem();
        p.assertEmpty("extra characters in string parsed as Item");
        return result;
    }

    /**
     * Implementation of "Parsing a Bare Item" (assuming no extra characters
     * left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-bare-item">Section
     *      4.2.3.1 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static Item<? extends Object> parseBareItem(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseBareItem();
        p.assertEmpty("extra characters in string parsed as Bare Item");
        return result;
    }

    /**
     * Implementation of "Parsing Parameters" (assuming no extra characters left
     * in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link Parameters}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-param">Section
     *      4.2.3.2 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static Parameters parseParameters(String input) {
        Parser p = new Parser(input);
        Parameters result = p.internalParseParameters();
        p.assertEmpty("extra characters in string parsed as Parameters");
        return result;
    }

    /**
     * Implementation of "Parsing a Key" (assuming no extra characters left in
     * input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link String}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-key">Section
     *      4.2.3.3 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static String parseKey(String input) {
        Parser p = new Parser(input);
        String result = p.internalParseKey();
        p.assertEmpty("extra characters in string parsed as Key");
        return result;
    }

    /**
     * Implementation of "Parsing an Integer or Decimal" (assuming no extra
     * characters left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link NumberItem}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-number">Section
     *      4.2.4 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static NumberItem<? extends Object> parseIntegerOrDecimal(String input) {
        Parser p = new Parser(input);
        NumberItem<? extends Object> result = p.internalParseIntegerOrDecimal();
        p.assertEmpty("extra characters in string parsed as Integer or Decimal");
        return result;
    }

    /**
     * Implementation of "Parsing a String" (assuming no extra characters left
     * in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link StringItem}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-string">Section
     *      4.2.5 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static StringItem parseString(String input) {
        Parser p = new Parser(input);
        StringItem result = p.internalParseString();
        p.assertEmpty("extra characters in string parsed as String");
        return result;
    }

    /**
     * Implementation of "Parsing a Token" (assuming no extra characters left in
     * input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link TokenItem}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-token">Section
     *      4.2.6 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static TokenItem parseToken(String input) {
        Parser p = new Parser(input);
        TokenItem result = p.internalParseToken();
        p.assertEmpty("extra characters in string parsed as Token");
        return result;
    }

    /**
     * Implementation of "Parsing a Byte Sequence" (assuming no extra characters
     * left in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link ByteSequenceItem}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-binary">Section
     *      4.2.7 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static ByteSequenceItem parseByteSequence(String input) {
        Parser p = new Parser(input);
        ByteSequenceItem result = p.internalParseByteSequence();
        p.assertEmpty("extra characters in string parsed as Byte Sequence");
        return result;
    }

    /**
     * Implementation of "Parsing a Boolean" (assuming no extra characters left
     * in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link BooleanItem}.
     *
     * @see <a href=
     *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#parse-boolean">Section
     *      4.2.8 of draft-ietf-httpbis-header-structure-18</a>
     */
    public static BooleanItem parseBoolean(String input) {
        Parser p = new Parser(input);
        BooleanItem result = p.internalParseBoolean();
        p.assertEmpty("extra characters in string parsed as Boolean");
        return result;
    }

    // utility methods on CharBuffer

    private void assertEmpty(String message) {
        if (hasRemaining()) {
            throw new IllegalArgumentException(message + ": '" + input + "'");
        }
    }

    private void advance() {
        input.position(1 + input.position());
    }

    private boolean checkNextChar(char c) {
        return hasRemaining() && input.charAt(0) == c;
    }

    private boolean checkNextChar(String valid) {
        return hasRemaining() && valid.indexOf(input.charAt(0)) >= 0;
    }

    private char get() {
        return input.get();
    }

    private char getOrEOF() {
        return hasRemaining() ? get() : (char) -1;
    }

    private boolean hasRemaining() {
        return input.hasRemaining();
    }

    private char peek() {
        return hasRemaining() ? input.charAt(0) : (char) -1;
    }

    private void removeLeadingSP() {
        while (checkNextChar(' ')) {
            advance();
        }
    }

}
