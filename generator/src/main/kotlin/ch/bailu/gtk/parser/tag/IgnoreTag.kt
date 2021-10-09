package ch.bailu.gtk.parser.tag

class IgnoreTag (parent: Tag) : TagWithParent(parent){

    override fun getChild(name: String, prefix: String): TagWithParent {
        return ignore()
    }
}