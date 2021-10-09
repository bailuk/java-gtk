package ch.bailu.gtk.parser.tag

import java.io.IOException

class StructureTag(parent: TagWithParent, structureType: String): NamedWithDocTag(parent) {

    private var parent = ""
    private val structureType: String = structureType
    private var disguised = false

    private var type = ""

    private val implementsList = TagList<NamedWithDocTag>()
    private val constructors = TagList<MethodTag>()
    private val functions = TagList<MethodTag>()


    private val virtuals = TagList<MethodTag>()
    private val methods = TagList<MethodTag>()

    private val signals = TagList<MethodTag>()
    private val fields = TagList<ParameterTag>()

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
        if (isDisguised() == false) {
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


    fun getParentName(): String {
        return parent
    }

    fun getStructureType(): String {
        return structureType
    }

    fun getImplementsList(): List<NamedWithDocTag> {
        return implementsList
    }

    fun getConstructors(): List<MethodTag> {
        return constructors
    }

    fun getVirtuals(): List<MethodTag> {
        return virtuals
    }

    fun getMethods(): List<MethodTag> {
        return methods
    }

    fun getSignals(): List<MethodTag> {
        return signals
    }

    fun getFields(): List<ParameterTag> {
        return fields
    }

    fun getFunctions(): List<MethodTag> {
        return functions
    }

    fun isDisguised(): Boolean {
        return disguised
    }

    fun getType(): String {
        return type
    }
}