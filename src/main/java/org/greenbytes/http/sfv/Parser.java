package org.greenbytes.http.sfv;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

public class Parser {

    private final CharBuffer input;

    private Parser(CharBuffer input) {
        this.input = input;
    }

    private Parser(String input) {
        this(CharBuffer.wrap(input));
    }

    private Item<? extends Object> parseBareIntegerOrDecimal() {
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

    private Item<? extends Object> parseIntegerOrDecimal() {
        Item<? extends Object> result = parseBareIntegerOrDecimal();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private StringItem parseBareString() {

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

    private StringItem parseString() {
        StringItem result = parseBareString();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private TokenItem parseBareToken() {

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

    private TokenItem parseToken() {
        TokenItem result = parseBareToken();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private static Base64.Decoder BASE64DECODER = Base64.getDecoder();

    private ByteSequenceItem parseBareByteSequence() {
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

    private ByteSequenceItem parseByteSequence() {
        ByteSequenceItem result = parseBareByteSequence();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private BooleanItem parseBareBoolean() {

        if (getOrEOF() != '?') {
            throw new IllegalArgumentException("must start with question mark: " + input);
        }

        char c = getOrEOF();

        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("expected 0 or 1 in boolean: " + input);
        }

        return BooleanItem.valueOf(c == '1');
    }

    private BooleanItem parseBoolean() {
        BooleanItem result = parseBareBoolean();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private String parseKey() {

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

    private Parameters parseParameters() {

        LinkedHashMap<String, Item<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (hasRemaining() && !done) {
            char c = peek();
            if (c != ';') {
                done = true;
            } else {
                advance();
                removeLeadingSP();
                String name = parseKey();
                Item<? extends Object> value = BooleanItem.valueOf(true);
                if (peek() == '=') {
                    advance();
                    value = parseBareItem();
                }
                result.put(name, value);
            }
        }

        return Parameters.valueOf(result);
    }

    private Item<? extends Object> parseBareItem() {
        if (!hasRemaining()) {
            throw new IllegalArgumentException("empty string");
        }

        char c = peek();
        if (Utils.isDigit(c) || c == '-') {
            return parseBareIntegerOrDecimal();
        } else if (c == '"') {
            return parseBareString();
        } else if (c == '?') {
            return parseBareBoolean();
        } else if (c == '*' || Utils.isAlpha(c)) {
            return parseBareToken();
        } else if (c == ':') {
            return parseBareByteSequence();
        } else {
            throw new IllegalArgumentException("unknown type: " + input);
        }
    }

    private Item<? extends Object> parseItem() {
        Item<? extends Object> result = parseBareItem();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private Item<? extends Object> parseItemOrInnerList() {
        return peek() == '(' ? parseInnerList() : parseItem();
    }

    private List<Item<? extends Object>> parseOuterList() {
        List<Item<? extends Object>> result = new ArrayList<>();

        while (hasRemaining()) {
            result.add(parseItemOrInnerList());
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

    private List<Item<? extends Object>> parseBareInnerList() {

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
                Item<? extends Object> item = parseItem();
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

    private InnerList parseInnerList() {
        List<Item<? extends Object>> result = parseBareInnerList();
        Parameters params = parseParameters();
        return InnerList.valueOf(result).withParams(params);
    }

    private Dictionary parseDictionary() {

        LinkedHashMap<String, Item<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (hasRemaining() && !done) {

            Item<? extends Object> member;

            String name = parseKey();

            if (peek() == '=') {
                advance();
                member = parseItemOrInnerList();
            } else {
                member = BooleanItem.valueOf(true).withParams(parseParameters());
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

    // static convenience methods

    public static IntegerItem parseInteger(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseIntegerOrDecimal();
        if (!(result instanceof IntegerItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is a Decimal");
        } else {
            p.assertEmpty("extra characters in string parsed as integer");
            return (IntegerItem) result;
        }
    }

    public static DecimalItem parseDecimal(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseIntegerOrDecimal();
        if (!(result instanceof DecimalItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is an Integer");
        } else {
            p.assertEmpty("extra characters in string parsed as decimal");
            return (DecimalItem) result;
        }
    }

    public static ByteSequenceItem parseByteSequence(String input) {
        Parser p = new Parser(input);
        ByteSequenceItem result = p.parseByteSequence();
        p.assertEmpty("extra characters in string parsed as byte sequence");
        return result;
    }

    public static StringItem parseString(String input) {
        Parser p = new Parser(input);
        StringItem result = p.parseString();
        p.assertEmpty("extra characters in string parsed as string");
        return result;
    }

    public static TokenItem parseToken(String input) {
        Parser p = new Parser(input);
        TokenItem result = p.parseToken();
        p.assertEmpty("extra characters in string parsed as token");
        return result;
    }

    public static BooleanItem parseBoolean(String input) {
        Parser p = new Parser(input);
        BooleanItem result = p.parseBoolean();
        p.assertEmpty("extra characters in string parsed as boolean");
        return result;
    }

    public static Item<? extends Object> parseItem(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseItem();
        p.assertEmpty("extra characters in string parsed as item");
        return result;
    }

    public static Parameters parseParameters(String input) {
        Parser p = new Parser(input);
        Parameters result = p.parseParameters();
        p.assertEmpty("extra characters in string parsed as parameters");
        return result;
    }

    public static OuterList parseList(String input) {
        Parser p = new Parser(input);
        List<Item<? extends Object>> result = p.parseOuterList();
        p.assertEmpty("extra characters in string parsed as list");
        return OuterList.valueOf(result);
    }

    public static InnerList parseInnerList(String input) {
        Parser p = new Parser(input);
        InnerList result = p.parseInnerList();
        p.assertEmpty("extra characters in string parsed as inner list");
        return result;
    }

    public static Dictionary parseDictionary(String input) {
        Parser p = new Parser(input);
        Dictionary result = p.parseDictionary();
        p.assertEmpty("extra characters in string parsed as dictionary");
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
