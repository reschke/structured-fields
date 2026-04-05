package org.greenbytes.http.sfv;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class CLITest extends TestCase {

    @Test
    public void testCLIListAndDict() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(os);
        int ret = CLI.cli(out, "foo", "bar");
        assertEquals(0, ret);
        String output = os.toString();
        // parse failure
        assertTrue(output.contains("Item: >>foo,bar<<"));
        // parse success
        assertTrue(output.contains("List: foo, bar (OuterList)"));
        assertTrue(output.contains("Dict: foo, bar (Dictionary)"));
    }

    @Test
    public void testCLILineBoundary() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(os);
        int ret = CLI.cli(out, "\"foo", "bar\"");
        assertEquals(1, ret);
        String output = os.toString();
        // parse failure
        assertTrue(output.contains("String crosses field line boundary"));
    }

    @Test
    public void testCLIGoodStringItem() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(os);
        int ret = CLI.cli(out, "\"foo bar\"");
        assertEquals(0, ret);
        String output = os.toString();
        // parse failure
        assertTrue(output.contains("Item: \"foo bar\" (StringItem)"));
    }
}