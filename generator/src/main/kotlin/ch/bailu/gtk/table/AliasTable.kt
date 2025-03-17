package ch.bailu.gtk.table

import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.model.type.NamespaceType
import java.io.Writer

object AliasTable : Logable {
    val table: MutableMap<NamespaceType, NamespaceType> = HashMap()

    init {
        add("glib", "String", "GString")
    }


    fun add(from: NamespaceType, to: NamespaceType) {
        table[from] = to
    }

    fun add(namespace: String, fromName: String, toName: String) {
        val from = NamespaceType(namespace, fromName)
        val to = NamespaceType(namespace, toName)
        add(from, to)
    }

    fun convert(fallbackNamespace: String, typeName: String): NamespaceType {
        return convert(NamespaceType(fallbackNamespace, typeName))
    }

    fun convert(from: NamespaceType): NamespaceType {
        return table[from] ?: from
    }

    override fun log(writer: Writer) {
        writer.write("# ${AliasTable.javaClass.canonicalName}\n\n")
        writer.write("| From                           | To\n")
        writer.write("|--------------------------------|---\n")
        table.forEach {
            writer.write(String.format("| %-30s | %s\n", it.key, it.value))
        }
    }
}
