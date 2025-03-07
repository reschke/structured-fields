package org.greenbytes.http.sfv;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserAPITests {

    @Test
    public void testItemEmpty() {
        expectParseItemException("", 0, "  ^ Empty string found when parsing Bare Item");
    }

    @Test
    public void testItemNull() {
        expectParseItemException("", 0, "  ^ Empty string found when parsing Bare Item");
    }

    @Test
    public void testItemBadString() {
        expectParseItemException("a b", 2, "  --^ (0x62) Extra characters in string parsed as Item");
    }

    @Test
    public void testBareItemWithParameters() {
        expectParseBareItemException("1; b=2", 1, "  -^ (0x3b) Extra characters in string parsed as Bare Item");
    }

    @Test
    public void testItemBadDisplayString() {
        expectParseItemException("%\"%80\"", 2, "  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2");
    }

    @Test
    public void testItemBadDisplayString2() {
        expectParseItemException("%\"%20", 5, "  -----^ Closing DQUOTE missing");
    }

    @Test
    public void testItemString() {
        Item<?> parsed =  Parser.parseItem("\"123\"; a=1");
        assertEquals(StringItem.class, parsed.getClass());
        assertEquals("123", parsed.get());
        assertEquals(";a=1", parsed.getParams().serialize());
    }

    @Test
    public void testBareItemString() {
        Item<?> parsed =  Parser.parseBareItem("\"123\"");
        assertEquals(StringItem.class, parsed.getClass());
        assertEquals("123", parsed.get());
    }

    @Test
    public void testItemDisplayString() {
        Item<?> parsed = Parser.parseItem("%\"%e2%82%ac%20rates\"");
        assertEquals(DisplayStringItem.class, parsed.getClass());
        assertEquals("\u20ac rates", parsed.get());
    }

    @Test
    public void testItemOrInnerListString() {
        Parameterizable<?> parsed =  Parser.parseItemOrInnerList("\"foobar\"");
        assertEquals(StringItem.class, parsed.getClass());
        assertEquals("foobar", parsed.get());
    }

    @Test
    public void testIntegerOrDecimalInteger() {
        NumberItem<?> parsed = Parser.parseIntegerOrDecimal("42");
        assertEquals(IntegerItem.class, parsed.getClass());
        assertEquals(42L, parsed.get());
    }

    @Test
    public void testIntegerOrDecimalDecimal() {
        NumberItem<?> parsed = Parser.parseIntegerOrDecimal("4.5");
        assertEquals(DecimalItem.class, parsed.getClass());
        assertEquals(BigDecimal.valueOf(4500, 3), parsed.get());
    }

    @Test
    public void testKey() {
        String parsed = Parser.parseKey("*a23b");
        assertEquals("*a23b", parsed);
    }

    @Test
    public void testItemOrInnerListInnerList() {
        Parameterizable<?> parsed =  Parser.parseItemOrInnerList("(1    \"x\")");
        assertEquals(InnerList.class, parsed.getClass());
        assertEquals("(1 \"x\")", parsed.serialize());
    }

    private static void expectParseItemException(String input, int position, String diagostics) {
        try {
            Parser.parseItem(input);
            fail("should trow");
        } catch (ParseException pex) {
            assertEquals(position, pex.getPosition());
            assertEquals(">>" + input + "<<\n" + diagostics + "\n", pex.getDiagnostics());
        }
    }

    private static void expectParseBareItemException(String input, int position, String diagostics) {
        try {
            Parser.parseBareItem(input);
            fail("should throw");
        } catch (ParseException pex) {
            assertEquals(position, pex.getPosition());
            assertEquals(">>" + input + "<<\n" + diagostics + "\n", pex.getDiagnostics());
        }
    }
}
