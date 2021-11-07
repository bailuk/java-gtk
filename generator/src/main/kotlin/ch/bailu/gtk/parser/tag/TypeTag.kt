package ch.bailu.gtk.parser.tag

open class TypeTag(parent: TagWithParent) : NamedWithDocTag(parent) {
    var type: String = ""
        private set

    override fun setAttribute(name: String, value: String) {
        if ("type" == name) {
            type = value
        } else {
            super.setAttribute(name, value)
        }
    }
}
