package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class ItemAPITests {

    @Test
    public void testBoolean() {

        BooleanItem b0 = BooleanItem.of(false);
        assertEquals(false, b0.get());
        assertEquals("?0", b0.serialize());
        assertEquals(false, b0.booleanValue());

        BooleanItem b1 = BooleanItem.of(true);
        assertEquals(true, b1.get());
        assertEquals("?1", b1.serialize());

        StringItem s = StringItem.of("");
        assertThrows(UnsupportedOperationException.class, () -> s.booleanValue());
    }

    @Test
    public void testInteger() {

        long[] tests = new long[] { 0L, -0L, 999999999999999L, -999999999999999L };

        for (long l : tests) {
            IntegerItem item = IntegerItem.of(l);
            assertEquals(Long.valueOf(l), item.get());
            assertEquals(l, item.getAsLong());
            assertEquals(l, item.longValue());
            assertEquals(Long.valueOf(l).toString(), item.serialize());
            assertEquals(1, item.getDivisor());
        }
    }

    @Test
    public void testIntegerInvalid() {

        Long[] tests = new Long[] { 1000000000000000L, -1000000000000000L };

        for (Long l : tests) {
            try {
                IntegerItem item = IntegerItem.of(l);
                fail("should fail for " + l + " but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testDecimal() {

        long[] tests = new long[] { 0L, -0L, 999999999999999L, -999999999999999L, -123, 1000, 500, 10, -1 };

        for (long l : tests) {
            DecimalItem item = DecimalItem.valueOf(l);
            assertEquals(BigDecimal.valueOf(l, 3), item.get());
            assertEquals(l, item.getAsLong());
            assertEquals("got: " + item.doubleValue() + ", expected: " + Double.valueOf(l), Double.valueOf(l), item.doubleValue(), 0.0);
            assertEquals(1000, item.getDivisor());
        }
    }

    @Test
    public void testDecimalByBigDecimal() {

        BigDecimal[] tests = new BigDecimal[] { new BigDecimal("0.5"), new BigDecimal(1), BigDecimal.valueOf(-1.1),
                new BigDecimal("0.1234") };

        for (BigDecimal b : tests) {
            BigDecimal permille = b.multiply(new BigDecimal(1000));
            DecimalItem item = DecimalItem.valueOf(b);
            assertEquals(permille.longValue(), item.getAsLong());
        }
    }

    @Test
    public void testDecimalSerializationLeadingZeros() {

        String[] tests = new String[] { "100.012", "1.001", "0.01", "0.001", "-100.012" };

        for (String s : tests) {
            DecimalItem item = DecimalItem.valueOf(new BigDecimal(s));
            assertEquals(s, item.serialize());
        }
    }

    @Test
    public void testDecimalSerializationIntegerValues() {

        String[] tests = new String[] { "0.0", "1000.0", "-1000.0", "5000.0", "123000.0" };

        for (String s : tests) {
            DecimalItem item = DecimalItem.valueOf(new BigDecimal(s));
            assertEquals(s, item.serialize());
        } 
    }

    @Test
    public void testString() {

        String[] tests = new String[] { "", "'", "\"", "\\" };

        for (String s : tests) {
            StringItem item = StringItem.of(s);
            assertEquals(s, item.get());
            assertEquals(s, item.stringValue());
        }
    }

    @Test
    public void testStringInvalid() {

        String[] tests = new String[] { "\n", "\u0080", "\u007f", "\u0000" };

        for (String s : tests) {
            try {
                StringItem item = StringItem.of(s);
                fail("should fail for '" + s + "' but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testToken() {

        String[] tests = new String[] { "*", "x", "*-/", "foo.bar-qux" };

        for (String s : tests) {
            TokenItem item = TokenItem.of(s);
            assertEquals(s, item.get());
            assertEquals(SfDataType.TOKEN, item.getType());
            assertEquals(s, item.tokenValue());
        }
    }

    @Test
    public void testTokenInvalid() {

        String[] tests = new String[] { "123", ".x", "a(b)", "\u0000foo" };

        for (String s : tests) {
            try {
                TokenItem item = TokenItem.of(s);
                fail("should fail for '" + s + "' but got '" + item.get() + "'");
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    @Test
    public void testByteSequence() {

        byte[][] tests = new byte[][] { new byte[0], "x".getBytes() };
        String[] results = new String[] { "::", ":eA==:" };

        for (int i = 0; i < tests.length; i++) {
            ByteSequenceItem item = ByteSequenceItem.valueOf(tests[i]);
            assertArrayEquals(tests[i], item.get().array());
            assertEquals(results[i], item.serialize());
            assertArrayEquals(tests[i], item.byteBufferValue().array());
        }
    }

    @Test
    public void testByteSequenceInvalid() {

        try {
            ByteSequenceItem item = ByteSequenceItem.valueOf(null);
            fail("should fail for '" + item + "' but got '" + item.get() + "'");
        } catch (NullPointerException | IllegalArgumentException expected) {
        }
    }

    @Test
    public void testParameters() {
        Parameters p = createParams1();

        assertEquals(StringItem.class, p.get("*").getClass());
        assertEquals(IntegerItem.class, p.get("i").getClass());
        assertEquals(IntegerItem.class, p.get("l").getClass());
        assertEquals(BooleanItem.class, p.get("b").getClass());
        assertEquals(ByteSequenceItem.class, p.get("o").getClass());
        assertEquals(DecimalItem.class, p.get("d").getClass());
    }

    @Test
    public void testParameters2() {

        Parameters p = createParams2();

        assertEquals(StringItem.class, p.get("*").getClass());
        assertEquals(IntegerItem.class, p.get("i").getClass());
        assertEquals(IntegerItem.class, p.get("l").getClass());
        assertEquals(BooleanItem.class, p.get("b").getClass());
        assertEquals(ByteSequenceItem.class, p.get("o").getClass());
        assertEquals(DecimalItem.class, p.get("d").getClass());
        assertEquals(DecimalItem.class, p.get("d2").getClass());

        assertEquals(";*=\"star\";i=1;l=2;b=?0;o=::;d=0.155;d2=12345.0;d3=3.14", p.serialize());

        Map<String, String> sermap = p.entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().serialize()));

        assertEquals("?0", sermap.get("b"));
        assertEquals("12345.0", sermap.get("d2"));
    }

    @Test
    public void testParametersEquals() {
        Parameters p1 = createParams1();
        Parameters p2 = createParams2();
        assertEquals(p1.serialize(), p2.serialize());
        assertEquals(p1, p2);
    }

    private static Parameters createParams1() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("*", "star");
        m.put("i", 1);
        m.put("l", 2L);
        m.put("b", false);
        m.put("o", new byte[0]);
        m.put("d", new BigDecimal("0.155"));
        m.put("d2", BigDecimal.valueOf(12345));
        m.put("d3", 3.14f);
       return Parameters.of(m);
    }

    private static Parameters createParams2() {
        return Parameters.valueOf("*", "star", "i", 1, "l", 2L, "b", false,
                "o", new byte[0], "d", 0.155d, "d2", BigDecimal.valueOf(12345), "d3", 3.14f);
    }

    @Test
    public void testParametersUnmodifiable() {

        Map<String, Object> m = new HashMap<>();
        m.put("test", "test");
        Parameters p = Parameters.of(m);

        assertThrows(
                UnsupportedOperationException.class,
                p::clear);
    }

    @Test
    public void testInvalidParameterKeys() {

        String[] tests = { "Aa", "-a", "/a", "", " ", "1" };
        Map<String, Object> m = new LinkedHashMap<>();
        for (String key : tests) {
            m.clear();
            m.put(key, IntegerItem.of(1));
            assertThrows("should throe",
                    IllegalArgumentException.class,
                    () -> Parameters.valueOf(m));
        }
    }

    @Test
    public void testInvalidParameterValues() {

        Map<String, Object> itemParam = new LinkedHashMap<>();
        itemParam.put("foo", IntegerItem.of(2));
        IntegerItem iitem = IntegerItem.of(1).withParams(Parameters.of(itemParam));

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("bar", iitem);
        try {
            Parameters test = Parameters.of(m);
            fail("Parameters containing non-bare Item should fail, but got: " + test.serialize());
        } catch (IllegalArgumentException expected) {
        }
    }
}
