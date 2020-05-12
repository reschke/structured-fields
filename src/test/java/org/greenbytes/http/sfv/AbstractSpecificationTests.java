package org.greenbytes.http.sfv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public static Collection<Object[]> makeParameters(List<String> filenames) {
        List<Object[]> result = new ArrayList<>();
        for (String filename : filenames) {
            result.addAll(makeParameters(filename));
        }
        return result;
    }

    private static Collection<Object[]> makeParameters(String filename) {
        List<Object[]> result = new ArrayList<>();
        JsonReader reader = Json.createReader(AbstractSpecificationTests.class.getClassLoader().getResourceAsStream(filename));
        for (JsonValue vt : reader.readArray()) {
            JsonObject v = (JsonObject) vt;
            TestParams p = new TestParams();
            p.name = ((JsonObject) v).getString("name");
            p.raw = "";
            for (JsonValue raw : v.getJsonArray("raw")) {
                String t = ((JsonString) raw).getString();
                while (t.startsWith(" ")) {
                    t = t.substring(1);
                }
                while (t.length() > 1 && t.lastIndexOf(' ') == t.length() -1) {
                    t = t.substring(0, t.length() - 1);
                }
                if (p.raw.length() != 0) {
                    p.raw += ",";
                }
                p.raw += t;
            }
            p.header_type = v.getString("header_type");
            p.must_fail = v.getBoolean("must_fail", false);
            p.expected_params = null;
            if (v.get("expected") instanceof JsonArray) {
                JsonArray array = v.getJsonArray("expected");
                if (array == null) {
                    p.expected_value = null;
                } else if (array.size() == 2 && "item".equals(p.header_type)) {
                    p.expected_value = array.get(0);
                    p.expected_params = array.get(1);
                } else {
                    p.expected_value = array;
                }
            } else {
                p.expected_value = v.getJsonObject("expected");
            }
            p.canonical = v.getString("canonical", null);
            String basename = filename.substring(0, filename.length() - ".json".length());
            result.add(new Object[] { basename + ": " + p.name, p });
        }

        return result;
    }

    public Item<? extends Object> parse() {
        if (p.header_type.equals("item")) {
            return Parser.parseItem(p.raw);
        } else if (p.header_type.equals("list")) {
            return Parser.parseList(p.raw);
        } else if (p.header_type.equals("dictionary")) {
            return Parser.parseDictionary(p.raw);
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
            if (container.containsKey("__type")) {
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
            } else {
                Map<String, Item<? extends Object>> result = (Map<String, Item<? extends Object>>) item.get();
                assertEquals(container.size(), result.size());
                for (Map.Entry<String, JsonValue> e : container.entrySet()) {
                    if (e.getValue() instanceof JsonArray) {
                        JsonArray array = (JsonArray) e.getValue();
                        assertEquals(2, array.size());
                        match(array.get(0), array.get(1), result.get(e.getKey()));
                    } else {
                        match(e.getValue(), null, result.get(e.getKey()));
                    }
                }
            }
        } else if (value instanceof JsonArray) {
            JsonArray array = (JsonArray) value;
            List<Item<? extends Object>> result = (List<Item<? extends Object>>) item.get();
            assertEquals(array.size(), result.size());
            for (int i = 0; i < array.size(); i++) {
                JsonArray t = (JsonArray) array.get(i);
                assertEquals(2, t.size());
                match(t.get(0), t.get(1), result.get(i));
            }
        } else if (value instanceof JsonValue) {
            if (JsonValue.TRUE.equals(value) || JsonValue.FALSE.equals(value)) {
                Boolean expected = Boolean.valueOf(((JsonValue) value).toString());
                assertEquals(expected, item.get());
            } else {
                fail("unexpected JsonValue: " + value + " (" + value.getClass() + ")");
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
                assertTrue(item instanceof ListItem);
                JsonArray array = (JsonArray) p.expected_value;
                for (int i = 0; i < array.size(); i++) {
                    JsonValue m = array.get(i);
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
