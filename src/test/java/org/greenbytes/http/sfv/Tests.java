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
        String tests[] = new String[] { "a", "1a", "1.", "9999999999999999", "-9999999999999999", "0999999999999999", "1-2", "3 4" };

        for (String s : tests) {
            try {
                Parser.parseInteger(s);
                org.junit.Assert.fail("should not parse as integer: " + s);
            } catch (IllegalArgumentException expected) {
            }
        }
    }
}
