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
        public String filename;
        public String name;
        public List<String> raw;
        public String header_type;
        public boolean must_fail;
        public JsonValue expected_value;
        public JsonValue expected_params;
        public String canonical;

        // provides the test name to Junit runner
        @Override public String toString() {
            return filename + ":" + name;
        }
    }

    public static Collection<Object[]> makeParameters(String... filenames) {
        List<Object[]> result = new ArrayList<>();
        for (String filename : filenames) {
            result.addAll(internalMakeParameters(filename));
        }
        return result;
    }

    private static Collection<Object[]> internalMakeParameters(String filename) {
        String basename = filename.substring(0, filename.length() - ".json".length());
        List<Object[]> result = new ArrayList<>();

        JsonReader reader = Json.createReader(AbstractSpecificationTests.class.getClassLoader().getResourceAsStream(filename));
        for (JsonValue vt : reader.readArray()) {
            TestParams p = makeOneTest(basename, vt);
            result.add(new Object[] { p });
        }

        return result;
    }

    private static TestParams makeOneTest(String basename, JsonValue vt) {
        JsonObject v = (JsonObject) vt;
        TestParams p = new TestParams();

        p.filename = basename;
        p.name = v.getString("name");
        p.raw = new ArrayList<>();
        for (JsonValue raw : v.getJsonArray("raw")) {
            String t = ((JsonString) raw).getString();
            p.raw.add(t);
        }
        p.header_type = v.getString("header_type");
        p.must_fail = v.getBoolean("must_fail", false);

        JsonValue expected = v.get("expected");

        if (expected == null) {
            p.expected_value = null;
            p.expected_params = null;
        } else if ("item".equals(p.header_type)) {
            if (expected instanceof JsonArray) {
                JsonArray array = (JsonArray) expected;
                p.expected_value = array.get(0);
                p.expected_params = array.get(1);
            } else {
                p.expected_value = expected;
                p.expected_params = null;
            }
        } else if ("dictionary".equals(p.header_type)) {
            if (expected instanceof JsonArray) {
                p.expected_value = expected;
            } else {
                throw new RuntimeException("unexpected dictionary expected value for test: " + p.name);
            }
        } else if ("list".equals(p.header_type)) {
            p.expected_value = v.getJsonArray("expected");
            p.expected_params = null;
        } else {
            throw new RuntimeException("unexpected header_type: " + p.header_type);
        }
        JsonArray canarr = v.getJsonArray("canonical");
        p.canonical = canarr == null || canarr.isEmpty() ? null : canarr.getString(0);
        return p;
    }

    public Type<?> parse() {
        Parser parser = new Parser(p.raw);
        switch (p.header_type) {
            case "item":
                return parser.parseItem();
            case "list":
                return parser.parseList();
            case "dictionary":
                return parser.parseDictionary();
            default:
                fail("unsupported header type");
                return null;
        }
    }

    private static void match(StringBuilder out, JsonValue value, JsonValue params, Type<?> item) {
        try {
            internalMatch(out, value, params, item);
        }
        catch (AssertionError ae) {
            out.append("*FAIL*:\n");
            out.append("~~~\n");
            out.append(ae.getMessage()).append("\n");
            out.append("~~~\n");
            throw ae;
        }
    }

    private static void internalMatch(StringBuilder out, JsonValue value, JsonValue params, Type<?> item) {
        if (value instanceof JsonString) {
            CharSequence expected = (((JsonString) value).getChars());
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
                    byte[] expectedBytes = new Base32().decode(container.get("value").toString());
                    byte[] actualBytes = ((ByteBuffer) (item.get())).array();
                    assertArrayEquals(expectedBytes, actualBytes);
                } else if ("token".equals(type)) {
                    CharSequence expectedString = ((JsonString) container.get("value")).getChars();
                    assertEquals(expectedString, item.get());
                } else if ("date".equals(type)) {
                    JsonNumber expectedNumber = (JsonNumber) container.get("value");
                    assertEquals(expectedNumber.longValueExact(), item.get());
                } else if ("displaystring".equals(type)) {
                    CharSequence expectedString = ((JsonString) container.get("value")).getChars();
                    assertEquals(expectedString, item.get());
                } else {
                    fail("unexpected type: " + type);
                }
            } else {
                @SuppressWarnings("unchecked")
                Map<String, Item<?>> result = (Map<String, Item<?>>) item.get();
                assertEquals(container.size(), result.size());
                for (Map.Entry<String, JsonValue> e : container.entrySet()) {
                    if (e.getValue() instanceof JsonArray) {
                        JsonArray array = (JsonArray) e.getValue();
                        assertEquals(2, array.size());
                        match(out, array.get(0), array.get(1), result.get(e.getKey()));
                    } else {
                        match(out, e.getValue(), null, result.get(e.getKey()));
                    }
                }
            }
        } else if (value instanceof JsonArray) {
            JsonArray array = (JsonArray) value;
            @SuppressWarnings("unchecked")
            List<Item<?>> result = (List<Item<?>>) item.get();
            assertEquals(array.size(), result.size());
            for (int i = 0; i < array.size(); i++) {
                JsonArray t = (JsonArray) array.get(i);
                assertEquals(2, t.size());
                match(out, t.get(0), t.get(1), result.get(i));
            }
        } else if (value != null) {
            if (JsonValue.TRUE.equals(value) || JsonValue.FALSE.equals(value)) {
                Boolean expected = Boolean.valueOf(value.toString());
                assertEquals(expected, item.get());
            } else {
                fail("unexpected JsonValue: " + value + " (" + value.getClass() + ")");
            }
        } else {
            fail("unexpected type");
        }
        if (params != null) {
            assertTrue(item instanceof Parameterizable);
            Map<String, Item<?>> result = ((Parameterizable<?>) item).getParams();

            if (params instanceof JsonArray) {
                // new format
                JsonArray expected = (JsonArray) params;
                assertEquals(expected.size(), result.size());
                int i = 0;
                for (Map.Entry<String, Item<?>> param : result.entrySet()) {
                    JsonArray e = (JsonArray) expected.get(i);
                    assertEquals(2, e.size());
                    assertEquals(((JsonString) e.get(0)).getString(), param.getKey());
                    match(out, e.get(1), null, param.getValue());
                    i += 1;
                }
            } else {
                fail("unexpected param type: " + params);
            }
        }
    }

    public void executeTest(StringBuilder out) {
        if (p.must_fail) {
            out.append("Expects Parse Error\n");
            try {
                Type<?> parsed = parse();
                out.append("*FAIL*, got:\n");
                out.append("~~~\n");
                out.append(parsed.serialize()).append("\n");
                out.append("~~~\n");
                fail("should fail, but passed. Input >>>" + p.raw + "<<<, Output >>>" + parsed.serialize() + "<<<");
            } catch (ParseException expected) {
                out.append("~~~\n");
                out.append(expected.getDiagnostics());
                out.append("~~~\n");
                out.append("\n");
            }
        } else {
            Type<?> item = parse();
            out.append("Result:\n");
            out.append("~~~\n");
            out.append(item.serialize()).append("\n");
            out.append("~~~\n");
            if (p.expected_value instanceof JsonArray) {
                JsonArray array = (JsonArray) p.expected_value;
                if (item instanceof OuterList) {
                    for (int i = 0; i < array.size(); i++) {
                        JsonValue m = array.get(i);
                        match(out, ((JsonArray) m).get(0), ((JsonArray) m).get(1), ((OuterList) item).get().get(i));
                    }
                } else if (item instanceof Dictionary) {
                    int i = 0;
                    for (Map.Entry<String, ListElement<?>> e : ((Dictionary) item).get().entrySet()) {
                        JsonArray m = (JsonArray) array.get(i);
                        JsonValue name = m.get(0);
                        JsonArray val = (JsonArray) m.get(1);
                        assertEquals(((JsonString) name).getString(), e.getKey());
                        match(out, val.get(0), val.get(1), e.getValue());
                        i += 1;
                    }
                } else {
                    fail("unexpected parse result: " + item.getClass());
                }
            } else {
                match(out, p.expected_value, p.expected_params, item);
            }

            if (p.canonical != null) {
                assertEquals(p.canonical, item.serialize());
            } else {
                assertEquals("in absence of 'canonical', expect only one 'raw' value", 1, p.raw.size());
                assertEquals(p.raw.get(0), item.serialize());
            }
        }
    }
}
