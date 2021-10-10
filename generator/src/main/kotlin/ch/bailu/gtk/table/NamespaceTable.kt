package ch.bailu.gtk.table

import ch.bailu.gtk.config.NamespaceConfig
import java.util.*

object NamespaceTable {
    private val table: MutableMap<String, NamespaceConfig> = HashMap()

    fun add(namespace: String, namespaceConfig: NamespaceConfig) {
        table[namespace.lowercase()] = namespaceConfig
    }

    operator fun contains(namespace: String): Boolean {
        return table.containsKey(namespace)
    }

    fun with(namespace: String, action: (NamespaceConfig) -> Unit) {
        val config = table.get(namespace)
        if (config != null) {
            action(config)
        }
    }
}