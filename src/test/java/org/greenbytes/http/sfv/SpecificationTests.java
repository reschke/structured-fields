package org.greenbytes.http.sfv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SpecificationTests extends AbstractSpecificationTests {

    private static String basename = "";

    private static final Path output;

    static {
        String filename = System.getProperty("output");
        if (filename == null) {
            output = null;
        } else {
            output = Paths.get(filename);
            output.toFile().delete();
        }
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
        StringBuilder out = new StringBuilder();
        if (!p.filename.equals(basename)) {
            out.append("\n");
            out.append("# ").append(p.filename).append("\n");
            out.append("\n");
            basename = p.filename;
            out.append("\n");
        }
        out.append("## ").append(p.name).append("\n");
        out.append("\n");
        out.append("Input:").append("\n");
        out.append("~~~" + "\n");
        for (String s : p.raw) {
            out.append(s).append("\n");
        }
        out.append("~~~" + "\n");
        out.append("\n");

        executeTest(out);
        out.append("\n");

        if (output != null) {
            try (PrintWriter pw = new PrintWriter(
                    new FileOutputStream(output.toFile(), true), true, StandardCharsets.UTF_8)) {
                pw.println(out);
            } catch (FileNotFoundException ex) {
            }
        }
    }
}
