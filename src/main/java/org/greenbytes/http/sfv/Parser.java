package org.greenbytes.http.sfv;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the "Structured Field Values" Parser.
 *
 * @see <a href=
 *      "https://www.rfc-editor.org/rfc/rfc8941.html#text-parse">Section 4.2 of
 *      RFC 8941</a>
 */
public class Parser {

    private final CharBuffer input;
    private final List<Integer> startPositions;

    /**
     * Creates {@link Parser} for the given input.
     * 
     * @param input
     *            single field line
     * @throws ParseException
     *             for non-ASCII characters
     */
    public Parser(String input) {
        this(Collections.singletonList(Objects.requireNonNull(input, "input must not be null")));
    }

    /**
     * Creates {@link Parser} for the given input.
     * 
     * @param input
     *            field lines
     * @throws ParseException
     *             for non-ASCII characters
     */
    public Parser(String... input) {
        this(Arrays.asList(input));
    }

    /**
     * Creates {@link Parser} for the given input.
     * 
     * @param fieldLines
     *            field lines
     * @throws ParseException
     *             for non-ASCII characters or empty input
     */
    public Parser(Iterable<String> fieldLines) {

        StringBuilder sb = null;
        String str = null;
        List<Integer> startPositions = Collections.emptyList();

        for (String s : Objects.requireNonNull(fieldLines, "fieldLines must not be null")) {
            Objects.requireNonNull(s, "field line must not be null");
            if (str == null) {
                str = checkASCII(s);
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(str);
                }
                if (startPositions.size() == 0) {
                    startPositions = new ArrayList<>();
                }
                startPositions.add(sb.length());
                sb.append(",").append(checkASCII(s));
            }
        }
        if (str == null && sb == null) {
            throw new ParseException("Empty input", "", 0);
        }
        this.input = CharBuffer.wrap(sb != null ? sb : str);
        this.startPositions = startPositions;
    }

    private static String checkASCII(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 0x00 || c > 0x7f) {
                throw new ParseException(String.format("Invalid character in field line at position %d: '%c' (0x%04x) (input: %s)",
                        i, c, (int) c, value), value, i);
            }
        }
        return value;
    }

    private DateItem internalParseBareDate() {
        int sign = 1;
        StringBuilder inputNumber = new StringBuilder(21);

        if (!checkNextChar("@")) {
            throw complaint("Illegal start for Date: '" + input + "'");
        }
        advance();

        if (checkNextChar('-')) {
            sign = -1;
            advance();
        }

        if (!checkNextChar("0123456789")) {
            throw complaint("Illegal start inside a Date: '" + input + "'");
        }

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = peek();
            if (Utils.isDigit(c)) {
                inputNumber.append(c);
                advance();
            } else {
                done = true;
            }
            if (inputNumber.length() > 15) {
                backout();
                throw complaint("Date too long: " + inputNumber.length() + " characters");
            }
        }

        long l = Long.parseLong(inputNumber.toString());
        return DateItem.valueOf(sign * l);
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
            throw complaint("Illegal start for Integer or Decimal: '" + input + "'");
        }

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = peek();
            if (Utils.isDigit(c)) {
                inputNumber.append(c);
                advance();
            } else if (!isDecimal && c == '.') {
                if (inputNumber.length() > 12) {
                    throw complaint("Illegal position for decimal point in Decimal after '" + inputNumber + "'");
                }
                inputNumber.append(c);
                isDecimal = true;
                advance();
            } else {
                done = true;
            }
            if (inputNumber.length() > (isDecimal ? 16 : 15)) {
                backout();
                throw complaint((isDecimal ? "Decimal" : "Integer") + " too long: " + inputNumber.length() + " characters");
            }
        }

        if (!isDecimal) {
            long l = Long.parseLong(inputNumber.toString());
            return IntegerItem.valueOf(sign * l);
        } else {
            int dotPos = inputNumber.indexOf(".");
            int fracLen = inputNumber.length() - dotPos - 1;

            if (fracLen < 1) {
                backout();
                throw complaint("Decimal must not end in '.'");
            } else if (fracLen == 1) {
                inputNumber.append("00");
            } else if (fracLen == 2) {
                inputNumber.append("0");
            } else if (fracLen > 3) {
                backout();
                throw complaint("Maximum number of fractional digits is 3, found: " + fracLen + ", in: " + inputNumber);
            }

            inputNumber.deleteCharAt(dotPos);
            long l = Long.parseLong(inputNumber.toString());
            return DecimalItem.valueOf(sign * l);
        }
    }

    private DateItem internalParseDate() {
        DateItem result = internalParseBareDate();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private NumberItem<? extends Object> internalParseIntegerOrDecimal() {
        NumberItem<? extends Object> result = internalParseBareIntegerOrDecimal();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private StringItem internalParseBareString() {

        if (getOrEOD() != '"') {
            throw complaint("String must start with double quote: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(length());

        while (hasRemaining()) {
            if (startPositions.contains(position())) {
                throw complaint("String crosses field line boundary at position " + position());
            }

            char c = get();
            if (c == '\\') {
                c = getOrEOD();
                if (c == EOD) {
                    throw complaint("Incomplete escape sequence at position " + position());
                } else if (c != '"' && c != '\\') {
                    backout();
                    throw complaint("Invalid escape sequence character '" + c + "' at position " + position());
                }
                outputString.append(c);
            } else {
                if (c == '"') {
                    return StringItem.valueOf(outputString.toString());
                } else if (c < 0x20 || c >= 0x7f) {
                    throw complaint("Invalid character in String at position " + position());
                } else {
                    outputString.append(c);
                }
            }
        }

        throw complaint("Closing DQUOTE missing");
    }

    private DisplayStringItem internalParseBareDisplayString() {

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT);

        if (getOrEOD() != '%') {
            throw complaint("DisplayString must start with a percent sign: '" + input + "'");
        }

        if (getOrEOD() != '"') {
            throw complaint("DisplayString must continue with a double quote: '" + input + "'");
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream(length() * 2);
        int startpos = position();

        while (hasRemaining()) {
            if (startPositions.contains(position())) {
                throw complaint("Display String crosses field line boundary at position " + position());
            }

            char c = get();
            if (c == '%') {
                char c1 = getOrEOD();
                if (c1 == EOD) {
                    throw complaint("Incomplete percent escape sequence at position " + position());
                } else if (!isHex(c1)) {
                    backout();
                    throw complaint("Invalid percent escape sequence character '" + c + "' at position " + position());
                }
                char c2 = getOrEOD();
                if (c2 == EOD) {
                    throw complaint("Incomplete percent escape sequence at position " + position());
                } else if (!isHex(c2)) {
                    backout();
                    throw complaint("Invalid percent escape sequence character '" + c + "' at position " + position());
                }
                output.write(decodeHex(c1, c2));
            } else {
                if (c == '"') {
                    ByteBuffer bytes = ByteBuffer.wrap(output.toByteArray());
                    int blen = bytes.remaining();
                    try {
                        return DisplayStringItem.valueOf(decoder.decode(bytes).toString());
                    } catch (CharacterCodingException ex) {
                        int length = position() - startpos - 1;
                        char chars[] = new char[length];
                        input.position(startpos);
                        input.get(chars, 0, length);
//                        System.err.println("s: " + new String(chars));

                        // map byte positions to input positions
                        int offsets[] = new int[blen];
                        for (int i = 0, j = 0; i < blen; i++) {
                            offsets[i] = j;
//                            System.err.println(chars[j] + " " + i + " " + j);
                            if (chars[j] == '%') {
                                j+=3;
                            }else {
                                j += 1;
                            }
                        }
//                        System.err.println("r: " + bytes.remaining());
//                        System.err.println("l: " + blen);
//                        System.err.println("i: " + offsets[blen - bytes.remaining()]);
                        int failpos = startpos + offsets[blen - bytes.remaining()];
                        throw complaint("Invalid UTF-8 sequence (" + ex.getMessage() + ") before position " + failpos, failpos, ex);
                    }
                } else if (c < 0x20 || c >= 0x7f) {
                    throw complaint("Invalid character in Display String at position " + position());
                } else {
                    output.write((int) c);
                }
            }
        }

        throw complaint("Closing DQUOTE missing");
    }

    private StringItem internalParseString() {
        StringItem result = internalParseBareString();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private DisplayStringItem internalParseDisplayString() {
        DisplayStringItem result = internalParseBareDisplayString();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private TokenItem internalParseBareToken() {

        char c = getOrEOD();
        if (c != '*' && !Utils.isAlpha(c)) {
            throw complaint("Token must start with ALPHA or *: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(length());
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

    private static boolean isBase64Char(char c) {
        return Utils.isAlpha(c) || Utils.isDigit(c) || c == '+' || c == '/' || c == '=';
    }

    private ByteSequenceItem internalParseBareByteSequence() {
        if (getOrEOD() != ':') {
            throw complaint("Byte Sequence must start with colon: " + input);
        }

        StringBuilder outputString = new StringBuilder(length());

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = get();
            if (c == ':') {
                done = true;
            } else {
                if (!isBase64Char(c)) {
                    throw complaint("Invalid Byte Sequence Character '" + c + "' at position " + position());
                }
                outputString.append(c);
            }
        }

        if (!done) {
            throw complaint("Byte Sequence must end with COLON: '" + outputString + "'");
        }

        try {
            return ByteSequenceItem.valueOf(BASE64DECODER.decode(outputString.toString()));
        } catch (IllegalArgumentException ex) {
            throw complaint(ex.getMessage(), ex);
        }
    }

    private ByteSequenceItem internalParseByteSequence() {
        ByteSequenceItem result = internalParseBareByteSequence();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private BooleanItem internalParseBareBoolean() {

        char c = getOrEOD();

        if (c == EOD) {
            throw complaint("Missing data in Boolean");
        } else if (c != '?') {
            backout();
            throw complaint(String.format("Boolean must start with question mark, got '%c'", c));
        }

        c = getOrEOD();

        if (c == EOD) {
            throw complaint("Missing data in Boolean");
        } else if (c != '0' && c != '1') {
            backout();
            throw complaint(String.format("Expected '0' or '1' in Boolean, found '%c'", c));
        }

        return BooleanItem.valueOf(c == '1');
    }

    private BooleanItem internalParseBoolean() {
        BooleanItem result = internalParseBareBoolean();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private String internalParseKey() {

        char c = getOrEOD();
        if (c == EOD) {
            throw complaint("Missing data in Key");
        } else if (c != '*' && !Utils.isLcAlpha(c)) {
            backout();
            throw complaint("Key must start with LCALPHA or '*': " + format(c));
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

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

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
            throw complaint("Empty string found when parsing Bare Item");
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
        } else if (c == '@') {
            return internalParseBareDate();
        } else if (c == '%') {
            return internalParseBareDisplayString();
        } else {
            throw complaint("Unexpected start character in Bare Item: " + format(c));
        }
    }

    private Item<? extends Object> internalParseItem() {
        Item<? extends Object> result = internalParseBareItem();
        Parameters params = internalParseParameters();
        return result.withParams(params);
    }

    private ListElement<? extends Object> internalParseItemOrInnerList() {
        return peek() == '(' ? internalParseInnerList() : internalParseItem();
    }

    private List<ListElement<? extends Object>> internalParseOuterList() {
        List<ListElement<? extends Object>> result = new ArrayList<>();

        while (hasRemaining()) {
            result.add(internalParseItemOrInnerList());
            removeLeadingOWS();
            if (!hasRemaining()) {
                return result;
            }
            char c = get();
            if (c != ',') {
                backout();
                throw complaint("Expected COMMA in List, got: " + format(c));
            }
            removeLeadingOWS();
            if (!hasRemaining()) {
                throw complaint("Found trailing COMMA in List");
            }
        }

        // Won't get here
        return result;
    }

    private List<Item<? extends Object>> internalParseBareInnerList() {

        char c = getOrEOD();
        if (c != '(') {
            throw complaint("Inner List must start with '(': " + input);
        }

        List<Item<? extends Object>> result = new ArrayList<>();

        boolean done = false;
        while (hasRemaining() && !done) {
            removeLeadingSP();

            c = peek();
            if (c == ')') {
                advance();
                done = true;
            } else {
                Item<? extends Object> item = internalParseItem();
                result.add(item);

                c = peek();
                if (c == EOD) {
                    throw complaint("Missing data in Inner List");
                } else if (c != ' ' && c != ')') {
                    throw complaint("Expected SP or ')' in Inner List, got: " + format(c));
                }
            }

        }

        if (!done) {
            throw complaint("Inner List must end with ')': " + input);
        }

        return result;
    }

    private InnerList internalParseInnerList() {
        List<Item<? extends Object>> result = internalParseBareInnerList();
        Parameters params = internalParseParameters();
        return InnerList.valueOf(result).withParams(params);
    }

    private Dictionary internalParseDictionary() {

        LinkedHashMap<String, ListElement<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (hasRemaining() && !done) {

            ListElement<? extends Object> member;

            String name = internalParseKey();

            if (peek() == '=') {
                advance();
                member = internalParseItemOrInnerList();
            } else {
                member = BooleanItem.valueOf(true).withParams(internalParseParameters());
            }

            result.put(name, member);

            removeLeadingOWS();
            if (hasRemaining()) {
                char c = get();
                if (c != ',') {
                    backout();
                    throw complaint("Expected COMMA in Dictionary, found: " + format(c));
                }
                removeLeadingOWS();
                if (!hasRemaining()) {
                    throw complaint("Found trailing COMMA in Dictionary");
                }
            } else {
                done = true;
            }
        }

        return Dictionary.valueOf(result);
    }

    // protected methods unit testing

    protected static DateItem parseDate(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseDate();
        if (!(result instanceof DateItem)) {
            throw p.complaint("String parsed as Date '" + input + "' is not a Date");
        } else {
            p.assertEmpty("Extra characters in string parsed as Date");
            return (DateItem) result;
        }
    }

    protected static IntegerItem parseInteger(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseIntegerOrDecimal();
        if (!(result instanceof IntegerItem)) {
            throw p.complaint("String parsed as Integer '" + input + "' is a Decimal");
        } else {
            p.assertEmpty("Extra characters in string parsed as Integer");
            return (IntegerItem) result;
        }
    }

    protected static DecimalItem parseDecimal(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseIntegerOrDecimal();
        if (!(result instanceof DecimalItem)) {
            throw p.complaint("String parsed as Decimal '" + input + "' is an Integer");
        } else {
            p.assertEmpty("Extra characters in string parsed as Decimal");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-list">Section
     *      4.2.1 of RFC 8941</a>
     */
    public OuterList parseList() {
        removeLeadingSP();
        List<ListElement<? extends Object>> result = internalParseOuterList();
        removeLeadingSP();
        assertEmpty("Extra characters in string parsed as List");
        return OuterList.valueOf(result);
    }

    /**
     * Implementation of "Parsing a Dictionary"
     *
     * @return result of parse as {@link Dictionary}.
     *
     * @see <a href=
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-dictionary">Section
     *      4.2.2 of RFC 8941</a>
     */
    public Dictionary parseDictionary() {
        removeLeadingSP();
        Dictionary result = internalParseDictionary();
        removeLeadingSP();
        assertEmpty("Extra characters in string parsed as Dictionary");
        return result;
    }

    /**
     * Implementation of "Parsing an Item"
     *
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-item">Section
     *      4.2.3 of RFC 8941</a>
     */
    public Item<? extends Object> parseItem() {
        removeLeadingSP();
        Item<? extends Object> result = internalParseItem();
        removeLeadingSP();
        assertEmpty("Extra characters in string parsed as Item");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-list">Section
     *      4.2.1 of RFC 8941</a>
     */
    public static OuterList parseList(String input) {
        Parser p = new Parser(input);
        List<ListElement<? extends Object>> result = p.internalParseOuterList();
        p.assertEmpty("Extra characters in string parsed as List");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-item-or-list">Section
     *      4.2.1.1 of RFC 8941</a>
     */
    public static Parametrizable<? extends Object> parseItemOrInnerList(String input) {
        Parser p = new Parser(input);
        ListElement<? extends Object> result = p.internalParseItemOrInnerList();
        p.assertEmpty("Extra characters in string parsed as Item or Inner List");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-item-or-list">Section
     *      4.2.1.2 of RFC 8941</a>
     */
    public static InnerList parseInnerList(String input) {
        Parser p = new Parser(input);
        InnerList result = p.internalParseInnerList();
        p.assertEmpty("Extra characters in string parsed as Inner List");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-dictionary">Section
     *      4.2.2 of RFC 8941</a>
     */
    public static Dictionary parseDictionary(String input) {
        Parser p = new Parser(input);
        Dictionary result = p.internalParseDictionary();
        p.assertEmpty("Extra characters in string parsed as Dictionary");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-item">Section
     *      4.2.3 of RFC 8941</a>
     */
    public static Item<? extends Object> parseItem(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseItem();
        p.assertEmpty("Extra characters in string parsed as Item");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-bare-item">Section
     *      4.2.3.1 of RFC 8941</a>
     */
    public static Item<? extends Object> parseBareItem(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.internalParseBareItem();
        p.assertEmpty("Extra characters in string parsed as Bare Item");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-param">Section
     *      4.2.3.2 of RFC 8941</a>
     */
    public static Parameters parseParameters(String input) {
        Parser p = new Parser(input);
        Parameters result = p.internalParseParameters();
        p.assertEmpty("Extra characters in string parsed as Parameters");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-key">Section
     *      4.2.3.3 of RFC 8941</a>
     */
    public static String parseKey(String input) {
        Parser p = new Parser(input);
        String result = p.internalParseKey();
        p.assertEmpty("Extra characters in string parsed as Key");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-number">Section
     *      4.2.4 of RFC 8941</a>
     */
    public static NumberItem<? extends Object> parseIntegerOrDecimal(String input) {
        Parser p = new Parser(input);
        NumberItem<? extends Object> result = p.internalParseIntegerOrDecimal();
        p.assertEmpty("Extra characters in string parsed as Integer or Decimal");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-string">Section
     *      4.2.5 of RFC 8941</a>
     */
    public static StringItem parseString(String input) {
        Parser p = new Parser(input);
        StringItem result = p.internalParseString();
        p.assertEmpty("Extra characters in string parsed as String");
        return result;
    }

    /**
     * Implementation of "Parsing a Display String" (assuming no extra characters left
     * in input string)
     *
     * @param input
     *            {@link String} to parse.
     * @return result of parse as {@link DisplayStringItem}.
     */
    public static DisplayStringItem parseDisplayString(String input) {
        Parser p = new Parser(input);
        DisplayStringItem result = p.internalParseDisplayString();
        p.assertEmpty("Extra characters in string parsed as String");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-token">Section
     *      4.2.6 of RFC 8941</a>
     */
    public static TokenItem parseToken(String input) {
        Parser p = new Parser(input);
        TokenItem result = p.internalParseToken();
        p.assertEmpty("Extra characters in string parsed as Token");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-binary">Section
     *      4.2.7 of RFC 8941</a>
     */
    public static ByteSequenceItem parseByteSequence(String input) {
        Parser p = new Parser(input);
        ByteSequenceItem result = p.internalParseByteSequence();
        p.assertEmpty("Extra characters in string parsed as Byte Sequence");
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
     *      "https://www.rfc-editor.org/rfc/rfc8941.html#parse-boolean">Section
     *      4.2.8 of RFC 8941</a>
     */
    public static BooleanItem parseBoolean(String input) {
        Parser p = new Parser(input);
        BooleanItem result = p.internalParseBoolean();
        p.assertEmpty("Extra characters at position %d in string parsed as Boolean: '%s'");
        return result;
    }

    // utility methods on CharBuffer

    private static char EOD = (char) -1;

    private void assertEmpty(String message) {
        if (hasRemaining()) {
            throw complaint(String.format(message, position(), input));
        }
    }

    private void advance() {
        input.position(1 + input.position());
    }

    private void backout() {
        input.position(-1 + input.position());
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

    private char getOrEOD() {
        return hasRemaining() ? get() : EOD;
    }

    private boolean hasRemaining() {
        return input.hasRemaining();
    }

    private int length() {
        return input.length();
    }

    private char peek() {
        return hasRemaining() ? input.charAt(0) : EOD;
    }

    private int position() {
        return input.position();
    }

    private void removeLeadingSP() {
        while (checkNextChar(' ')) {
            advance();
        }
    }

    private void removeLeadingOWS() {
        while (checkNextChar(" \t")) {
            advance();
        }
    }

    private ParseException complaint(String message) {
        return new ParseException(message, input);
    }

    private ParseException complaint(String message, Throwable cause) {
        return new ParseException(message, input, cause);
    }

    private ParseException complaint(String message, int position, Throwable cause) {
        return new ParseException(message, input, position, cause);
    }

    private static boolean isHex(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
    }

    private static int decodeHex(char c1, char c2) {
        String lookup="0123456789abcdef";
        return (lookup.indexOf(c1) << 4) | lookup.indexOf(c2);
    }

    private static String format(char c) {
        String s;
        if (c == 9) {
            s = "HTAB";
        } else {
            s = "'" + c + "'";
        }
        return String.format("%s (\\u%04x)", s, (int) c);
    }
}
