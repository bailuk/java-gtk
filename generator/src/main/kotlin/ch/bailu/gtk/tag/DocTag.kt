package ch.bailu.gtk.tag

class DocTag(parent: TagWithParent): TagWithParent(parent) {
    var aText = ""

    override fun getChild(name: String, prefix: String): TagWithParent {
        return ignore()
    }

    override fun setText(text: String) {
        this.aText = text
    }

    fun getText(): String {
        return aText
    }

}