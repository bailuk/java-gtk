package ch.bailu.gtk.tag

open class NamedWithDocTag(parent: TagWithParent) : TagWithParent(parent){
    private var name: String = ""
    private val doc =  DocTag(this)

    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("doc" == name) {
            return doc
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
        return doc.getText()
    }
}