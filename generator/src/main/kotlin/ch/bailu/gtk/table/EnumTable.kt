package ch.bailu.gtk.table

import ch.bailu.gtk.converter.NamespaceType
import java.util.*

object EnumTable {
    private val table: MutableMap<String, MutableMap<String, String>> = HashMap()

    fun add(type: NamespaceType) {
        if (type.isValid()) {
            getTable(type.getNamespace())[type.getName()] = type.getName()
        }
    }

    private fun getTable(namespace: String): MutableMap<String, String> {
        var result = table[namespace]
        if (result == null) {
            result = HashMap()
            table[namespace] = result
        }
        return result
    }

    operator fun contains(type: NamespaceType): Boolean {
        return type.isValid() && getTable(type.getNamespace()).containsKey(type.getName())
    }
}