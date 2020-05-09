package org.greenbytes.http.sfv;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static Item<? extends Object> parseIntegerOrDecimal(CharBuffer input) {
        String type = "integer";
        int sign = 1;
        StringBuilder inputNumber = new StringBuilder(20);

        if (checkNextChar(input, '-')) {
            sign = -1;
            advance(input);
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

    public static IntegerItem parseInteger(String input) {
        CharBuffer sb = CharBuffer.wrap(input);
        Item<? extends Object> result = parseIntegerOrDecimal(sb);
        if (!(result instanceof IntegerItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is a Decimal");
        } else {
            if (sb.hasRemaining()) {
                throw new IllegalArgumentException("extra characters in string parsed as integer: '" + sb + "'");
            }
            return (IntegerItem) result;
        }
    }

    public static DecimalItem parseDecimal(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        Item<? extends Object> result = parseIntegerOrDecimal(buffer);
        if (!(result instanceof DecimalItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is an Integer");
        } else {
            if (buffer.hasRemaining()) {
                throw new IllegalArgumentException("extra characters in string parsed as decimal: '" + buffer + "'");
            }
            return (DecimalItem) result;
        }
    }

    private static StringItem parseString(CharBuffer inputString) {

        if (getOrEOF(inputString) != '"') {
            throw new IllegalArgumentException("must start with double quote: " + inputString);
        }

        StringBuilder outputString = new StringBuilder(inputString.length());

        while (inputString.hasRemaining()) {
            char c = inputString.get();
            if (c == '\\') {
                c = getOrEOF(inputString);
                if (c != '"' && c != '\\') {
                    throw new IllegalArgumentException("invalid escape sequence at " + inputString.position());
                }
                outputString.append(c);
            } else {
                if (c == '"') {
                    return new StringItem(outputString.toString());
                } else if (c < 0x20 || c >= 0x7f) {
                    throw new IllegalArgumentException("invalid character at " + inputString.length());
                } else {
                    outputString.append(c);
                }
            }
        }

        throw new IllegalArgumentException("closing double quote missing");
    }

    public static StringItem parseString(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        Item<String> result = parseString(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as integer: '" + buffer + "'");
        }
        return (StringItem) result;
    }

    private static TokenItem parseToken(CharBuffer inputString) {

        char c = getOrEOF(inputString);
        if (c != '*' && !isAlpha(c)) {
                throw new IllegalArgumentException("must start with ALPHA or *: " + inputString);
        }

        StringBuilder outputString = new StringBuilder(inputString.length());
        outputString.append(c);

        while (inputString.hasRemaining()) {
            c = inputString.get();
            if (c < ' ' || c >= 0x7f || "\"(),;<=>?@[\\]{}".indexOf(c) >= 0) {
                throw new IllegalArgumentException("not allowed in token: " + inputString);
            }
            outputString.append(c);
        }

        return new TokenItem(outputString.toString());
    }

    public static TokenItem parseToken(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        Item<String> result = parseToken(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as token: '" + buffer + "'");
        }
        return (TokenItem) result;
    }

    private static BooleanItem parseBoolean(CharBuffer inputString) {

        if (getOrEOF(inputString) != '?') {
            throw new IllegalArgumentException("must start with question mark: " + inputString);
        }

        char c = getOrEOF(inputString);

        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("expected 0 or 1 in boolean: " + inputString);
        }

        return new BooleanItem(c == '1');
    }

    public static BooleanItem parseBoolean(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        Item<Boolean> result = parseBoolean(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as boolean: '" + buffer + "'");
        }
        return (BooleanItem) result;
    }

    private static Item<? extends Object> parseBareItem(CharBuffer buffer) {
        if (!buffer.hasRemaining()) {
            throw new IllegalArgumentException("empty string");
        }

        char c = buffer.charAt(0);
        if (isDigit(c) || c == '-') {
            return parseIntegerOrDecimal(buffer);
        } else if (c == '"') {
            return parseString(buffer);
        } else if (c == '?') {
            return parseBoolean(buffer);
        } else if (c == '*' || isAlpha(c)) {
            return parseToken(buffer);
        } else {
            throw new IllegalArgumentException("unknown type: " + buffer);
        }
    }

    private static List<Item<? extends Object>> parseList(CharBuffer sb) {
        List<Item<? extends Object>> result = new ArrayList<>();

        while (sb.hasRemaining()) {
            result.add(parseBareItem(sb));
            removeLeadingSP(sb);
            if (!sb.hasRemaining()) {
                return result;
            }
            if (sb.get() != ',') {
                throw new IllegalArgumentException("expected COMMA, got: " + sb);
            }
            removeLeadingSP(sb);
            if (!sb.hasRemaining()) {
                throw new IllegalArgumentException("found trailing COMMA in list");
            }
        }

        // Won't get here
        return result;
    }

    public static ListItem parseList(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        List<Item<? extends Object>> result = parseList(buffer);
        if (buffer.hasRemaining()) {
            throw new IllegalArgumentException("extra characters in string parsed as list: '" + buffer + "'");
        }
        return new ListItem(result);
    }

    private static void removeLeadingSP(CharBuffer sb) {
        while (checkNextChar(sb, ' ')) {
            advance(sb);
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    // utility methods on CharBuffer

    private static boolean checkNextChar(CharBuffer buffer, char c) {
        return buffer.hasRemaining() && buffer.charAt(0) == c;
    }

    private static boolean checkNextChar(CharBuffer buffer, String valid) {
        return buffer.hasRemaining() && valid.indexOf(buffer.charAt(0)) >= 0;
    }

    private static void advance(CharBuffer buffer) {
        buffer.position(1 + buffer.position());
    }

    private static char getOrEOF(CharBuffer buffer) {
        return buffer.hasRemaining() ? buffer.get() : (char) -1;
    }
}
