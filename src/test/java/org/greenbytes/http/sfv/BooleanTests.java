package org.greenbytes.http.sfv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BooleanTests {

    private static class TestParams {
        public String name;
        public String raw;
        public String header_type;
        public boolean must_fail;
        public Boolean expected_value;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        List<Object[]> result = new ArrayList<>();
        JsonReader reader = Json.createReader(BooleanTests.class.getClassLoader().getResourceAsStream("boolean.json"));
        for (JsonValue v : reader.readArray()) {
            TestParams p = new TestParams();
            p.name = ((JsonObject) v).getString("name");
            p.raw = "";
            for (JsonValue raw : ((JsonObject) v).getJsonArray("raw")) {
                if (p.raw.length() != 0) {
                    p.raw += ",";
                }
                p.raw += ((JsonString) raw).getString();
            }
            p.header_type = ((JsonObject) v).getString("header_type");
            p.must_fail = ((JsonObject) v).getBoolean("must_fail", false);
            JsonArray testExpected = ((JsonObject) v).getJsonArray("expected");
            p.expected_value = testExpected == null ? null : testExpected.getBoolean(0);
            result.add(new Object[] { p.name, p });
        }

        return result;
    }

    private TestParams p;

    public BooleanTests(Object x, Object y) {
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
            Item<? extends Object> b = parse();
            assertEquals(p.expected_value, b.get());
        }
    }
}
