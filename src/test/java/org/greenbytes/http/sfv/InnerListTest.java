package org.greenbytes.http.sfv;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertNotEquals;

public class InnerListTest extends TestCase {

    @Test
    public void testOuterListEquality() {
        BooleanItem b = BooleanItem.valueOf(true);
        DecimalItem d = DecimalItem.valueOf(BigDecimal.valueOf(10.5));

        InnerList l1 = InnerList.valueOf((Arrays.asList(b, d)));
        InnerList l2 = InnerList.valueOf((Arrays.asList(b, d)));
        InnerList l3 = InnerList.valueOf((Arrays.asList(b, d, b)));

        assertFalse(l1 == l2);
        assertEquals(l1, l2);

        assertFalse(l1 == l3);
        assertNotEquals(l1, l3);
    }
}