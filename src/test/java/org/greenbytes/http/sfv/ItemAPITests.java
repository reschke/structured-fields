package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

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
            assertEquals(1, item.getDivisor());
        }
    }

    @Test
    public void testIntegerInvalid() {

        Long tests[] = new Long[] { 1000000000000000L, -1000000000000000L };

        for (Long l : tests) {
            try {
                IntegerItem item = IntegerItem.valueOf(l);
                fail("should fail for " + l + " but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testDecimal() {

        long tests[] = new long[] { 0L, -0L, 999999999999999L, -999999999999999L, -123, 1000, 500, 10, -1 };

        for (long l : tests) {
            DecimalItem item = DecimalItem.valueOf(l);
            assertEquals(BigDecimal.valueOf(l, 3), item.get());
            assertEquals(l, item.getAsLong());
            assertEquals(1000, item.getDivisor());
            // TODO: figure out how to check the serialization without copying
            // the actual impl code
            // assertEquals(BigDecimal.valueOf(l, 3).toPlainString(),
            // item.serialize());
        }
    }

    @Test
    public void testString() {

        String tests[] = new String[] { "", "'", "\"", "\\" };

        for (String s : tests) {
            StringItem item = StringItem.valueOf(s);
            assertEquals(s, item.get());
            // TODO: figure out how to check the serialization without copying
            // the actual impl code
        }
    }

    @Test
    public void testStringInvalid() {

        String tests[] = new String[] { "\n", "\u0080", "\u007f", "\u0000" };

        for (String s : tests) {
            try {
                StringItem item = StringItem.valueOf(s);
                fail("should fail for '" + s + "' but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testToken() {

        String tests[] = new String[] { "*", "x", "*-/", "foo.bar-qux" };

        for (String s : tests) {
            TokenItem item = TokenItem.valueOf(s);
            assertEquals(s, item.get());
            // TODO: figure out how to check the serialization without copying
            // the actual impl code
        }
    }

    @Test
    public void testTokenInvalid() {

        String tests[] = new String[] { "123", ".x", "a(b)", "\u0000foo" };

        for (String s : tests) {
            try {
                TokenItem item = TokenItem.valueOf(s);
                fail("should fail for '" + s + "' but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testByteSequence() {

        byte tests[][] = new byte[][] { new byte[0], "x".getBytes() };
        String results[] = new String[] { "::", ":eA==:" };

        for (int i = 0; i < tests.length; i++) {
            ByteSequenceItem item = ByteSequenceItem.valueOf(tests[i]);
            assertEquals(tests[i], item.get().array());
            assertEquals(results[i], item.serialize());
        }
    }

    @Test
    public void testByteSequenceInvalid() {

        byte tests[][] = new byte[][] { null };

        for (byte[] ba : tests) {
            try {
                ByteSequenceItem item = ByteSequenceItem.valueOf(ba);
                fail("should fail for '" + item + "' but got '" + item.get() + "'");
            } catch (NullPointerException | IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testParameters() {

        Map<String, Item<? extends Object>> m = new LinkedHashMap<>();
        m.put("*", IntegerItem.valueOf(1));
        Parameters p = Parameters.valueOf(m);
        assertEquals(m, p.get());
    }

    @Test
    public void testInvalidParameters() {

        String tests[] = { "Aa", "-a", "/a", "", " ", "1" };
        Map<String, Item<? extends Object>> m = new LinkedHashMap<>();
        for (String key : tests) {
            m.clear();
            m.put(key, IntegerItem.valueOf(1));
            try {
                Parameters p = Parameters.valueOf(m);
                fail("should fail for key '" + key + "' but got: " + p.get());
            } catch (IllegalArgumentException ex) {
            }
        }
    }
}
