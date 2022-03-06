package ch.bailu.gtk.converter

class RelativeNamespaceType(currentNamespace: String, namespaceType: NamespaceType) {
    private val type: NamespaceType = namespaceType
    private val hasCurrentNamespace = namespaceType.namespace == currentNamespace


    constructor(currentNamespace: String, typeName: String)
            : this(currentNamespace, NamespaceType(currentNamespace, typeName))


    fun isValid(): Boolean {
        return type.isValid()
    }

    fun hasCurrentNamespace(): Boolean {
        return hasCurrentNamespace
    }

    val namespace: String
        get() = type.namespace

    val name: String
        get() = type.name
}