package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class DiagnosticsTests {

    private class TestCase {

        final String[] input;
        final String type;
        final int position;
        final String message;

        TestCase(String[] input, String type, int position, String message) {
            this.input = input;
            this.type = type;
            this.position = position;
            this.message = message;
        }

        TestCase(String input, String type, int position, String message) {
            this(new String[] { input }, type, position, message);
        }
    }

    @Test
    public void diagnostics() {

        TestCase[] tests = new TestCase[] { new TestCase("\u0080", null, 0, "Invalid character in field line "),
                new TestCase("   \u0080", null, 3, "Invalid character in field line ") };

        for (TestCase test : tests) {
            try {
                Parser p = new Parser(test.input);
                switch (test.type) {
                    case "item":
                        p.parseItem();
                        break;
                    case "list":
                        p.parseList();
                        break;
                    case "dictionary":
                        p.parseDictionary();
                        break;
                    default:
                        fail("unknown type: " + test.type);
                }
                fail("Should not parse: " + Arrays.asList(test.input));
            } catch (ParseException ex) {
                Assert.assertTrue("does not start with '" + test.message + "' : '" + ex.getMessage() + "'",
                        ex.getMessage().startsWith(test.message));
                assertEquals(test.position, ex.getPosition());
            }
        }
    }
}
