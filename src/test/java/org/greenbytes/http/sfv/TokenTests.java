package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TokenTests extends AbstractSpecificationTests {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("token.json");
    }

    public TokenTests(Object x, Object y) {
        this.p = (TestParams) y;
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
            if (item instanceof ListItem) {
                List<Item<? extends Object>> list = ((ListItem) item).get();
                JsonObject expected = (JsonObject) ((JsonArray) p.expected_value).get(0);
                CharSequence expectedString = ((JsonString) expected.get("value")).getChars();
                assertEquals(expectedString, list.get(0).get());
            } else {
                JsonObject expected = (JsonObject) p.expected_value;
                CharSequence expectedString = ((JsonString) expected.get("value")).getChars();
                assertEquals(expectedString, item.get());
            }

            if (p.canonical != null) {
                assertEquals(p.canonical, item.serialize());
            }
        }
    }
}
