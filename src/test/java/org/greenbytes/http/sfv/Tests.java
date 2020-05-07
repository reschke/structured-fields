package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Tests {

    @Test
    public void testValidIntegers() {
        String tests[] = new String[] { "0", "1", "-1", "999999999999", "-999999999999" };

        for (String s : tests) {
            Item i = Parser.parseInteger(s);
            assertTrue(i instanceof IntegerItem);
            assertEquals("should round-trip", i.serialize(), s);
        }
    }

    @Test
    public void testInvalidIntegers() {
        String tests[] = new String[] { "a", "1a", "1.", "9999999999999999", "-9999999999999999", "0999999999999999", "1-2",
                "3 4" };

        for (String s : tests) {
            try {
                Parser.parseInteger(s);
                org.junit.Assert.fail("should not parse as integer: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testValidDecimals() {
        String tests[] = new String[] { "0.1", "1.345", "-1.567", "999999999999.999", "-999999999999.999" };

        for (String s : tests) {
            Item i = Parser.parseDecimal(s);
            assertTrue(i instanceof DecimalItem);
            assertEquals("should round-trip", i.serialize(), s);
        }
    }

    @Test
    public void testInvalidDecimals() {
        String tests[] = new String[] { " 0.1", "1.3453", "-1.56.7", "99999999999999.90", "-99999999999999.90" };

        for (String s : tests) {
            try {
                Parser.parseDecimal(s);
                org.junit.Assert.fail("should not parse as decimal: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testValidStrings() {
        String tests[] = new String[] { "\"\"", "\"abc\"", "\"a\\\\\\\"b\"" };

        for (String s : tests) {
            Item i = Parser.parseString(s);
            assertTrue(i instanceof StringItem);
            assertEquals("should round-trip", s, i.serialize());
        }
    }

    @Test
    public void testInvalidStrings() {
        String tests[] = new String[] { "\"abc", "\"\\g\"" };

        for (String s : tests) {
            try {
                Parser.parseString(s);
                org.junit.Assert.fail("should not parse as string: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }
}
