package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.Collection;

import javax.json.JsonObject;

import org.apache.commons.codec.binary.Base32;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BinaryTests extends AbstractSpecificationTests {

    private TestParams p;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("binary.json");
    }

    public BinaryTests(Object x, Object y) {
        this.p = (TestParams) y;
    }

    private Item<? extends Object> parse() {
        if (p.header_type.equals("item")) {
            return Parser.parseItem(p.raw);
        } else {
            fail("unsupported header type");
            return null;
        }
    }

    @Test
    public void runTest() {
        if (p.must_fail) {
            try {
                parse();
                fail("should fail");
            } catch (IllegalArgumentException expected) {
            }
        } else {
            Item<? extends Object> number = parse();
            JsonObject expected = (JsonObject) p.expected_value;
            byte expectedBytes[] = new Base32().decode(expected.get("value").toString());
            byte actualBytes[] = ((ByteBuffer) (number.get())).array();
            assertArrayEquals(expectedBytes, actualBytes);
            assertEquals(p.canonical, number.serialize());
        }
    }
}
