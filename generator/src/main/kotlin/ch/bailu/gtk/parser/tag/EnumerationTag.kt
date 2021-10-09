package ch.bailu.gtk.parser.tag

import java.io.IOException

class EnumerationTag(parent: TagWithParent) : TypeTag(parent) {
    private val members = TagList<ParameterTag>()

    override fun getChild(name: String, prefix: String): TagWithParent {
        return if ("member" == name) {
            members.addTag(MemberTag(this))
        } else super.getChild(name, prefix)
    }

    @Throws(IOException::class)
    override fun end() {
        getBuilder().buildEnumeration(this)
    }

    fun getMembers(): List<ParameterTag> {
        return members
    }
}
