package org.greenbytes.http.sfv;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static Item parseIntegerOrDecimal(StringBuilder input) {
        String type = "integer";
        int sign = 1;
        int consumed = 0;
        StringBuilder inputNumber = new StringBuilder(20);

        if (input.length() > 0 && input.charAt(consumed) == '-') {
            sign = -1;
            consumed += 1;
        }

        if (input.length() == consumed) {
            throw new IllegalArgumentException("empty Integer or Decimal");
        }

        if (!isDigit(input.charAt(consumed))) {
            throw new IllegalArgumentException("illegal start character for Integer or Decimal: '" + input.charAt(consumed) + "'");
        }

        while (input.length() != consumed) {
            char c = input.charAt(consumed++);
            if (isDigit(c)) {
                inputNumber.append(c);
            } else if ("integer".equals(type) && c == '.') {
                if (inputNumber.length() > 12) {
                    throw new IllegalArgumentException("illegal position for decimal point in Decimal at " + inputNumber.length());
                }
                inputNumber.append(c);
                type = "decimal";
            } else {
                consumed -= 1;
                break;
            }
            if (inputNumber.length() > ("integer".equals(type) ? 15 : 16)) {
                throw new IllegalArgumentException(type + " too long: " + inputNumber.length());
            }
        }

        input.delete(0, consumed);

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
        StringBuilder sb = new StringBuilder(input);
        Item result = parseIntegerOrDecimal(sb);
        if (!(result instanceof IntegerItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is a Decimal");
        } else {
            if (sb.length() != 0) {
                throw new IllegalArgumentException("extra characters in string parsed as integer: '" + sb + "'");
            }
            return (IntegerItem) result;
        }
    }

    public static DecimalItem parseDecimal(String input) {
        StringBuilder sb = new StringBuilder(input);
        Item result = parseIntegerOrDecimal(sb);
        if (!(result instanceof DecimalItem)) {
            throw new IllegalArgumentException("string parsed as integer '" + input + "' is an Integer");
        } else {
            if (sb.length() != 0) {
                throw new IllegalArgumentException("extra characters in string parsed as decimal: '" + sb + "'");
            }
            return (DecimalItem) result;
        }
    }

    private static Item parseString(StringBuilder inputString) {
        int consumed = 0;
        StringBuilder outputString = new StringBuilder(inputString.length());

        if (inputString.length() > 0 && inputString.charAt(consumed++) != '"') {
            throw new IllegalArgumentException("must start with double quote");
        }

        while (inputString.length() != consumed) {
            char c = inputString.charAt(consumed++);
            if (c == '\\') {
                if (inputString.length() == consumed) {
                    throw new IllegalArgumentException("incomplete escape sequence at " + inputString.length());
                }
                c = inputString.charAt(consumed++);
                if (c != '"' && c != '\\') {
                    throw new IllegalArgumentException("invalid escape sequence at " + inputString.length());
                }
                outputString.append(c);
            } else {
                if (c == '"') {
                    inputString.delete(0, consumed);
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
        StringBuilder sb = new StringBuilder(input);
        Item result = parseString(sb);
        if (sb.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as integer: '" + sb + "'");
        }
        return (StringItem) result;
    }

    private static Item parseBareItem(StringBuilder sb) {
        if (sb.length() == 0) {
            throw new IllegalArgumentException("empty string");
        }

        char c = sb.charAt(0);
        if (isDigit(c) || c == '-') {
            return parseIntegerOrDecimal(sb);
        } else if (c == '"') {
            return parseString(sb);
        } else {
            throw new IllegalArgumentException("unknown type: " + sb);
        }
    }

    private static List<Item> parseList(StringBuilder sb) {
        List<Item> result = new ArrayList<>();

        while (sb.length() != 0) {
            result.add(parseBareItem(sb));
            removeLeadingSP(sb);
            if (sb.length() == 0) {
                return result;
            }
            if (sb.charAt(0) != ',') {
                throw new IllegalArgumentException("expected COMMA, got: " + sb);
            }
            sb.deleteCharAt(0);
            removeLeadingSP(sb);
            if (sb.length() == 0) {
                throw new IllegalArgumentException("found trailing COMMA in list");
            }
        }

        // Won't get here
        return result;
    }

    public static ListItem parseList(String input) {
        StringBuilder sb = new StringBuilder(input);
        List<Item> result = parseList(sb);
        if (sb.length() != 0) {
            throw new IllegalArgumentException("extra characters in string parsed as list: '" + sb + "'");
        }
        return new ListItem(result);
    }

    private static void removeLeadingSP(StringBuilder sb) {
        while (sb.length() != 0 && sb.charAt(0) == ' ') {
            sb.deleteCharAt(0);
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
