package org.greenbytes.http.sfv;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class NoVaryDraftTests {

    @Test
    public void testSimpleKeyOrder() {
        NoVary test = new NoVary(true, null, null);
        assertEquals("key-order", test.serialize());
    }

    @Test
    public void testEmpty() {
        NoVary test = new NoVary(false, null, null);
        assertEquals("", test.serialize());
    }

    static class NoVary {

        boolean varyOnKeyOrder;
        List<String> noVary;
        List<String> vary;

        public NoVary(boolean varyOnKeyOrder, List<String> noVary, List<String> vary) {
            this.varyOnKeyOrder = varyOnKeyOrder;
            this.noVary = noVary;
            this.vary = vary;
        }

        public String serialize() {
            Map<String, ListElement<?>> dict = new LinkedHashMap<>();
            if (varyOnKeyOrder) {
                dict.put("key-order", BooleanItem.of(true));
            }
            return Dictionary.of(dict).serialize();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NoVary)) {
                return false;
            } else {
                NoVary noVary1 = (NoVary) o;
                return varyOnKeyOrder == noVary1.varyOnKeyOrder && Objects.equals(noVary, noVary1.noVary) && Objects.equals(vary, noVary1.vary);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(varyOnKeyOrder, noVary, vary);
        }
    }
}
