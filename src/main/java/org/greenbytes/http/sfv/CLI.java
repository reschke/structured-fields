package org.greenbytes.http.sfv;

public class CLI {

    static final boolean isInteractive = System.console() != null;

    static final String ANSI_FAINT = isInteractive ? "\u001B[2m" : "";
    static final String ANSI_GREEN = isInteractive ? "\u001B[32m" : "";
    static final String ANSI_RED = isInteractive ? "\u001B[31m" : "";
    static final String ANSI_RESET = isInteractive ? "\u001B[0m" : "";

    public static void main(String[] args) {
        int passed = 0;

        System.out.println();
        try {
            Parser parser = new Parser(args);
            Item<?> item = parser.parseItem();
            dump("Item", item);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics("Item", ex);
        }

        System.out.println();
        try {
            Parser parser = new Parser(args);
            OuterList list = parser.parseList();
            dump("List", list);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics("List", ex);
        }

        System.out.println();
        try {
            Parser parser = new Parser(args);
            Dictionary dict = parser.parseDictionary();
            dump("Dict", dict);
            passed += 1;
        } catch (ParseException ex) {
            diagnostics("Dict", ex);
        }

        System.exit(passed > 0 ? 0 : 1);
    }

    private static void dump(String as, Type result) {
        System.out.println(as + ": " +
                ANSI_GREEN + result.serialize() + " " + ANSI_RESET +
                ANSI_FAINT + "(" + result.getClass().getSimpleName() + ")" + ANSI_RESET);
    }

    private static void diagnostics(String as, ParseException ex) {
        System.out.println(as + ": " +
                ANSI_RED + ex.getDiagnostics().replace("\n", "\n      ").trim() + ANSI_RESET);
    }
}
