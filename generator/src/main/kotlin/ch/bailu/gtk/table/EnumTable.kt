package ch.bailu.gtk.table

import ch.bailu.gtk.model.type.NamespaceType
import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.parser.tag.ParameterTag
import java.io.Writer
import java.util.*

object EnumTable: Logable {
    private val table: MutableMap<String, MutableMap<String, String>> = HashMap()

    fun add(type: NamespaceType) {
        if (type.valid) {
            getTable(type.namespace)[type.name] = type.name
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
        return type.valid && getTable(type.namespace).containsKey(type.name)
    }

    fun isEnum(namespace: String, parameter: ParameterTag): Boolean {
        return isEnum(namespace, parameter.getTypeName()) || isEnum(namespace, parameter.getType())
    }

    private fun isEnum(namespace: String, typeName: String): Boolean {
        return contains(NamespaceType(namespace, typeName))
    }

    override fun log(writer: Writer) {
        table.onEach { namespace ->
            writer.write("{${namespace.key}\n")
            namespace.value.forEach {
                writer.write(String.format("    %-40s\n", it.key))
            }
            writer.write("}\n\n")
        }
    }
}
