package org.greenbytes.http.sfv;

public class CLI {

    protected static final String ANSI_FAINT = "\u001B[2m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        int passed = 0;

        System.out.println();
        try {
            Parser parser = new Parser(args);
            Item<?> item = parser.parseItem();
            dump("Item", item);
            passed += 1;
        } catch (ParseException ex) {
            System.err.print("Item: " + ANSI_RED + ex.getDiagnostics().replace("\n", "\n      "));
            System.err.println(ANSI_RESET);
        }

        System.out.println();
        try {
            Parser parser = new Parser(args);
            OuterList list = parser.parseList();
            dump("List", list);
            passed += 1;
        } catch (ParseException ex) {
            System.err.print("List: " + ANSI_RED + ex.getDiagnostics().replace("\n", "\n      "));
            System.err.println(ANSI_RESET);
        }

        System.out.println();
        try {
            Parser parser = new Parser(args);
            Dictionary dict = parser.parseDictionary();
            dump("Dict", dict);
            passed += 1;
        } catch (ParseException ex) {
            System.err.print("Dict: " + ANSI_RED + ex.getDiagnostics().replace("\n", "\n      "));
            System.err.println(ANSI_RESET);
        }

        System.exit(passed > 0 ? 0 : 1);
    }

    private static void dump(String as, Type result) {
        System.out.println(as + ": " +
                ANSI_GREEN + result.serialize() + " " + ANSI_RESET +
                ANSI_FAINT + "(" + result.getClass().getSimpleName() + ")" + ANSI_RESET);
    }
}
