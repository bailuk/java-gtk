package ch.bailu.gtk.table

import java.util.*

object ReservedTokenTable {
    private val table: MutableMap<String, String> = HashMap()

    init {
        add("native", "_native")
        add("continue", "_continue")
        add("double", "_double")
        add("int", "_int")
        add("new", "_new")
        add("default", "_default")
        add("private", "_private")
        add("...", "_elipse")
        //add("notify", "xnotify")
        add("interface", "_interface")
        add("false", "_false")
        add("true", "_true")
        //add("ch", "CH")
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
