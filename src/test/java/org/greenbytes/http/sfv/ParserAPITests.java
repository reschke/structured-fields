package org.greenbytes.http.sfv;

import org.junit.Test;

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
    public void testItemBadDisplayString() {
        expectParseItemException("%\"%80\"", 2, "  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2");
    }

    @Test
    public void testItemBadDisplayString2() {
        expectParseItemException("%\"%20", 5, "  -----^ Closing DQUOTE missing");
    }

    @Test
    public void testItemString() {
        Item<?> parsed =  Parser.parseItem("\"123\"");
        assertEquals(StringItem.class, parsed.getClass());
        assertEquals("123", parsed.get());
    }

    @Test
    public void testItemDisplayString() {
        Item<?> parsed = Parser.parseItem("%\"%e2%82%ac%20rates\"");
        assertEquals(DisplayStringItem.class, parsed.getClass());
        assertEquals("\u20ac rates", parsed.get());
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
}
