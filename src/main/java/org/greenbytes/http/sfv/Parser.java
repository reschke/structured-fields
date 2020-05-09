package org.greenbytes.http.sfv;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private static Base64.Decoder BASE64DECODER = Base64.getDecoder();

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
        StringItem result = parseString(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as integer: '" + buffer + "'");
        }
        return result;
    }

    private static TokenItem parseToken(CharBuffer inputString) {

        char c = getOrEOF(inputString);
        if (c != '*' && !isAlpha(c)) {
            throw new IllegalArgumentException("must start with ALPHA or *: " + inputString);
        }

        StringBuilder outputString = new StringBuilder(inputString.length());
        outputString.append(c);
        boolean done = false;

        while (inputString.hasRemaining() && !done) {
            c = inputString.charAt(0);
            if (c < ' ' || c >= 0x7f || "\"(),;<=>?@[\\]{}".indexOf(c) >= 0) {
                done = true;
            }
            else {
                advance(inputString);
                outputString.append(c);
            }
        }

        return new TokenItem(outputString.toString());
    }

    public static TokenItem parseToken(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        TokenItem result = parseToken(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as token: '" + buffer + "'");
        }
        return result;
    }

    private static ByteSequenceItem parseByteSequence(CharBuffer inputString) {
        if (getOrEOF(inputString) != ':') {
            throw new IllegalArgumentException("must start with colon: " + inputString);
        }

        StringBuilder outputString = new StringBuilder(inputString.length());
        boolean done = false;

        while (inputString.hasRemaining() && !done) {
            char c = inputString.get();
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

    public static ByteSequenceItem parseByteSequence(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        ByteSequenceItem result = parseByteSequence(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as byte sequence: '" + buffer + "'");
        }
        return result;
    }

    private static BooleanItem parseBoolean(CharBuffer inputString) {

        if (getOrEOF(inputString) != '?') {
            throw new IllegalArgumentException("must start with question mark: " + inputString);
        }

        char c = getOrEOF(inputString);

        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("expected 0 or 1 in boolean: " + inputString);
        }

        return BooleanItem.valueOf(c == '1');
    }

    public static BooleanItem parseBoolean(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        BooleanItem result = parseBoolean(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as boolean: '" + buffer + "'");
        }
        return result;
    }

    private static String parseKey(CharBuffer buffer) {

        char c = getOrEOF(buffer);
        if (c != '*' && !isLcAlpha(c)) {
            throw new IllegalArgumentException("must start with LCALPHA or *: " + buffer);
        }

        StringBuilder result = new StringBuilder();
        result.append(c);

        boolean done = false;
        while (buffer.hasRemaining() && !done) {
            c = buffer.charAt(0);
            if (isLcAlpha(c) || isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*') {
                result.append(c);
                advance(buffer);
            } else {
                done = true;
            }
        }

        return result.toString();
    }

    private static Parameters parseParameters(CharBuffer buffer) {

        LinkedHashMap<String, Item<? extends Object>> result = new LinkedHashMap<>();

        boolean done = false;
        while (buffer.hasRemaining() && !done) {
            char c = buffer.charAt(0);
            if (c != ';') {
                done = true;
            } else {
                advance(buffer);
                removeLeadingSP(buffer);
                String name = parseKey(buffer);
                Item<? extends Object> value = BooleanItem.valueOf(true);
                if (buffer.hasRemaining() && buffer.charAt(0) == '=') {
                    advance(buffer);
                    value = parseBareItem(buffer);
                }
                result.put(name, value);
            }
        }

        return new Parameters(result);
    }

    public static Parameters parseParameters(String input) {
        CharBuffer buffer = CharBuffer.wrap(input);
        Parameters result = parseParameters(buffer);
        if (buffer.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as parameters: '" + buffer + "'");
        }
        return result;
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
        } else if (c == ':') {
            return parseByteSequence(buffer);
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

    private static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
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
