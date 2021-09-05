package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTable {
    
    private final static String[][] TABLE = {
        {"none",          "void"},
        {"gboolean",      "int"},
        {"gint",          "int"},
        {"glong",         "long"},
        {"guint",         "int"},
        {"gdouble",       "double"},
        {"guint16",       "int"},
        {"gfloat",        "float"},
        {"gchar*",        "String"},
        {"guint32",       "int"},
        {"gssize",        "long"},
        {"gpointer",      "long"},
        {"gulong",        "long"},
        {"gsize",         "long"},
        {"gchar",         "char"},
        {"gunichar",      "char"},
        {"filename",      "String"},
        {"gint64",        "long"},
        {"gint32",        "int"},
        {"gint16",        "int"},
        {"gint8",         "int"},
        {"guint64",       "long"},
        {"guint32",       "int"},
        {"guint16",       "int"},
        {"guint8",        "int"},
        {"time_t",        "long"},
        {"const char*",   "String"},
        {"time_t",        "long"},
        {"const gchar*",  "String"},
        {"void",          "void"},
        {"int",           "int"},
        {"double",        "double"},
        {"char**",        "String[]"},
    };

    private final Map<String, String> table = new HashMap<>(50);

    private static PrimitivesTable INSTANCE = null;

    public static PrimitivesTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new PrimitivesTable();
        }
        return INSTANCE;
    }

    private PrimitivesTable() {
        for (String[] a : TABLE) {
            table.put(a[0], a[1]);
        }
    }

    public boolean contains(String cType) {
        return table.containsKey(cType);
    }

    public String convert(String cType) {
        return table.get(cType);
    }
}
