package org.greenbytes.http.sfv;

import java.io.PrintStream;

public class CLI {

    static final boolean isInteractive = System.console() != null;

    static final String ANSI_FAINT = isInteractive ? "\u001B[2m" : "";
    static final String ANSI_GREEN = isInteractive ? "\u001B[32m" : "";
    static final String ANSI_RED = isInteractive ? "\u001B[31m" : "";
    static final String ANSI_RESET = isInteractive ? "\u001B[0m" : "";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: all arguments will be passed to the SF parser - you passed none");
            System.exit(2);
        } else {
            System.exit(cli(System.out, args));
        }
    }

    protected static int cli(PrintStream out, String... args) {

        int passed = 0;

        out.println();
        try {
            Parser parser = new Parser(args);
            Item<?> item = parser.parseItem();
            dump(out, "Item", item);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics(out, "Item", ex);
        }

        out.println();
        try {
            Parser parser = new Parser(args);
            OuterList list = parser.parseList();
            dump(out, "List", list);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics(out, "List", ex);
        }

        out.println();
        try {
            Parser parser = new Parser(args);
            Dictionary dict = parser.parseDictionary();
            dump(out, "Dict", dict);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics(out, "Dict", ex);
        }

        return passed > 0 ? 0 : 1;
    }

    private static void dump(PrintStream out, String as, Type<?> result) {
        out.println(as + ": " +
                ANSI_GREEN + result.serialize() + " " + ANSI_RESET +
                ANSI_FAINT + "(" + result.getClass().getSimpleName() + ")" + ANSI_RESET);
        out.println(result.serializeToForDebug(new StringBuilder(), 2));
    }

    private static void diagnostics(PrintStream out, String as, ParseException ex) {
        out.println(as + ": " +
                ANSI_RED + ex.getDiagnostics().replace("\n", "\n      ").trim() + ANSI_RESET);
    }
}
