package ch.bailu.gtk.table

import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.parser.tag.CallbackTag
import java.io.Writer

object CallbackTable : Logable {
    private val table: MutableMap<String, MutableMap<String, CallbackTag?>> = HashMap()

    fun add(namespace: String, callbackTag: CallbackTag) {
        getTable(namespace)[callbackTag.getName()] = callbackTag
    }

    private fun getTable(namespace: String): MutableMap<String, CallbackTag?> {
        var result = table[namespace]
        if (result == null) {
            result = HashMap()
            table[namespace] = result
        }
        return result
    }

    operator fun get(namespace: String, name: String): CallbackTag? {
        return getTable(namespace)[name]
    }

    override fun log(writer: Writer) {
        table.onEach {
            writer.write("{${it.key}\n")

            it.value.forEach {
                writer.write(String.format("    %-40s %-40s\n", it.key, it.value))
            }
            writer.write("}\n\n")
        }
    }
}
