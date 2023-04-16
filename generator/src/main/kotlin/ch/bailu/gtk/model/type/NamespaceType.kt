package ch.bailu.gtk.model.type

import java.util.*
import java.util.regex.Pattern

class NamespaceType(fallbackNamespace: String, typeName: String) {
    companion object {
        private val NAMESPACE = Pattern.compile("^([A-Za-z_]\\w+)\\.([A-Za-z_]\\w+)$")
        private val NONAMESPACE = Pattern.compile("^[A-Za-z_]\\w+$")

        val INVALID = NamespaceType("", "")
    }

    val namespace: String
    val name: String

    val valid: Boolean
        get() = name.isNotEmpty()

    init {
        var m = NAMESPACE.matcher(typeName)
        if (m.find()) {
            namespace = m.group(1).lowercase()
            name = m.group(2)
        } else {
            m = NONAMESPACE.matcher(typeName)
            if (m.find()) {
                namespace = fallbackNamespace.lowercase()
                name = typeName
            } else {
                namespace = ""
                name = ""
            }
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(namespace, name)
    }

    override fun toString(): String {
        return "${namespace}.${name}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NamespaceType

        if (namespace != other.namespace) return false
        if (name != other.name) return false

        return true
    }

    fun isCurrentNameSpace(currentNameSpace: String): Boolean {
        return namespace == currentNameSpace
    }
}
