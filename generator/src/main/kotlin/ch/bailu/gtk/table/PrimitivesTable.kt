package ch.bailu.gtk.table

import java.util.*

object PrimitivesTable {

    private val table: MutableMap<String, String> = HashMap(50)

    init {
        add("none", "void")
        add("void", "void")
        add("gboolean", "boolean")
        add("gint", "int")
        add("glong", "long")
        add("guint", "int")
        add("gdouble", "double")
        add("guint16", "int")
        add("gfloat", "float")
        add("guint32", "int")
        add("gssize", "long")
        add("gulong", "long")
        add("gsize", "long")
        add("GType", "long")
        add("gchar", "byte")
        add("gunichar", "byte")
        add("gint64", "long")
        add("gint32", "int")
        add("gint16", "int")
        add("gint8", "int")
        add("guint64", "long")
        add("guint32", "int")
        add("guint16", "int")
        add("guint8", "int")
        add("time_t", "long")
        add("long", "long")
        add("int", "int")
        add("double", "double")
        add("float", "float")
        add("const char*", "String")
        add("const gchar*", "String")
        add("...", "java.lang.Object...")
        add("GQuark", "int")
    }

    private fun add(cType: String, jType: String) {
        table[cType] = jType
    }

    operator fun contains(cType: String): Boolean {
        return table.containsKey(cType)
    }

    fun convert(cType: String): String {
        return table[cType] ?: ""
    }
}
