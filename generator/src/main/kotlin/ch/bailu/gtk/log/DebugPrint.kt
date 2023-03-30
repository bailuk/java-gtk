package ch.bailu.gtk.log

import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.type.Type
import ch.bailu.gtk.parser.tag.TagWithParent

object DebugPrint {

    fun colon(tag: TagWithParent, vararg strings: String): String {
        return colonList(tag::class.simpleName?: "", strings.toList(), "(", ")")
    }

    fun colon(model: Model, vararg strings: String): String {
        return colonList(model::class.simpleName?: "", strings.toList())
    }

    fun colon(type: Type, vararg strings: String): String {
        return colonList(type.getDebugIdentifier(), strings.toList(), "{", "}")
    }

    private fun colonList(model: String, strings: List<String>, open: String = "[", close : String = "]"): String {
        val builder = StringBuilder()
        builder.append(open).append(model)
        for (s in strings) {
            builder.append(':')
            builder.append(s)
        }
        return builder.append(close).toString()
    }
}
