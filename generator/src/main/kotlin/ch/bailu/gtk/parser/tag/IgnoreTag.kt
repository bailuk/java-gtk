package ch.bailu.gtk.parser.tag

class IgnoreTag (parent: Tag) : TagWithParent(parent){

    override fun getChild(name: String): TagWithParent {
        return ignore()
    }
}