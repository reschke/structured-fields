package org.greenbytes.http.sfv;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
        return OuterList.of(InnerList.valueOf("foo", "bar"),
                InnerList.valueOf("baz"),
                InnerList.valueOf("bat", "one"),
                InnerList.of());
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

    // RFC 9651, Section 3.1.2
    // Example-List: abc;a=1;b=2; cde_456, (ghi;jk=4 l);q="9";r=w

    @Test
    public void testComplexListOfParams() {
        OuterList ol1 = createComplexListOfParams1();
        OuterList ol2 = createComplexListOfParams2();
        assertEquals("abc;a=1;b=2;cde_456, (ghi;jk=4 l);q=\"9\";r=w", ol1.serialize());
        assertEquals("abc;a=1;b=2;cde_456, (ghi;jk=4 l);q=\"9\";r=w", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static OuterList createComplexListOfParams1() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("a", IntegerItem.valueOf(1));
        map1.put("b", IntegerItem.valueOf(2));
        map1.put("cde_456", BooleanItem.valueOf(true));
        TokenItem l1 = TokenItem.valueOf("abc").
                withParams(Parameters.valueOf(map1));

        List<Item<?>> lc2 = new ArrayList<>();
        TokenItem t21 = TokenItem.valueOf("ghi").
                withParams(Parameters.valueOf(
                        Collections.singletonMap("jk", IntegerItem.valueOf(4))));

        lc2.add(t21);
        lc2.add(TokenItem.valueOf("l"));

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("q", StringItem.valueOf("9"));
        map2.put("r", TokenItem.valueOf("w"));
        Parameters p2 = Parameters.valueOf(map2);
        InnerList l2 = InnerList.of(lc2).withParams(p2);

        List<ListElement<?>> value = new LinkedList<>();
        value.add(l1);
        value.add(l2);

        return OuterList.valueOf(value);
    }

    // concise API
    private static OuterList createComplexListOfParams2() {
        TokenItem l1 = TokenItem.of("abc").
                withParamValuesOf("a", 1, "b", 2, "cde_456", true);

        InnerList l2 = InnerList.valueOf(
                TokenItem.of("ghi").withParamValuesOf("jk", 4),
                TokenItem.of("l")).
                    withParamValuesOf("q", "9", "r", TokenItem.of("w"));

        List<ListElement<?>> value = new LinkedList<>();
        value.add(l1);
        value.add(l2);

        return OuterList.of(value);
    }
}
