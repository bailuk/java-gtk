package ch.bailu.gtk.table

import java.util.*

object ReservedTokenTable {
    private val table: MutableMap<String, String> = HashMap()

    init {
        add("native", "xnative")
        add("continue", "xcontinue")
        add("double", "xdouble")
        add("int", "xint")
        add("new", "xnew")
        add("default", "xdefault")
        add("private", "xprivate")
        add("...", "xelipse")
        add("notify", "xnotify")
        add("interface", "xinterface")
        add("2BUTTON_PRESS", "TWO_BUTTON_PRESS")
        add("3BUTTON_PRESS", "TREE_BUTTON_PRESS")
        add("2BIG", "_2_BIG")
        add("false", "FALSE")
        add("true", "TRUE")
        add("ch", "CH")
        add("toString", "toStr")
    }

    private fun add(reserved: String, replacement: String) {
        table[reserved] = replacement
    }

    fun convert(token: String): String {
        return table[token] ?: token
    }
}