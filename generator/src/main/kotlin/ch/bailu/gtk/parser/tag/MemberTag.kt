package ch.bailu.gtk.parser.tag

class MemberTag(parent: TagWithParent) : ParameterTag(parent) {

    override fun getType(): String {
        return if ("" == super.getType()) "int" else super.getType()
    }

    override fun getName(): String {
        return super.getName().uppercase()
    }
}
