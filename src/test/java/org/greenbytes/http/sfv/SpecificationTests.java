package org.greenbytes.http.sfv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SpecificationTests extends AbstractSpecificationTests {

    private static String basename = "";

    private static final Path outputPath;

    static {
        String filename = System.getProperty("output");
        Path tmp = null;
        if (filename != null) {
            tmp = Paths.get(filename);
            try {
                Files.deleteIfExists(tmp);
            } catch (IOException e) {
                tmp = null;
            }
        }
        outputPath = tmp;
    }

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
        StringBuilder testOutput = new StringBuilder();
        if (basename.isEmpty()) {
            testOutput.append("# Test Report\n\n");
        }
        if (!p.filename.equals(basename)) {
            testOutput.append("\n## ").append(p.filename).append("\n\n\n");
            basename = p.filename;
        }
        testOutput.append("### ").append(p.name).append("\n\n");
        testOutput.append("Input:").append("\n");
        testOutput.append("~~~\n");
        for (String s : p.raw) {
            testOutput.append(s).append("\n");
        }
        testOutput.append("~~~\n\n");

        executeTest(testOutput);

        testOutput.append("\n");

        if (outputPath != null) {
            try {
                Files.write(outputPath,
                        testOutput.toString().getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (Exception ex) {
                System.err.printf("Can't write to %s%n.", outputPath);
            }
        }
    }
}
