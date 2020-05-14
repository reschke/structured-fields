package org.greenbytes.http.sfv;

/**
 * Common utility methods.
 */
public class Utils {

    private Utils() {
    }

    protected static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    protected static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    protected static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
