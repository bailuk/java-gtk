package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class ReservedTokenTable {
    
    private final Map<String, String> table = new HashMap<>();

    private static ReservedTokenTable INSTANCE = null;

    public static ReservedTokenTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new ReservedTokenTable();
        }
        return INSTANCE;
    }

    private ReservedTokenTable() {
        add("native",   "xnative");
        add("continue", "xcontinue");
        add("double",   "xdouble");
        add("int",      "xint");
        add("new",      "xnew");
        add("default",  "xdefault");
        add("private",  "xprivate");
        add("...",      "xelipse");
        add("notify",   "xnotify");
        add("interface","xinterface");
        add("2BUTTON_PRESS", "TWO_BUTTON_PRESS");
        add("3BUTTON_PRESS", "TREE_BUTTON_PRESS");
        add("2BIG", "_2_BIG");
        add("false", "FALSE");
        add("true", "TRUE");
        add("ch",   "CH");
        add("toString", "toStr");
    }


    private void add(String reserved, String replacement) {
        table.put(reserved, replacement);
    }

    public String convert(String token) {
        String result =  table.get(token);
        if (result == null) {
            result = token;
        }
        return result;
    }
}
