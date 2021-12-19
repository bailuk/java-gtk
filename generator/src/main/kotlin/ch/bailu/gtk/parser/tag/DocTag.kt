package ch.bailu.gtk.parser.tag

class DocTag(parent: TagWithParent): TagWithParent(parent) {
    var aText = ""

    override fun getChild(name: String): TagWithParent {
        return ignore()
    }

    override fun setText(text: String) {
        this.aText = text
    }

    fun getText(): String {
        return aText
    }

}