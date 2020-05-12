package org.greenbytes.http.sfv;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NumberTests extends AbstractSpecificationTests {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return AbstractSpecificationTests.makeParameters("number.json");
    }

    public NumberTests(Object x, Object y) {
        this.p = (TestParams) y;
    }

    @Test
    public void runTest() {
        executeTest();
    }
}
