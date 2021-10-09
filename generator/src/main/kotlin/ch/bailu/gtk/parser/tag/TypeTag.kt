package ch.bailu.gtk.parser.tag

open class TypeTag(parent: TagWithParent) : NamedWithDocTag(parent) {
    var aType: String = ""

    override fun setAttribute(name: String, value: String) {
        if ("type" == name) {
            aType = value
        } else {
            super.setAttribute(name, value)
        }
    }

    fun getType(): String {
        return aType
    }
}
