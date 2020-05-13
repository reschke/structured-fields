package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ItemAPITests {

    @Test
    public void testBoolean() {

        BooleanItem b0 = BooleanItem.valueOf(false);
        assertEquals(false, b0.get());
        assertEquals("?0", b0.serialize());

        BooleanItem b1 = BooleanItem.valueOf(true);
        assertEquals(true, b1.get());
        assertEquals("?1", b1.serialize());
    }
}
