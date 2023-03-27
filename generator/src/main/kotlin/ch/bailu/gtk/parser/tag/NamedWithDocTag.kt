package ch.bailu.gtk.parser.tag

open class NamedWithDocTag(parent: TagWithParent) : TagWithParent(parent){
    private var name: String = ""
    private val docTag =  DocTag(this)

    override fun getChild(name: String): TagWithParent {
        if ("doc" == name) {
            return docTag
        }
        return ignore()
    }

    override fun setAttribute(name: String, value: String) {
        if ("name" == name) {
            this.name = value
        }
    }

    open fun getName(): String {
        return name
    }

    fun getDoc(): String {
        return docTag.getText()
    }
}
