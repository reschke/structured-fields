package org.greenbytes.http.sfv;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
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
}