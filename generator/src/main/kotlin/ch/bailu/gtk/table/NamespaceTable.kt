package ch.bailu.gtk.table

import java.util.*

object NamespaceTable {
    private val table: MutableMap<String, String> = HashMap()

    fun add(namespace: String) {
        table[namespace.lowercase()] = namespace
    }

    operator fun contains(namespace: String): Boolean {
        return table.containsKey(namespace)
    }
}