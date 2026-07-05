package org.greenbytes.http.sfv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void testConversionFromStringWhichIsNotAValidSFString() {
        // sanity check
        Item simple = Utils.asBareItem("foobar");
        assertEquals(StringItem.class, simple.getClass());

        Item notSimple = Utils.asBareItem("qux: \u83D0\uDCA9");
        assertEquals(DisplayStringItem.class, notSimple.getClass());
    }

    // TODO: when constructing Items, need to check validity of arguments
    // Test below should fail, for instance

    @Test
    public void testConversionFromStringWhichHasUnpairedSurrogates() {
        Item what = Utils.asBareItem("qux: \u83D0\uDCA9 what?");
        assertEquals(DisplayStringItem.class, what.getClass());
    }
}