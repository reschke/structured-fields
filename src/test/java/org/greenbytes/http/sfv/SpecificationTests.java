package org.greenbytes.http.sfv;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SpecificationTests extends AbstractSpecificationTests {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters(Arrays.asList(
                new String[] { "binary.json", "boolean.json", "dictionary.json", "examples.json", "item.json", "key-generated.json",
                        "large-generated.json", "list.json", "number.json", "param-list.json", "string.json", "token.json" }));
    }

    public SpecificationTests(Object x, Object y) {
        this.p = (TestParams) y;
    }

    @Test
    public void runTest() {
        executeTest();
    }
}
