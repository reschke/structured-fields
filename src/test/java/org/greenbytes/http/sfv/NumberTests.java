package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;

import javax.json.JsonValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NumberTests extends AbstractSpecificationTests {

    private TestParams p;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("number.json");
    }

    public NumberTests(Object x, Object y) {
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
            String expected = ((((JsonValue) p.expected_value)).toString());
            assertEquals(expected, number.serialize());

            if (p.canonical != null) {
                assertEquals(p.canonical, number.serialize());
            }
        }
    }
}
