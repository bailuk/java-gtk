package ch.bailu.gtk.converter

import java.util.*
import java.util.regex.Pattern

class NamespaceType(fallbackNamespace: String, typeName: String) {
    private val NAMESPACE = Pattern.compile("^([A-Za-z]\\w+)\\.([A-Za-z]\\w+)$")
    private val NONAMESPACE = Pattern.compile("^[A-Za-z]\\w+$")


    private var namespace: String = ""
    private var name: String = ""

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


    fun getNamespace(): String? {
        return namespace
    }

    fun getName(): String {
        return name
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as NamespaceType
        return namespace == that.namespace &&
                name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(namespace, name)
    }

    fun isValid(): Boolean {
        return "" != name
    }

    override fun toString(): String {
        return "${namespace}.${name}"
    }
}