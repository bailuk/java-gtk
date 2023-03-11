package ch.bailu.gtk.table

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.log.Logable
import java.io.Writer

object AliasTable : Logable {
    val table: MutableMap<NamespaceType, NamespaceType> = HashMap()

    init {
        add(NamespaceType("glib", "String"),
            NamespaceType("glib", "GString"))

        // add(NamespaceType("gobject", "Object"),
        //    NamespaceType("gobject", "GObject"))
    }



    fun add(from: NamespaceType, to: NamespaceType) {
        table[from] = to
    }

    fun convert(from: NamespaceType): NamespaceType {
        return table[from] ?: from
    }

    fun add(namespace: String, fromName: String, toName: String) {
        val from = NamespaceType(namespace, fromName)
        val to = NamespaceType(namespace, toName)
        add(from, to)
    }

    override fun log(writer: Writer) {
        table.forEach {
            writer.write(String.format("%-30s => %s\n", it.key, it.value))
        }
    }
}
