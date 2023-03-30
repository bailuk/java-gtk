package ch.bailu.gtk.parser.tag

class FieldTag(parent: TagWithParent) : ParameterTag(parent) {

    val method = MethodTag(this)
    var isMethod = false
        private set

    override fun getChild(name: String): TagWithParent {
        if ("callback" == name) {
            isMethod = true
            return method
        }
        return super.getChild(name)
    }
}
