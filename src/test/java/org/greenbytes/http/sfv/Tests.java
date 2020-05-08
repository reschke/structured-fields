package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class Tests {

    @Test
    public void testValidIntegers() {
        String tests[] = new String[] { "0", "1", "-1", "999999999999", "-999999999999" };

        for (String s : tests) {
            IntegerItem i = Parser.parseInteger(s);
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
        String tests[] = new String[] { "0.1", "1.345", "123.99", "-1.567", "999999999999.999", "-999999999999.999", "123.0" };

        for (String s : tests) {
            DecimalItem i = Parser.parseDecimal(s);
            assertEquals("should round-trip", s, i.serialize());
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
            StringItem i = Parser.parseString(s);
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

    @Test
    public void testValidLists() {
        Map<String, Object[]> tests = new HashMap<>();

        tests.put("1, 2", new Object[] { 1L, 2L });
        tests.put("1, 1.1, \"foo\"", new Object[] { 1L, BigDecimal.valueOf(1100, 3), "foo" });

        for (Map.Entry<String, Object[]> e : tests.entrySet()) {
            ListItem list = Parser.parseList(e.getKey());
            Object[] expected = e.getValue();
            assertTrue(list instanceof ListItem);
            assertEquals(list.get().size(), expected.length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], list.get().get(i).get());
            }
        }
    }
}
