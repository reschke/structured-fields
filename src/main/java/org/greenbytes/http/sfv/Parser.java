package org.greenbytes.http.sfv;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

public class Parser {

    private static Base64.Decoder BASE64DECODER = Base64.getDecoder();

    private final CharBuffer input;

    public Parser(CharBuffer input) {
        this.input = input;
    }

    public Parser(String input) {
        this(CharBuffer.wrap(input));
    }

    public Item<? extends Object> parseIntegerOrDecimal() {
        String type = "integer";
        int sign = 1;
        StringBuilder inputNumber = new StringBuilder(20);

        if (checkNextChar('-')) {
            sign = -1;
            advance();
        }

        if (!checkNextChar(input, "0123456789")) {
            throw new IllegalArgumentException("illegal start for Integer or Decimal: '" + input + "'");
        }

        while (input.hasRemaining()) {
            input.mark();
            char c = input.get();
            if (isDigit(c)) {
                inputNumber.append(c);
            } else if ("integer".equals(type) && c == '.') {
                if (inputNumber.length() > 12) {
                    throw new IllegalArgumentException("illegal position for decimal point in Decimal at " + inputNumber.length());
                }
                inputNumber.append(c);
                type = "decimal";
            } else {
                input.reset();
                break;
            }
            if (inputNumber.length() > ("integer".equals(type) ? 15 : 16)) {
                throw new IllegalArgumentException(type + " too long: " + inputNumber.length());
            }
        }

        if ("integer".equals(type)) {
            long l = Long.parseLong(inputNumber.toString());
            return new IntegerItem(sign * l);
        } else {
            int dotPos = inputNumber.indexOf(".");
            int fracLen = inputNumber.length() - inputNumber.indexOf(".") - 1;

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
            return new DecimalItem(sign * l);
        }
    }

