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
        String tests[] = new String[] { "0", "1", "-1", "999999999999", "-999999999999", "3;a=b" };

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
        String tests[] = new String[] { "0.1", "1.345", "123.99", "-1.567", "999999999999.999", "-999999999999.999", "123.0", "3.14;this-is-pi" };

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
        String tests[] = new String[] { "\"\"", "\"abc\"", "\"a\\\\\\\"b\"", "\"a\";c=2" };

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
    public void testValidTokens() {
        String tests[] = new String[] { "x", "a2", "C", "text/plain;q=0.123", "foo:bar" };

        for (String s : tests) {
            TokenItem i = Parser.parseToken(s);
            assertEquals("should round-trip", s, i.serialize());
        }
    }

    @Test
    public void testInvalidTokens() {
        String tests[] = new String[] { "", "1", "a(b)", "3, ::" };

        for (String s : tests) {
            try {
                Parser.parseToken(s);
                org.junit.Assert.fail("should not parse as token: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testValidBooleans() {
        String tests[] = new String[] { "?0", "?1", "?0;maybe" };

        for (String s : tests) {
            BooleanItem i = Parser.parseBoolean(s);
            assertEquals("should round-trip", i.serialize(), s);
        }
    }

    @Test
    public void testInvalidBooleans() {
        String tests[] = new String[] { "?", "1", "?0 " };

        for (String s : tests) {
            try {
                Parser.parseBoolean(s);
                org.junit.Assert.fail("should not parse as boolean: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testValidByteSequences() {
        String tests[] = new String[] { ":cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:;foo=bar" };

        for (String s : tests) {
            ByteSequenceItem i = Parser.parseByteSequence(s);
            assertEquals("should round-trip", i.serialize(), s);
        }
    }

    @Test
    public void testInvalidByteSequences() {
        String tests[] = new String[] { "cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==",
                ":cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==", "cHJld\nGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:" };

        for (String s : tests) {
            try {
                Parser.parseByteSequence(s);
                org.junit.Assert.fail("should not parse as byte sequence: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testValidParameters() {
        Map<String, String> tests = new HashMap<>();

        tests.put("; a=b;c=1;d=?1;s=\"foo\"", ";a=b;c=1;d;s=\"foo\"");
        tests.put(";a=1;b=2;a=3", ";a=3;b=2");

        for (Map.Entry<String, String> e : tests.entrySet()) {
            Parameters i = Parser.parseParameters(e.getKey());
            assertEquals("should round-trip as ", e.getValue(), i.serialize());
        }
    }

    @Test
    public void testValidLists() {
        Map<String, Object[]> tests = new HashMap<>();

        tests.put("1, 2", new Object[] { 1L, 2L });
        tests.put("1, 1.1, \"foo\", ?0, a2, :Zg==:", new Object[] { 1L, BigDecimal.valueOf(1100, 3), "foo", Boolean.FALSE, "a2",
                new ByteSequenceItem("f".getBytes()).get() });

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
