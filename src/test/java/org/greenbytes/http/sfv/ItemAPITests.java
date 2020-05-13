package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    @Test
    public void testInteger() {

        long tests[] = new long[] { 0L, -0L, 999999999999999L, -999999999999999L };

        for (long l : tests) {
            IntegerItem item = IntegerItem.valueOf(l);
            assertEquals(Long.valueOf(l), item.get());
            assertEquals(l, item.getAsLong());
            assertEquals(Long.valueOf(l).toString(), item.serialize());
        }
    }

    @Test
    public void testIntegerInvalid() {

        Long tests[] = new Long[] { 1000000000000000L, -1000000000000000L };

        for (Long l : tests) {
            try {
                IntegerItem item = IntegerItem.valueOf(l);
                fail("should fail for " + l + " but got " + item.get());
            } catch (IllegalArgumentException expected) {
            }
        }
    }
}
