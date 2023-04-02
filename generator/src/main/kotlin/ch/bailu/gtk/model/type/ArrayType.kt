package ch.bailu.gtk.model.type

class ArrayType(type: NamespaceType) : Type() {

    val size = findSize(type)
    val valid = size > 0

    companion object {
        fun findSize(type: NamespaceType): Int {
            if (type.name == "TypeClass") {
                return 8
            } else if (type.name == "TypeInterface") {
                return 16
            } else if (type.name == "ObjectClass") {
                return 136
            }
            return 0
        }
    }
    override fun getDebugIdentifier(): String {
        return "a"
    }
}
