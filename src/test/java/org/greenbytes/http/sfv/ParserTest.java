package org.greenbytes.http.sfv;

import junit.framework.TestCase;
import org.junit.Test;

public class ParserTest extends TestCase {

    @Test
    public void testParserOnlyWorksOnce() {
        Parser parser = new Parser("a");
        // parse once: pass
        parser.parseItem();
        // parse twice: fail
        try {
            parser.parseItem();
            fail("expect IllegalStateException");
        } catch (IllegalStateException ex) {
            // all good
        }
    }
}