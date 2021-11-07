package ch.bailu.gtk.parser.tag

import java.io.IOException

class StructureTag(parent: TagWithParent, val structureType: String): NamedWithDocTag(parent) {

    var parent = ""
        private set

    var disguised = false
        private set

    var type = ""
        private set

    val implementsList = TagList<NamedWithDocTag>()
    val constructors = TagList<MethodTag>()
    val functions = TagList<MethodTag>()


    val virtuals = TagList<MethodTag>()
    val methods = TagList<MethodTag>()

    val signals = TagList<MethodTag>()
    val fields = TagList<ParameterTag>()

    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("field" == name) {
            return fields.addTag(ParameterTag(this))
        }
        if ("implements" == name) {
            return implementsList.addTag(NamedWithDocTag(this))
        }
        if ("constructor" == name) {
            return constructors.addTag(MethodTag(this))
        }
        if ("virtual-method" == name) {
            return virtuals.addTag(MethodTag(this))
        }
        if ("method" == name) {
            return methods.addTag(MethodTag(this))
        }
        if ("signal" == name) {
            return signals.addTag(MethodTag(this))
        }
        if ("function" == name) {
            val f = MethodTag(this)
            functions.add(f)
            return f
        }
        return super.getChild(name, prefix)
    }

    @Throws(IOException::class)
    override fun end() {
        if (!disguised) {
            getBuilder().buildStructure(this)
        }
    }


    override fun setAttribute(name: String, value: String) {
        if ("parent" == name) {
            parent = value
        } else if ("disguised" == name) {
            disguised = "1" == value
        } else if ("type" == name) {
            type = value
        } else {
            super.setAttribute(name, value)
        }
    }

}