    private Item<? extends Object> parseIntegerOrDecimalWithParams() {
        Item<? extends Object> result = parseIntegerOrDecimal();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private StringItem parseString() {

        if (getOrEOF() != '"') {
            throw new IllegalArgumentException("must start with double quote: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(input.length());

        while (input.hasRemaining()) {
            char c = input.get();
            if (c == '\\') {
                c = getOrEOF();
                if (c != '"' && c != '\\') {
                    throw new IllegalArgumentException("invalid escape sequence at " + input.position());
                }
                outputString.append(c);
            } else {
                if (c == '"') {
                    return new StringItem(outputString.toString());
                } else if (c < 0x20 || c >= 0x7f) {
                    throw new IllegalArgumentException("invalid character at " + input.length());
                } else {
                    outputString.append(c);
                }
            }
        }

        throw new IllegalArgumentException("closing double quote missing");
    }

    private StringItem parseStringWithParameters() {
        StringItem result = parseString();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private TokenItem parseToken() {

        char c = getOrEOF();
        if (c != '*' && !isAlpha(c)) {
            throw new IllegalArgumentException("must start with ALPHA or *: '" + input + "'");
        }

        StringBuilder outputString = new StringBuilder(input.length());
        outputString.append(c);

        boolean done = false;
        while (input.hasRemaining() && !done) {
            c = input.charAt(0);
            if (c <= ' ' || c >= 0x7f || "\"(),;<=>?@[\\]{}".indexOf(c) >= 0) {
                done = true;
            } else {
                advance();
                outputString.append(c);
            }
        }

        return new TokenItem(outputString.toString());
    }

    private TokenItem parseTokenWithParams() {
        TokenItem result = parseToken();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private ByteSequenceItem parseByteSequence() {
        if (getOrEOF() != ':') {
            throw new IllegalArgumentException("must start with colon: " + input);
        }

        StringBuilder outputString = new StringBuilder(input.length());

        boolean done = false;
        while (input.hasRemaining() && !done) {
            char c = input.get();
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
        return new ByteSequenceItem(BASE64DECODER.decode(outputString.toString()));
    }

    private ByteSequenceItem parseByteSequenceWithParams() {
        ByteSequenceItem result = parseByteSequence();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private BooleanItem parseBoolean() {

        if (getOrEOF() != '?') {
            throw new IllegalArgumentException("must start with question mark: " + input);
        }

        char c = getOrEOF();

        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("expected 0 or 1 in boolean: " + input);
        }

        return BooleanItem.valueOf(c == '1');
    }

    private BooleanItem parseBooleanWithParams() {
        BooleanItem result = parseBoolean();
        Parameters params = parseParameters();
        return result.withParams(params);
    }

    private String parseKey() {

        char c = getOrEOF();
        if (c != '*' && !isLcAlpha(c)) {
            throw new IllegalArgumentException("must start with LCALPHA or *: " + input);
        }

        StringBuilder result = new StringBuilder();
        result.append(c);

        boolean done = false;
        while (input.hasRemaining() && !done) {
            c = input.charAt(0);
            if (isLcAlpha(c) || isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*') {
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
        while (input.hasRemaining() && !done) {
            char c = input.charAt(0);
            if (c != ';') {
                done = true;
            } else {
                advance();
                removeLeadingSP();
                String name = parseKey();
                Item<? extends Object> value = BooleanItem.valueOf(true);
                if (input.hasRemaining() && input.charAt(0) == '=') {
                    advance();
                    value = parseBareItem();
                }
                result.put(name, value);
            }
        }

        return new Parameters(result);
    }

    private Item<? extends Object> parseBareItem() {
        if (!input.hasRemaining()) {
            throw new IllegalArgumentException("empty string");
        }

        char c = input.charAt(0);
        if (isDigit(c) || c == '-') {
            return parseIntegerOrDecimal();
        } else if (c == '"') {
            return parseString();
        } else if (c == '?') {
            return parseBoolean();
        } else if (c == '*' || isAlpha(c)) {
            return parseToken();
        } else if (c == ':') {
            return parseByteSequence();
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
        char c = input.hasRemaining() ? input.charAt(0) : 1;
        return c == '(' ? parseInnerListWithParams() : parseItem();
    }

    private List<Item<? extends Object>> parseList() {
        List<Item<? extends Object>> result = new ArrayList<>();

        while (input.hasRemaining()) {
            result.add(parseItemOrInnerList());
            removeLeadingSP();
            if (!input.hasRemaining()) {
                return result;
            }
            if (input.get() != ',') {
                throw new IllegalArgumentException("expected COMMA, got: " + input);
            }
            removeLeadingSP();
            if (!input.hasRemaining()) {
                throw new IllegalArgumentException("found trailing COMMA in list");
            }
        }

        // Won't get here
        return result;
    }

    private List<Item<? extends Object>> parseInnerList() {

        char c = getOrEOF();
        if (c != '(') {
            throw new IllegalArgumentException("inner list must start with '(': " + input);
        }

        List<Item<? extends Object>> result = new ArrayList<>();

        boolean done = false;
        while (input.hasRemaining() && !done) {
            removeLeadingSP();

            c = input.charAt(0);
            if (c == ')') {
                advance();
                done = true;
            } else {
                Item<? extends Object> item = parseItem();
                result.add(item);
            }
        }

        if (!done) {
            throw new IllegalArgumentException("inner list must end with ')': " + input);
        }

        return result;
    }

    private ListItem parseInnerListWithParams() {
        List<Item<? extends Object>> result = parseInnerList();
        Parameters params = parseParameters();
        return new ListItem(true, result).withParams(params);
    }

    // static convenience methods

    public static IntegerItem parseInteger(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseIntegerOrDecimalWithParams();
        if (!(result instanceof IntegerItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is a Decimal");
        } else {
            p.assertEmpty("extra characters in string parsed as integer");
            return (IntegerItem) result;
        }
    }

    public static DecimalItem parseDecimal(String input) {
        Parser p = new Parser(input);
        Item<? extends Object> result = p.parseIntegerOrDecimalWithParams();
        if (!(result instanceof DecimalItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is an Integer");
        } else {
            p.assertEmpty("extra characters in string parsed as decimal");
            return (DecimalItem) result;
        }
    }

    public static ByteSequenceItem parseByteSequence(String input) {
        Parser p = new Parser(input);
        ByteSequenceItem result = p.parseByteSequenceWithParams();
        p.assertEmpty("extra characters in string parsed as byte sequence");
        return result;
    }

    public static StringItem parseString(String input) {
        Parser p = new Parser(input);
        StringItem result = p.parseStringWithParameters();
        p.assertEmpty("extra characters in string parsed as string");
        return result;
    }

    public static TokenItem parseToken(String input) {
        Parser p = new Parser(input);
        TokenItem result = p.parseTokenWithParams();
        p.assertEmpty("extra characters in string parsed as token");
        return result;
    }

    public static BooleanItem parseBoolean(String input) {
        Parser p = new Parser(input);
        BooleanItem result = p.parseBooleanWithParams();
        p.assertEmpty("extra characters in string parsed as boolean");
        return result;
    }

    public static Parameters parseParameters(String input) {
        Parser p = new Parser(input);
        Parameters result = p.parseParameters();
        p.assertEmpty("extra characters in string parsed as parameters");
        return result;
    }

    public static ListItem parseList(String input) {
        Parser p = new Parser(input);
        List<Item<? extends Object>> result = p.parseList();
        p.assertEmpty("extra characters in string parsed as list");
        return new ListItem(false, result);
    }

    public static ListItem parseInnerList(String input) {
        Parser p = new Parser(input);
        ListItem result = p.parseInnerListWithParams();
        p.assertEmpty("extra characters in string parsed as inner list");
        return result;
    }

    // character types

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    private static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    // utility methods on CharBuffer

    private void removeLeadingSP() {
        while (checkNextChar(' ')) {
            advance();
        }
    }

    private boolean checkNextChar(char c) {
        return input.hasRemaining() && input.charAt(0) == c;
    }

    private static boolean checkNextChar(CharBuffer buffer, String valid) {
        return buffer.hasRemaining() && valid.indexOf(buffer.charAt(0)) >= 0;
    }

    private void advance() {
        input.position(1 + input.position());
    }

    private void assertEmpty(String message) {
        if (input.hasRemaining()) {
            throw new IllegalArgumentException(message + ": '" + input + "'");
        }
    }

    private char getOrEOF() {
        return input.hasRemaining() ? input.get() : (char) -1;
    }
}
