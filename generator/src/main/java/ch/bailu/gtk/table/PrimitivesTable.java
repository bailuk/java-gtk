package ch.bailu.gtk.table;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTable {
    
    private final Map<String, String> table = new HashMap<>(50);

    private static PrimitivesTable INSTANCE = null;

    public static PrimitivesTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new PrimitivesTable();
        }
        return INSTANCE;
    }

    private PrimitivesTable() {
        add("none",          "void");
        add("void",          "void");
        add("gboolean",      "int");
        add("gint",          "int");
        add("glong",         "long");
        add("guint",         "int");
        add("gdouble",       "double");
        add("guint16",       "int");
        add("gfloat",        "float");
        add("guint32",       "int");
        add("gssize",        "long");
//        add("gpointer",      "long");
        add("gulong",        "long");
        add("gsize",         "long");
        add("gchar",         "char");
        add("gunichar",      "char");
        add("gint64",        "long");
        add("gint32",        "int");
        add("gint16",        "int");
        add("gint8",         "int");
        add("guint64",       "long");
        add("guint32",       "int");
        add("guint16",       "int");
        add("guint8",        "int");
        add("time_t",        "long");
        add("time_t",        "long");
        add("long",          "long");
        add("int",           "int");
        add("double",        "double");
    }

    private void add(String cType, String jType) {
        table.put(cType, jType);
    }

    public boolean contains(String cType) {
        return table.containsKey(cType);
    }

    public String convert(String cType) {
        return table.get(cType);
    }
}
