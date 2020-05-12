package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        public JsonValue expected_params; // TODO
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
            JsonArray array = ((JsonObject) v).getJsonArray("expected");
            if (array == null) {
                p.expected_value = null;
                p.expected_params = null;
            } else if (array.size() == 2) {
                p.expected_value = array.get(0);
                p.expected_params = array.get(1);
            } else {
                p.expected_value = array;
                p.expected_params = null;
            }
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

    private static void match(JsonValue value, JsonValue params, Item<? extends Object> item) {
        if (value instanceof JsonString) {
            CharSequence expected = ((((JsonString) value)).getChars());
            assertEquals(expected, item.get());
        } else if (value instanceof JsonNumber) {
            JsonNumber num = (JsonNumber) value;
            if (num.isIntegral()) {
                assertEquals(num.longValueExact(), item.get());
            } else {
                assertEquals(num.toString(), item.serialize());
            }
        } else if (value instanceof JsonObject) {
            JsonObject container = (JsonObject) value;
            String type = container.getString("__type");
            if ("binary".equals(type)) {
                byte expectedBytes[] = new Base32().decode(container.get("value").toString());
                byte actualBytes[] = ((ByteBuffer) (item.get())).array();
                assertArrayEquals(expectedBytes, actualBytes);
            } else if ("token".equals(type)) {
                CharSequence expectedString = ((JsonString) container.get("value")).getChars();
                assertEquals(expectedString, item.get());
            } else {
                fail("unexpected type: " + type);
            }
        } else if (value instanceof JsonValue) {
            if (JsonValue.TRUE.equals(value) || JsonValue.FALSE.equals(value)) {
                Boolean expected = Boolean.valueOf(((JsonValue) value).toString());
                assertEquals(expected, item.get());
            } else {
                fail("unexpected JsonValue: " + value);
            }
        } else {
            fail("unexpected type: " + value.getClass());
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
            if (p.expected_value instanceof JsonArray) {
                // assume list for now
                JsonArray array = (JsonArray) p.expected_value;
                for (int i = 0; i < array.size(); i++) {
                    JsonValue m = array.get(i);
                    assertTrue(item instanceof ListItem);
                    assertTrue(m instanceof JsonArray);
                    match(((JsonArray) m).get(0), ((JsonArray) m).get(1), ((ListItem) item).get().get(i));
                }
            } else {
                match(p.expected_value, p.expected_params, item);
            }

            if (p.canonical != null) {
                assertEquals(p.canonical, item.serialize());
            }
        }
    }
}
