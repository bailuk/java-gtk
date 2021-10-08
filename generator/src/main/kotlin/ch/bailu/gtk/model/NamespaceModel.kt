package ch.bailu.gtk.model

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.converter.RelativeNamespaceType
import ch.bailu.gtk.table.NamespaceTable
import ch.bailu.gtk.tag.NamespaceTag
import java.io.File
import java.util.*

class NamespaceModel : Model, NamespaceInterface {
    private val includes: MutableList<String> = ArrayList()
    private val namespace: String

    constructor(namespace: NamespaceTag) {
        this.namespace = namespace.getName().lowercase()
        for (i in namespace.getIncludes()) {
            includes.add(i.getName())
        }
        setSupported("Namespace", NamespaceTable.contains(this.namespace))
    }

    constructor() {
        namespace = ""
        setSupported("", true)
    }

    constructor(type: RelativeNamespaceType) {
        namespace = type.namespace
        setSupported("Namespace", NamespaceTable.contains(namespace))
    }

    val javaSourceDirectory: File
        get() = File(Configuration.getInstance().javaBaseDir, namespace)
    val cSourceDirectory: File
        get() = Configuration.getInstance().cBaseDir

    fun getIncludes(): List<String> {
        return includes
    }

    override fun getNamespace(): String {
        return namespace
    }

    override fun getFullNamespace(): String {
        return if ("" == namespace) {
            Configuration.BASE_NAME_SPACE_NODOT
        } else {
            Configuration.BASE_NAME_SPACE_DOT + namespace
        }
    }
}
