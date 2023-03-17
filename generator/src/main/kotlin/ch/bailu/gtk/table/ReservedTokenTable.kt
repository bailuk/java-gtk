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
        add("false", "FALSE")
        add("true", "TRUE")
        add("ch", "CH")
        add("toString", "toStr")
    }

    private fun add(reserved: String, replacement: String) {
        table[reserved] = replacement
    }

    fun convert(token: String): String {
        return table[token] ?: convertNumber(token)
    }

    private fun convertNumber(token: String): String {
        if (token.isNotEmpty() && token[0].isDigit()) {
            return "_$token"
        }
        return token
    }
}
