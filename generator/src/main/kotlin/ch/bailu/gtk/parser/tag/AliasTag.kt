package ch.bailu.gtk.parser.tag

class AliasTag(parent: TagWithParent): ParameterTag(parent) {

    override fun end() {
        getBuilder().buildAlias(this)
    }
}