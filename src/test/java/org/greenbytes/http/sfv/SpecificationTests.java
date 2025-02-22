package org.greenbytes.http.sfv;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SpecificationTests extends AbstractSpecificationTests {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("binary.json", "boolean.json", "date.json", "dictionary.json",
                "display-string.json", "examples.json", "item.json", "key-generated.json", "large-generated.json", "list.json",
                "listlist.json", "number.json", "number-generated.json", "param-dict.json", "param-list.json",
                "param-listlist.json", "string.json", "string-generated.json", "token.json", "token-generated.json");
    }

    public SpecificationTests(Object x, Object y) {
        this.p = (TestParams) y;
    }

    @Test
    public void runTest() {
        executeTest();
    }
}
