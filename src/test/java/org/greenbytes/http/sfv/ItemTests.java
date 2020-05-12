package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Collection;

import javax.json.JsonNumber;
import javax.json.JsonString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ItemTests extends AbstractSpecificationTests {

    private TestParams p;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("item.json");
    }

    public ItemTests(Object x, Object y) {
        this.p = (TestParams) y;
    }

    private Item<? extends Object> parse() {
        if (p.header_type.equals("item")) {
            return Parser.parseItem(p.raw);
        } else if (p.header_type.equals("list")) {
            return Parser.parseList(p.raw);
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
            Item<? extends Object> item = parse();
            if (p.expected_value instanceof JsonString) {
                CharSequence expected = ((((JsonString) p.expected_value)).getChars());
                assertEquals(expected, item.get());
            } else if (p.expected_value instanceof JsonNumber) {
                JsonNumber num = (JsonNumber) p.expected_value;
                if (num.isIntegral()) {
                    long expected = num.longValueExact();
                    assertEquals(expected, item.get());
                } else {
                    BigDecimal expected = num.bigDecimalValue();
                    assertEquals(expected, item.get());
                }
            } else {
                fail("unexpected type: " + p.expected_value.getClass());
            }

            if (p.canonical != null) {
                assertEquals(p.canonical, item.serialize());
            }
        }
    }
}
