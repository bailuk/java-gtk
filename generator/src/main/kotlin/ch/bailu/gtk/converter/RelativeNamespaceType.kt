package ch.bailu.gtk.converter

class RelativeNamespaceType(currentNamespace: String, namespaceType: NamespaceType) {
    private val type: NamespaceType = namespaceType
    private val hasCurrentNamespace = namespaceType.getNamespace() == currentNamespace


    constructor(currentNamespace: String, typeName: String)
            : this(currentNamespace, NamespaceType(currentNamespace, typeName))


    fun isValid(): Boolean {
        return type.isValid()
    }

    fun hasCurrentNamespace(): Boolean {
        return hasCurrentNamespace
    }

    fun getNamespace(): String {
        return type.getNamespace()
    }

    fun getName(): String {
        return type.getName()
    }
}