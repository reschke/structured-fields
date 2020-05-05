package org.greenbytes.http.sfv;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Tests {

    @Test
    public void testIntegers() {
        Item i = Parser.parseInteger("0");
        assertTrue(i instanceof IntegerItem);
        assertEquals("should round-trip", i.serialize(), "0");
    }
}
