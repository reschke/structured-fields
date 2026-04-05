package org.greenbytes.http.sfv;

import org.junit.Test;

import java.util.HashMap;

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
}