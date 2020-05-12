package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.apache.commons.codec.binary.Base32;

public abstract class AbstractSpecificationTests {

    public TestParams p;

    public static class TestParams {
        public String name;
        public String raw;
        public String header_type;
        public boolean must_fail;
        public JsonValue expected_value;
        public String canonical;
    }

    public static Collection<Object[]> makeParameters(String filename) {
        List<Object[]> result = new ArrayList<>();
        JsonReader reader = Json.createReader(BooleanTests.class.getClassLoader().getResourceAsStream(filename));
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
            p.raw = p.raw.trim();
            p.header_type = ((JsonObject) v).getString("header_type");
            p.must_fail = ((JsonObject) v).getBoolean("must_fail", false);
            JsonArray testExpected = ((JsonObject) v).getJsonArray("expected");
            p.expected_value = testExpected == null ? null : testExpected.get(0);
            p.canonical = ((JsonObject) v).getString("canonical", null);
            result.add(new Object[] { p.name, p });
        }

        return result;
    }

    public Item<? extends Object> parse() {
        if (p.header_type.equals("item")) {
            return Parser.parseItem(p.raw);
        } else if (p.header_type.equals("list")) {
            return Parser.parseList(p.raw);
        } else {
            fail("unsupported header type");
            return null;
        }
    }

    public void executeTest() {
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
                    assertEquals(num.longValueExact(), item.get());
                } else {
                    assertEquals(num.toString(), item.serialize());
                }
            } else if (p.expected_value instanceof JsonObject) {
                JsonObject container = (JsonObject) p.expected_value;
                String type = container.getString("__type");
                if ("binary".equals(type)) {
                    byte expectedBytes[] = new Base32().decode(container.get("value").toString());
                    byte actualBytes[] = ((ByteBuffer) (item.get())).array();
                    assertArrayEquals(expectedBytes, actualBytes);
                } else {
                    fail("unexpected type: " + type);
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
