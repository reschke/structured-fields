package org.greenbytes.http.sfv;

import java.math.BigDecimal;

public class Parser {

    public static Item parseIntegerOrDecimal(StringBuilder input) {
        String type = "integer";
        int sign = 1;
        int consumed = 0;
        StringBuilder inputNumber = new StringBuilder(32);

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
                if (inputNumber.length() > ("integer".equals(type) ? 15 : 16)) {
                    throw new IllegalArgumentException(type + " too long: " + inputNumber.length());
                }
            }
        }

        input.delete(0, consumed);

        if ("integer".equals(type)) {
            long l = Long.parseLong(inputNumber.toString());
            return new IntegerItem(sign * l);
        } else {
            int len = inputNumber.length();
            int dotPos = inputNumber.indexOf(".");
            if (dotPos == len - 1) {
                throw new IllegalArgumentException("decimal number must not end in '.'");
            } else if (len - dotPos > 3) {
                throw new IllegalArgumentException("maximum number of fractional digits is 3, found: " + (len - dotPos));
            }
            if (sign == -1) {
                inputNumber.insert(0, '-');
            }
            BigDecimal bd = new BigDecimal(inputNumber.toString());
            return new DecimalItem(bd);
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

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
