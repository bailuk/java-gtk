package ch.bailu.gtk.table;

import java.util.HashMap;
import java.util.Map;

/**
 * cType to classType table.
 * Complex c-types are implemented manually as data structures in c-space.
 * These data structures are represented in Java-space by type-wrapper classes.
 */
public class WrapperTable {
    private static final String NAMESPACE = "type";

    private final Map<String, String> table = new HashMap<>(50);

    private static WrapperTable INSTANCE = null;

    public static WrapperTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new WrapperTable();
        }
        return INSTANCE;
    }

    private WrapperTable() {
        add("unsigned char*", "Bytes");
        add("const double*",  "Dbls");
        add("gchar*",         "Str");
        add("const char*",    "Str");
        add("const gchar*",   "Str");
        add("filename",       "Str");
        add("char**",         "Strs");
        add("gint*",          "Int");
        add("int*",           "Int");
        add("gdouble*",       "Dbls");
        add("gconstpointer",  "Pointer");
    }


    private void add(String ctype, String wrapper) {
        table.put(ctype, wrapper);
        StructureTable.instance().add(NAMESPACE, wrapper);
    }

    public boolean contains(String cType) {
        return table.containsKey(cType);
    }

    public String convert(String cType) {
        if (contains(cType)) {
            return  NAMESPACE + "." + table.get(cType);
        }
        return cType;
    }
}
