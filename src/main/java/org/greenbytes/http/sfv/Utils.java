package org.greenbytes.http.sfv;

import java.util.Map;
import java.util.Objects;

/**
 * Common utility methods.
 */
public class Utils {

    private Utils() {
    }

    /** check for character to be a decimal digit */
    protected static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /** check for character to be lowercase alphanumeric */
    protected static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /** check for character to be alphanumeric */
    protected static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Checks a key for validity
     * @param value to check
     * @return checked value
     */
    protected static String checkKey(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Key can not be null or empty");
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((i == 0 && (c != '*' && !isLcAlpha(c)))
                    || !(isLcAlpha(c) || isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*')) {
                throw new IllegalArgumentException(
                        String.format("Invalid character in key at position %d: '%c' (0x%04x)", i, c, (int) c));
            }
        }
        return value;
    }

    /**
     * Checks all keys in map for validity
     * @param value map to check
     * @return checked map
     */
    protected static Map<String, ListElement<?>> checkKeys(Map<String, ListElement<?>> value) {
        for (String key : Objects.requireNonNull(value, "value must not be null").keySet()) {
            checkKey(key);
        }
        return value;
    }
}
