package org.greenbytes.http.sfv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

public abstract class AbstractSpecificationTests {

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
            p.header_type = ((JsonObject) v).getString("header_type");
            p.must_fail = ((JsonObject) v).getBoolean("must_fail", false);
            JsonArray testExpected = ((JsonObject) v).getJsonArray("expected");
            p.expected_value = testExpected == null ? null : testExpected.get(0);
            p.canonical = ((JsonObject) v).getString("canonical", null);
            result.add(new Object[] { p.name, p });
        }

        return result;
    }
}
