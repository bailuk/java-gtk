package ch.bailu.gtk.table

import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.model.type.NamespaceType
import java.io.Writer

object SizeTable: Logable {
    val table: MutableMap<NamespaceType, Int> = HashMap()

    init {
        add("gobject", "TypeClass", 8)
        add("gobject", "TypeInterface", 16)
    }


    private fun add(type: NamespaceType, size: Int) {
        table[type] = size
    }

    private fun add(namespace: String, name: String, size: Int) {
        add(NamespaceType(namespace, name), size)
    }

    fun getSize(namespace: String, name: String): Int {
        return table.getOrDefault(NamespaceType(namespace, name),0)
    }

    fun isSizeKnown(namespaceType: NamespaceType): Boolean {
        return table.getOrDefault(namespaceType,0) > 0
    }

    override fun log(writer: Writer) {
        writer.write("# ${SizeTable.javaClass.canonicalName}\n\n")
        writer.write("| Type                           | Size\n")
        writer.write("|--------------------------------|-----\n")

        table.forEach {
            writer.write(String.format("| %-30s | %d\n", it.key, it.value))
        }
    }
}
