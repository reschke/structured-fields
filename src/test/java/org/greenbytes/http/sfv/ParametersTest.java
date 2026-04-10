package org.greenbytes.http.sfv;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ParametersTest {

    Parameters params = Parameters.valueOf(new HashMap<>());

    // Test that all write operations fail

    @Test(expected =  UnsupportedOperationException.class)
    public void testMerge() {
        params.merge(null,  null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testPut() {
        params.put(null, null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testPutAll() {
        params.putAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutIfAbsent() {
        params.putIfAbsent(null, null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testRemove() {
        params.remove(null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testRemove2() {
        params.remove(null, null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testReplace() {
        params.replace(null, null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testReplace3() {
        params.replace(null, null, null);
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void testReplaceAll1() {
        params.replaceAll(null);
    }


    @Test
    public void testParametersEquality() {
        Map m1 = new HashMap();
        m1.put("a", BooleanItem.valueOf(true));
        Map m2 = new HashMap();
        m2.put("b", IntegerItem.valueOf(12));
        m2.put("c", StringItem.valueOf("hello"));

        Parameters p1 = Parameters.valueOf(m1);
        Parameters p2 = Parameters.valueOf(m1);
        Parameters p3 = Parameters.valueOf(m2);

        assertFalse(p1 == p2);
        assertEquals(p1, p2);

        assertFalse(p1 == p3);
        assertNotEquals(p1, p3);
    }

    @Test
    public void canRemoveParams() {
        Parameters empty = Parameters.valueOf(Collections.emptyMap());

        BooleanItem boi = Parser.parseBoolean("?1;b");
        assertEquals("?1;b", boi.serialize());
        assertEquals("?1", boi.withParams(empty).serialize());

        ByteSequenceItem byi = Parser.parseByteSequence(":cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:;b");
        assertEquals(":cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:;b", byi.serialize());
        assertEquals(":cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:", byi.withParams(empty).serialize());

        DateItem dai = Parser.parseDate("@1;b");
        assertEquals("@1;b", dai.serialize());
        assertEquals("@1", dai.withParams(empty).serialize());

        DisplayStringItem dsi = Parser.parseDisplayString("%\"This is intended for display to %c3%bcsers.\";b");
        assertEquals("%\"This is intended for display to %c3%bcsers.\";b", dsi.serialize());
        assertEquals("%\"This is intended for display to %c3%bcsers.\"", dsi.withParams(empty).serialize());

        NumberItem dei = Parser.parseIntegerOrDecimal("0.3;b");
        assertEquals("0.3;b", dei.serialize());
        assertEquals("0.3", dei.withParams(empty).serialize());

        NumberItem ini = Parser.parseIntegerOrDecimal("3;b");
        assertEquals("3;b", ini.serialize());
        assertEquals("3", ini.withParams(empty).serialize());

        StringItem si = Parser.parseString("\"a\";b");
        assertEquals("\"a\";b", si.serialize());
        assertEquals("\"a\"", si.withParams(empty).serialize());

        TokenItem ti = Parser.parseToken("a;b");
        assertEquals("a;b", ti.serialize());
        assertEquals("a", ti.withParams(empty).serialize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParamsStrictnessReParametersInValues() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("foo", BooleanItem.valueOf(false));

        Map<String, Object> map2 = new HashMap<>();
        map2.put("qux", 0);

        BooleanItem.valueOf(true).withParams(Parameters.valueOf(map2));
        map1.put("bar", BooleanItem.valueOf(true).withParams(Parameters.valueOf(map2)));

        // this needs to fail because the second parameter's value has parameters
        Parameters.valueOf(map1);
    }
}