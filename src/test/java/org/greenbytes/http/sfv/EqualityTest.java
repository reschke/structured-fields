package org.greenbytes.http.sfv;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class EqualityTest {


    @Test
    public void testParametersEquality() {
        HashMap<String, Object> m1 = new LinkedHashMap<>();
        m1.put("a", "b");
        HashMap<String, Object> m2 = new LinkedHashMap<>();
        m2.put("a", "b");
        Parameters p1 = Parameters.of(m1);
        Parameters p2 = Parameters.of(m2);
        assertNotSame(p1, p2);
        assertEquals(p1, p2);
    }

    @Test
    public void testStringItemEquality() {
        StringItem s1 = StringItem.of("a");
        StringItem s2 = StringItem.of("a");
        assertNotSame(s1, s2);
        assertEquals(s1, s2);
        StringItem s3 = StringItem.of("b");
        assertNotEquals(s1, s3);

        StringItem s4 = StringItem.of("a").withParams(getParameters("a", "b"));
        assertNotSame(s1, s4);
        assertNotEquals(s1, s4);

        HashMap<String, Object> m5 = new LinkedHashMap<>();
        m5.put("c", "d");
        Parameters p5 = Parameters.of(m5);
        StringItem s5 = StringItem.of("a").withParams(p5);
        assertNotSame(s4, s5);
        assertNotEquals(s4, s5);
    }

    @Test
    public void testTokenItemEquality() {
        TokenItem t1 = TokenItem.of("a");
        TokenItem t = TokenItem.of("a");
        assertNotSame(t1, t);
        assertEquals(t1, t);
        TokenItem t3 = TokenItem.of("b");
        assertNotEquals(t1, t3);

        TokenItem t4 = TokenItem.of("a").withParams(getParameters("a", "b"));
        assertNotSame(t1, t4);
        assertNotEquals(t1, t4);

        TokenItem t5 = TokenItem.of("a").withParams(getParameters("c", "d"));
        assertNotSame(t4, t5);
        assertNotEquals(t4, t5);
    }

    @Test
    public void testBooleanItemEquality() {
        BooleanItem b1 = BooleanItem.of(true);
        BooleanItem b2 = BooleanItem.of(true);
        BooleanItem b3 = BooleanItem.of(false);

        // FALSE and TRUE are singletons
        assertSame(b1, b2);
        assertEquals(b1, b2);

        assertNotSame(b1, b3);
        assertNotEquals(b1, b3);

        BooleanItem b4 = BooleanItem.of(false).withParams(getParameters("c", "d"));
        assertNotSame(b1, b4);
        assertNotEquals(b1, b4);
    }

    @Test
    public void testByteSequenceItemEquality() {
        ByteSequenceItem b1 = ByteSequenceItem.valueOf("x".getBytes());
        ByteSequenceItem b2 = ByteSequenceItem.valueOf("x".getBytes());
        ByteSequenceItem b3 = ByteSequenceItem.valueOf("y".getBytes());

        assertNotSame(b1, b2);
        assertEquals(b1, b2);

        assertNotSame(b1, b3);
        assertNotEquals(b1, b3);

        ByteSequenceItem b4 = b1.withParams(getParameters("c", "d"));
        assertNotSame(b1, b4);
        assertNotEquals(b1, b4);
    }

    @Test
    public void testDateItemEquality() {
        DateItem d1 = DateItem.valueOf(0);
        DateItem d2 = DateItem.valueOf(0);
        DateItem d3 = DateItem.valueOf(1000);

        assertNotSame(d1, d2);
        assertEquals(d1, d2);

        assertNotSame(d1, d3);
        assertNotEquals(d1, d3);

        DateItem d4 = d1.withParams(getParameters("c", "d"));
        assertNotSame(d1, d4);
        assertNotEquals(d1, d4);
    }

    @Test
    public void testDecimalItemEquality() {
        DecimalItem d1 = DecimalItem.valueOf(BigDecimal.valueOf(10.5));
        DecimalItem d2 = DecimalItem.valueOf(BigDecimal.valueOf(10.5));
        DecimalItem d3 = DecimalItem.valueOf(BigDecimal.valueOf(10.6));

        assertNotSame(d1, d2);
        assertEquals(d1, d2);

        assertNotSame(d1, d3);
        assertNotEquals(d1, d3);

        DecimalItem d4 = d1.withParams(getParameters("c", "d"));
        assertNotSame(d1, d4);
        assertNotEquals(d1, d4);
    }

    @Test
    public void testDisplayStringItemEquality() {
        DisplayStringItem d1 = DisplayStringItem.valueOf("abc");
        DisplayStringItem d2 = DisplayStringItem.valueOf("abc");
        DisplayStringItem d3 = DisplayStringItem.valueOf("def");

        assertNotSame(d1, d2);
        assertEquals(d1, d2);

        assertNotSame(d1, d3);
        assertNotEquals(d1, d3);

        DisplayStringItem d4 = d1.withParams(getParameters("c", "d"));
        assertNotSame(d1, d4);
        assertNotEquals(d1, d4);
    }

    @Test
    public void testIntegerItemEquality() {
        IntegerItem i1 = IntegerItem.of(1);
        IntegerItem i2 = IntegerItem.of(1);
        IntegerItem i3 = IntegerItem.of(2);

        assertNotSame(i1, i2);
        assertEquals(i1, i2);

        assertNotSame(i1, i3);
        assertNotEquals(i1, i3);

        IntegerItem d4 = i1.withParams(getParameters("a", "b"));
        assertNotSame(i1, d4);
        assertNotEquals(i1, d4);
    }

    @Test
    public void testOuterListEquality() {
        BooleanItem b = BooleanItem.of(true);
        DecimalItem d = DecimalItem.valueOf(BigDecimal.valueOf(10.5));

        OuterList l1 = OuterList.valueOf((Arrays.asList(b, d)));
        OuterList l2 = OuterList.valueOf((Arrays.asList(b, d)));
        OuterList l3 = OuterList.valueOf((Arrays.asList(b, d, b)));

        assertNotSame(l1, l2);
        assertEquals(l1, l2);

        assertNotSame(l1, l3);
        assertNotEquals(l1, l3);
    }

    private static Parameters getParameters(String a, String b) {
        HashMap<String, Object> m = new LinkedHashMap<>();
        m.put(a, b);
        return Parameters.of(m);
    }
}
