package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        long[] tests = new long[] { 0L, -0L, 999999999999999L, -999999999999999L };

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

        Long[] tests = new Long[] { 1000000000000000L, -1000000000000000L };

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

        long[] tests = new long[] { 0L, -0L, 999999999999999L, -999999999999999L, -123, 1000, 500, 10, -1 };

        for (long l : tests) {
            DecimalItem item = DecimalItem.valueOf(l);
            assertEquals(BigDecimal.valueOf(l, 3), item.get());
            assertEquals(l, item.getAsLong());
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
            StringItem item = StringItem.valueOf(s);
            assertEquals(s, item.get());
            // TODO: figure out how to check the serialization without copying
            // the actual impl code
        }
    }

    @Test
    public void testStringInvalid() {

        String[] tests = new String[] { "\n", "\u0080", "\u007f", "\u0000" };

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

        String[] tests = new String[] { "*", "x", "*-/", "foo.bar-qux" };

        for (String s : tests) {
            TokenItem item = TokenItem.valueOf(s);
            assertEquals(s, item.get());
            // TODO: figure out how to check the serialization without copying
            // the actual impl code
        }
    }

    @Test
    public void testTokenInvalid() {

        String[] tests = new String[] { "123", ".x", "a(b)", "\u0000foo" };

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

        byte[][] tests = new byte[][] { new byte[0], "x".getBytes() };
        String[] results = new String[] { "::", ":eA==:" };

        for (int i = 0; i < tests.length; i++) {
            ByteSequenceItem item = ByteSequenceItem.valueOf(tests[i]);
            assertArrayEquals(tests[i], item.get().array());
            assertEquals(results[i], item.serialize());
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
       return Parameters.valueOf(m);
    }

    private static Parameters createParams2() {
        return Parameters.valueOf("*", "star", "i", 1, "l", 2L, "b", false,
                "o", new byte[0], "d", 0.155d, "d2", BigDecimal.valueOf(12345), "d3", 3.14f);
    }

    @Test
    public void testParametersUnmodifiable() {

        Map<String, Object> m = new HashMap<>();
        m.put("test", "test");
        Parameters p = Parameters.valueOf(m);

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
            m.put(key, IntegerItem.valueOf(1));
            assertThrows("should throe",
                    IllegalArgumentException.class,
                    () -> Parameters.valueOf(m));
        }
    }

    @Test
    public void testInvalidParameterValues() {

        Map<String, Object> itemParam = new LinkedHashMap<>();
        itemParam.put("foo", IntegerItem.valueOf(2));
        IntegerItem iitem = IntegerItem.valueOf(1).withParams(Parameters.valueOf(itemParam));

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("bar", iitem);
        try {
            Parameters test = Parameters.valueOf(m);
            fail("Parameters containing non-bare Item should fail, but got: " + test.serialize());
        } catch (IllegalArgumentException expected) {
        }
    }


    @Test
    public void testDictConstructionSimple() {
        // RFC 9651, Section 3.2
        Dictionary dict1 = createDictionarySimple1();
        Dictionary dict2 = createDictionarySimple2();
        assertEquals("en=\"Applepie\", da=:w4ZibGV0w6ZydGU=:", dict1.serialize());
        assertEquals("en=\"Applepie\", da=:w4ZibGV0w6ZydGU=:", dict2.serialize());
        assertEquals(dict1, dict2);
    }

    private static Dictionary createDictionarySimple1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("en", StringItem.of("Applepie"));
        map.put("da", ByteSequenceItem.valueOf("Æbletærte".getBytes(StandardCharsets.UTF_8)));
        return Dictionary.of(map);
    }

    private static Dictionary  createDictionarySimple2() {
        return Dictionary.valueOf(
                "en", "Applepie",
                "da", "Æbletærte".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testDictConstruction() {
        // RFC 9651, Section 3.2
        // Example-Dict: a=?0, b, c; foo=bar
        Dictionary dict1 = createDictionary1();
        Dictionary dict2 = createDictionary2();
        assertEquals("a=?0, b, c;foo=bar",  dict1.serialize());
        assertEquals("a=?0, b, c;foo=bar",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    private static Dictionary createDictionary1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("a", BooleanItem.valueOf(false));
        map.put("b", BooleanItem.valueOf(true));
        map.put("c", BooleanItem.valueOf(true).withParamValuesOf("foo", TokenItem.valueOf("bar")));
        return Dictionary.of(map);
    }

    private static Dictionary createDictionary2() {
        return Dictionary.valueOf(
                "a", false,
                "b", true,
                "c", BooleanItem.of(true).withParamValuesOf("foo", TokenItem.valueOf("bar")));
    }

    @Test
    public void testDictConstructionWithInnerList() {
        // RFC 9651, Section 3.2
        // Example-Dict: rating=1.5, feelings=(joy sadness)
        Dictionary dict1 = createDictionaryWithInnerList1();
        Dictionary dict2 = createDictionaryWithInnerList2();
        assertEquals("rating=1.5, feelings=(joy sadness)",  dict1.serialize());
        assertEquals("rating=1.5, feelings=(joy sadness)",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    private static Dictionary createDictionaryWithInnerList1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("rating", DecimalItem.valueOf(BigDecimal.valueOf(1.5f)));
        List<Item<?>> li = new ArrayList<>();
        li.add(TokenItem.valueOf("joy"));
        li.add(TokenItem.valueOf("sadness"));
        map.put("feelings", InnerList.of(li));
        return Dictionary.of(map);
    }

    private static Dictionary createDictionaryWithInnerList2() {
        return Dictionary.valueOf("rating", 1.5f,
                "feelings", InnerList.of(TokenItem.valueOf("joy"), TokenItem.valueOf("sadness")));
    }

    @Test
    public void testDictConstructionMix() {
        // RFC 9651, Section 3.2
        // Example-Dict: a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid
        Dictionary dict1 = createDictionaryMix1();
        Dictionary dict2 = createDictionaryMix2();
        assertEquals("a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid",  dict1.serialize());
        assertEquals("a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    private static Dictionary createDictionaryMix1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();

        List<Item<?>> inner1 = new ArrayList<>();
        inner1.add(IntegerItem.of(1));
        inner1.add(IntegerItem.of(2));
        InnerList linner1 = InnerList.of(inner1);

        Map<String, Object> p3 = new LinkedHashMap<>();
        p3.put("aa", TokenItem.valueOf("bb"));
        Parameters params3 = Parameters.valueOf(p3);

        List<Item<?>> inner4 = new ArrayList<>();
        inner4.add(IntegerItem.valueOf(5));
        inner4.add(IntegerItem.valueOf(6));
        InnerList linner4 = InnerList.of(inner4);

        Map<String, Object> p4 = new LinkedHashMap<>();
        p4.put("valid", BooleanItem.valueOf(true));
        Parameters params4 = Parameters.of(p4);

        map.put("a", linner1);
        map.put("b", IntegerItem.valueOf(3));
        map.put("c", IntegerItem.valueOf(4).withParams(params3));
        map.put("d", linner4.withParams(params4));

        return Dictionary.of(map);
    }

    private static Dictionary createDictionaryMix2() {
        return Dictionary.valueOf("a", InnerList.valueOf(1, 2),
                "b", 3,
                "c", IntegerItem.valueOf(4).
                        withParamValuesOf("aa", TokenItem.valueOf("bb")),
                "d", InnerList.valueOf(5, 6).
                        withParamValuesOf("valid", true));
    }
}
