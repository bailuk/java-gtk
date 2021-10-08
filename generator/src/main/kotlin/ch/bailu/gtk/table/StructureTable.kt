package ch.bailu.gtk.table

import java.io.Writer
import java.util.HashMap

object StructureTable {
    private val table: MutableMap<String, MutableMap<String, Int>> = HashMap()

    fun add(namespace: String, name: String) {
        val map = getTable(namespace)
        if (map[name] == null) {
            map[name] = 1
        } else {
            map[name]?.inc()
        }
    }

    private fun getTable(namespace: String): MutableMap<String, Int> {
        var result = table[namespace]
        if (result == null) {
            result = HashMap()
            table[namespace] = result
        }
        return result
    }

    fun contains(namespace: String, name: String): Boolean {
        return getTable(namespace)[name] != null
    }

    fun log(writer: Writer) {
        table.onEach {
            writer.write("{${it.key}\n")
            it.value.forEach {
                writer.write(String.format("%5d %-40s\n", it.value, it.key))
            }
            writer.write("}\n\n")

        }
    }
}