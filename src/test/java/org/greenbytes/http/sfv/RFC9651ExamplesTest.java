package org.greenbytes.http.sfv;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RFC9651ExamplesTest {

    // RFC 9651, Section 3.1
    // Example-List: sugar, tea, rum

    @Test
    public void testListConstructionBareItems() {
        OuterList l1 = createListBareItems1();
        OuterList l2 = createListBareItems2();
        assertEquals("\"sugar\", \"tee\", \"rum\"", l2.serialize());
        assertEquals(l1.serialize(), l2.serialize());
        assertEquals(l1, l2);
    }

    // chatty API
    private static OuterList createListBareItems1() {
        List<ListElement<?>> list = new ArrayList<>();
        list.add(StringItem.valueOf("sugar"));
        list.add(StringItem.valueOf("tee"));
        list.add(StringItem.valueOf("rum"));

        return OuterList.of(list);
    }

    // concise API
    private static OuterList createListBareItems2() {
        return OuterList.valueOf("sugar", "tee", "rum");
    }

    // RFC 9651, Section 3.1.1
    // Example-List: ("foo" "bar"), ("baz"), ("bat" "one"), ()

    @Test
    public void testListConstructionWithInnerLists() {
        OuterList ol1 = createListBareInnerLists1();
        OuterList ol2 = createListBareInnerLists2();
        assertEquals("(\"foo\" \"bar\"), (\"baz\"), (\"bat\" \"one\"), ()", ol1.serialize());
        assertEquals("(\"foo\" \"bar\"), (\"baz\"), (\"bat\" \"one\"), ()", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static OuterList createListBareInnerLists1() {
        List<Item<?>> inner1 = new ArrayList<>();
        inner1.add(StringItem.of("foo"));
        inner1.add(StringItem.of("bar"));

        List<Item<?>> inner2 = Collections.singletonList(StringItem.of("baz"));

        List<Item<?>> inner3 = new ArrayList<>();
        inner3.add(StringItem.of("bat"));
        inner3.add(StringItem.of("one"));

        List<Item<?>> inner4 = Collections.emptyList();

        List<ListElement<?>> combined = new ArrayList<>();
        combined.add(InnerList.valueOf(inner1));
        combined.add(InnerList.valueOf(inner2));
        combined.add(InnerList.valueOf(inner3));
        combined.add(InnerList.valueOf(inner4));

        return OuterList.of(combined);
    }

    // concise API
    private static OuterList createListBareInnerLists2() {
        InnerList inner1 = InnerList.valueOf("foo", "bar");
        InnerList inner2 = InnerList.valueOf("baz");
        InnerList inner3 = InnerList.valueOf("bat", "one");
        InnerList inner4 = InnerList.of();

        return OuterList.of(inner1, inner2, inner3, inner4);
    }

    // RFC 9651, Section 3.1.1
    // Example-List: ("foo"; a=1;b=2);lvl=5, ("bar" "baz");lvl=1

    @Test
    public void testListConstructionWithInnerListsWithParams() {
        OuterList ol1 = createParametrizedInnerLists1();
        OuterList ol2 = createParametrizedInnerLists2();
        assertEquals("(\"foo\";a=1;b=2);lvl=5, (\"bar\");lvl=1", ol1.serialize());
        assertEquals("(\"foo\";a=1;b=2);lvl=5, (\"bar\");lvl=1", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static OuterList createParametrizedInnerLists1() {
        List<Item<?>> inner1 = new ArrayList<>();
        Map<String, Object> itemParam1 = new LinkedHashMap<>();
        itemParam1.put("a", 1);
        itemParam1.put("b", 2);
        inner1.add(StringItem.of("foo").withParams(Parameters.valueOf(itemParam1)));
        Map<String, Object> itemParamOuter1 = Collections.singletonMap("lvl", 5);
        InnerList linner1 = InnerList.of(inner1).withParams(Parameters.valueOf(itemParamOuter1));

        List<Item<?>> inner2 = Collections.singletonList(StringItem.of("bar"));
        Map<String, Object> itemParamOuter2 = new LinkedHashMap<>();
        itemParamOuter2.put("lvl", 1);
        InnerList linner2 = InnerList.of(inner2).withParams(Parameters.valueOf(itemParamOuter2));

        List<ListElement<?>> combined = new ArrayList<>();
        combined.add(linner1);
        combined.add(linner2);

        return OuterList.of(combined);
    }

    // concise API
    private static OuterList createParametrizedInnerLists2() {
        InnerList linner1 = InnerList.of(
                        StringItem.of("foo").withParams(Parameters.valueOf("a", 1, "b", 2)))
                .withParams(Parameters.valueOf("lvl", 5));

        InnerList linner2 = InnerList.valueOf("bar").withParams(Parameters.valueOf("lvl", 1));

        return OuterList.of(linner1, linner2);
    }
}
