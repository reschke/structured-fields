package org.greenbytes.http.sfv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserAPITests {

    @Test
    public void testItemEmpty() {
        expectParseException("", 0, "  ^ Empty string found when parsing Bare Item");
    }

    @Test
    public void testItemNull() {
        expectParseException("", 0, "  ^ Empty string found when parsing Bare Item");
    }

    @Test
    public void testBadString() {
        expectParseException("a b", 2, "  --^ (0x62) Extra characters in string parsed as Item");
    }

    private static void expectParseException(String input, int position, String diagostics) {
        try {
            Parser.parseItem(input);
            fail("should trow");
        } catch (ParseException pex) {
            assertEquals(position, pex.getPosition());
            assertEquals(">>" + input + "<<\n" + diagostics + "\n", pex.getDiagnostics());
        }
    }
}
