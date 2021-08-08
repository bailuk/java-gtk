package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class ReservedTokenTable {
    

    private final static String[][] TABLE = {
        {"native",   "xnative"},
        {"continue", "xcontinue"},
        {"double",   "xdouble"},
        {"int",      "xint"},
        {"new",      "xnew"},
        {"default",  "xdefault"},
        {"private",  "xprivate"},
        {"...",      "xelipse"},
        {"notify",   "xnotify"}
};


    private final Map<String, String> table = new HashMap<>();

    private static ReservedTokenTable INSTANCE = null;

    public static ReservedTokenTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new ReservedTokenTable();
        }
        return INSTANCE;
    }

    private ReservedTokenTable() {
        for (String[] a : TABLE) {
            table.put(a[0], a[1]);
        }
    }

    public String convert(String token) {
        String result =  table.get(token);
        if (result == null) {
            result = token;
        }
        return result;
    }
}
