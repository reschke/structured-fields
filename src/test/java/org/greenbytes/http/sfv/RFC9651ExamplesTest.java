package org.greenbytes.http.sfv;

import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class RFC9651ExamplesTest {

    // RFC 9651, Section 3.1
    // Example-List: sugar, tea, rum

    @Test
    public void testListConstructionBareItems() {
        SfList l1 = createListBareItems1();
        SfList l2 = createListBareItems2();
        assertEquals("\"sugar\", \"tee\", \"rum\"", l2.serialize());
        assertEquals(l1.serialize(), l2.serialize());
        assertEquals(l1, l2);
    }

    // chatty API
    private static SfList createListBareItems1() {
        List<ListElement<?>> list = new ArrayList<>();
        list.add(StringItem.of("sugar"));
        list.add(StringItem.of("tee"));
        list.add(StringItem.of("rum"));

        return SfList.of(list);
    }

    // concise API
    private static SfList createListBareItems2() {
        return SfList.valueOf("sugar", "tee", "rum");
    }

    // RFC 9651, Section 3.1.1
    // Example-List: ("foo" "bar"), ("baz"), ("bat" "one"), ()

    @Test
    public void testListConstructionWithInnerLists() {
        SfList ol1 = createListBareInnerLists1();
        SfList ol2 = createListBareInnerLists2();
        assertEquals("(\"foo\" \"bar\"), (\"baz\"), (\"bat\" \"one\"), ()", ol1.serialize());
        assertEquals("(\"foo\" \"bar\"), (\"baz\"), (\"bat\" \"one\"), ()", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static SfList createListBareInnerLists1() {
        List<Item<?>> inner1 = new ArrayList<>();
        inner1.add(StringItem.of("foo"));
        inner1.add(StringItem.of("bar"));

        List<Item<?>> inner2 = Collections.singletonList(StringItem.of("baz"));

        List<Item<?>> inner3 = new ArrayList<>();
        inner3.add(StringItem.of("bat"));
        inner3.add(StringItem.of("one"));

        List<Item<?>> inner4 = Collections.emptyList();

        List<ListElement<?>> combined = new ArrayList<>();
        combined.add(InnerList.of(inner1));
        combined.add(InnerList.of(inner2));
        combined.add(InnerList.of(inner3));
        combined.add(InnerList.of(inner4));

        return SfList.of(combined);
    }

    // concise API
    private static SfList createListBareInnerLists2() {
        return SfList.of(InnerList.valueOf("foo", "bar"),
                InnerList.valueOf("baz"),
                InnerList.valueOf("bat", "one"),
                InnerList.of());
    }

    // RFC 9651, Section 3.1.1
    // Example-List: ("foo"; a=1;b=2);lvl=5, ("bar" "baz");lvl=1

    @Test
    public void testListConstructionWithInnerListsWithParams() {
        SfList ol1 = createParametrizedInnerLists1();
        SfList ol2 = createParametrizedInnerLists2();
        assertEquals("(\"foo\";a=1;b=2);lvl=5, (\"bar\");lvl=1", ol1.serialize());
        assertEquals("(\"foo\";a=1;b=2);lvl=5, (\"bar\");lvl=1", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static SfList createParametrizedInnerLists1() {
        List<Item<?>> inner1 = new ArrayList<>();
        Map<String, Object> itemParam1 = new LinkedHashMap<>();
        itemParam1.put("a", 1);
        itemParam1.put("b", 2);
        inner1.add(StringItem.of("foo").withParams(Parameters.of(itemParam1)));
        Map<String, Object> itemParamOuter1 = Collections.singletonMap("lvl", 5);
        InnerList linner1 = InnerList.of(inner1).withParams(Parameters.of(itemParamOuter1));

        List<Item<?>> inner2 = Collections.singletonList(StringItem.of("bar"));
        Map<String, Object> itemParamOuter2 = new LinkedHashMap<>();
        itemParamOuter2.put("lvl", 1);
        InnerList linner2 = InnerList.of(inner2).withParams(Parameters.of(itemParamOuter2));

        List<ListElement<?>> combined = new ArrayList<>();
        combined.add(linner1);
        combined.add(linner2);

        return SfList.of(combined);
    }

    // concise API
    private static SfList createParametrizedInnerLists2() {
        InnerList linner1 = InnerList.of(
                        StringItem.of("foo").withParams(Parameters.valueOf("a", 1, "b", 2)))
                .withParams(Parameters.valueOf("lvl", 5));

        InnerList linner2 = InnerList.valueOf("bar").withParams(Parameters.valueOf("lvl", 1));

        return SfList.of(linner1, linner2);
    }

    // RFC 9651, Section 3.1.2
    // Example-List: abc;a=1;b=2; cde_456, (ghi;jk=4 l);q="9";r=w

    @Test
    public void testComplexListOfParams() {
        SfList ol1 = createComplexListOfParams1();
        SfList ol2 = createComplexListOfParams2();
        assertEquals("abc;a=1;b=2;cde_456, (ghi;jk=4 l);q=\"9\";r=w", ol1.serialize());
        assertEquals("abc;a=1;b=2;cde_456, (ghi;jk=4 l);q=\"9\";r=w", ol2.serialize());
        assertEquals(ol1, ol2);
    }

    // chatty API
    private static SfList createComplexListOfParams1() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("a", IntegerItem.of(1));
        map1.put("b", IntegerItem.of(2));
        map1.put("cde_456", BooleanItem.of(true));
        TokenItem l1 = TokenItem.of("abc").
                withParams(Parameters.of(map1));

        List<Item<?>> lc2 = new ArrayList<>();
        TokenItem t21 = TokenItem.of("ghi").
                withParams(Parameters.of(
                        Collections.singletonMap("jk", IntegerItem.of(4))));

        lc2.add(t21);
        lc2.add(TokenItem.of("l"));

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("q", StringItem.of("9"));
        map2.put("r", TokenItem.of("w"));
        Parameters p2 = Parameters.of(map2);
        InnerList l2 = InnerList.of(lc2).withParams(p2);

        List<ListElement<?>> value = new LinkedList<>();
        value.add(l1);
        value.add(l2);

        return SfList.of(value);
    }

    // concise API
    private static SfList createComplexListOfParams2() {
        TokenItem l1 = TokenItem.of("abc").
                withParamValuesOf("a", 1, "b", 2, "cde_456", true);

        InnerList l2 = InnerList.valueOf(
                TokenItem.of("ghi").withParamValuesOf("jk", 4),
                TokenItem.of("l")).
                    withParamValuesOf("q", "9", "r", TokenItem.of("w"));

        return SfList.valueOf(l1, l2);
    }

    // RFC 9651, Section 3.2
    // Example-Dict: en="Applepie", da=:w4ZibGV0w6ZydGU=:

    @Test
    public void testDictConstructionSimple() {
        Dictionary dict1 = createDictionarySimple1();
        Dictionary dict2 = createDictionarySimple2();
        assertEquals("en=\"Applepie\", da=:w4ZibGV0w6ZydGU=:", dict1.serialize());
        assertEquals("en=\"Applepie\", da=:w4ZibGV0w6ZydGU=:", dict2.serialize());
        assertEquals(dict1, dict2);
    }

    // chatty API
    private static Dictionary createDictionarySimple1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("en", StringItem.of("Applepie"));
        map.put("da", ByteSequenceItem.valueOf("Æbletærte".getBytes(StandardCharsets.UTF_8)));
        return Dictionary.of(map);
    }

    // concise API
    private static Dictionary  createDictionarySimple2() {
        return Dictionary.valueOf(
                "en", "Applepie",
                "da", "Æbletærte".getBytes(StandardCharsets.UTF_8));
    }

    // RFC 9651, Section 3.2
    // Example-Dict: a=?0, b, c; foo=bar

    @Test
    public void testDictConstruction() {
        Dictionary dict1 = createDictionary1();
        Dictionary dict2 = createDictionary2();
        assertEquals("a=?0, b, c;foo=bar",  dict1.serialize());
        assertEquals("a=?0, b, c;foo=bar",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    // chatty API
    private static Dictionary createDictionary1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("a", BooleanItem.of(false));
        map.put("b", BooleanItem.of(true));
        map.put("c", BooleanItem.of(true).withParams(Parameters.of(Collections.singletonMap("foo", TokenItem.of("bar")))));
        return Dictionary.of(map);
    }

    // concise API
    private static Dictionary createDictionary2() {
        return Dictionary.valueOf(
                "a", false,
                "b", true,
                "c", BooleanItem.of(true).withParamValuesOf("foo", TokenItem.of("bar")));
    }

    // RFC 9651, Section 3.2
    // Example-Dict: rating=1.5, feelings=(joy sadness)

    @Test
    public void testDictConstructionWithInnerList() {
        Dictionary dict1 = createDictionaryWithInnerList1();
        Dictionary dict2 = createDictionaryWithInnerList2();
        assertEquals("rating=1.5, feelings=(joy sadness)",  dict1.serialize());
        assertEquals("rating=1.5, feelings=(joy sadness)",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    // chatty API
    private static Dictionary createDictionaryWithInnerList1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();
        map.put("rating", DecimalItem.valueOf(BigDecimal.valueOf(1.5f)));
        List<Item<?>> li = new ArrayList<>();
        li.add(TokenItem.of("joy"));
        li.add(TokenItem.of("sadness"));
        map.put("feelings", InnerList.of(li));
        return Dictionary.of(map);
    }

    // concise API
    private static Dictionary createDictionaryWithInnerList2() {
        return Dictionary.valueOf("rating", 1.5f,
                "feelings", InnerList.of(TokenItem.of("joy"), TokenItem.of("sadness")));
    }

    // RFC 9651, Section 3.2
    // Example-Dict: a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid

    @Test
    public void testDictConstructionMix() {
        Dictionary dict1 = createDictionaryMix1();
        Dictionary dict2 = createDictionaryMix2();
        assertEquals("a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid",  dict1.serialize());
        assertEquals("a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid",  dict2.serialize());
        assertEquals(dict1, dict2);
    }

    // chatty API
    private static Dictionary createDictionaryMix1() {
        Map<String, ListElement<?>> map = new LinkedHashMap<>();

        List<Item<?>> inner1 = new ArrayList<>();
        inner1.add(IntegerItem.of(1));
        inner1.add(IntegerItem.of(2));
        InnerList linner1 = InnerList.of(inner1);

        Map<String, Object> p3 = new LinkedHashMap<>();
        p3.put("aa", TokenItem.of("bb"));
        Parameters params3 = Parameters.of(p3);

        List<Item<?>> inner4 = new ArrayList<>();
        inner4.add(IntegerItem.of(5));
        inner4.add(IntegerItem.of(6));
        InnerList linner4 = InnerList.of(inner4);

        Map<String, Object> p4 = new LinkedHashMap<>();
        p4.put("valid", BooleanItem.of(true));
        Parameters params4 = Parameters.of(p4);

        map.put("a", linner1);
        map.put("b", IntegerItem.of(3));
        map.put("c", IntegerItem.of(4).withParams(params3));
        map.put("d", linner4.withParams(params4));

        return Dictionary.of(map);
    }

    // concise API
    private static Dictionary createDictionaryMix2() {
        return Dictionary.valueOf("a", InnerList.valueOf(1, 2),
                "b", 3,
                "c", IntegerItem.of(4).
                        withParamValuesOf("aa", TokenItem.of("bb")),
                "d", InnerList.valueOf(5, 6).
                        withParamValuesOf("valid", true));
    }

    // RFC 9651, Section 3.2
    // Foo-Example: 2; foourl="https://foo.example.com/"
    // (parse and validate)

    @Test
    public void testParseAndValidateExample() {

        {
            Foo foo = parseAndValidateExample("2; foourl=\"https://foo.example.com/\"", null);
            assertEquals(2, foo.amount);
            assertEquals("https://foo.example.com/", foo.url.toString());
        }

        {
            Foo foo = parseAndValidateExample("5", null);
            assertEquals(5, foo.amount);
            assertNull(foo.url);
        }

        {
            Foo foo = parseAndValidateExample("5; foourl=\"foo\"", URI.create("https://example.org/"));
            assertEquals(5, foo.amount);
            assertEquals("https://example.org/foo", foo.url.toString());
        }

        try {
            parseAndValidateExample("11; foourl=\"https://foo.example.com/\"", null);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (UnsupportedOperationException expected) {
        }

        try {
            parseAndValidateExample("9.0; foourl=\"https://foo.example.com/\"", null);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (UnsupportedOperationException expected) {
        }

        try {
            parseAndValidateExample("2; foourl=\"https:oh no//foo.example.com/\"", null);
            fail();
        } catch (IllegalArgumentException expected) {
        }

        try {
            parseAndValidateExample("2; foourl=\"relative\"", null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    private static class Foo {
        int amount;
        URI url;
    }

    private static Foo parseAndValidateExample(String serialization, URI baseUri) {
        Item<?> item = Parser.parseItem(serialization);

        long amountOfFoo = item.longValue();
        if (amountOfFoo < 0 || amountOfFoo > 10) {
            throw new IllegalArgumentException("invalid amountOfFoo (was " + amountOfFoo + ")");
        }

        Item<?> fooURLParam = item.params().get("foourl");

        URI url = null;
        if (fooURLParam != null) {
            url = URI.create(fooURLParam.stringValue());
            if (! url.isAbsolute()) {
                url = baseUri.resolve(url);
            }
        }

        Foo foo = new Foo();
        foo.amount = (int) amountOfFoo;
        foo.url = url;
        return foo;
    }
}
