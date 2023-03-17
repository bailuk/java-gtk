package ch.bailu.gtk.parser.tag

import java.io.IOException
import java.util.*

class NamespaceTag(parent: TagWithParent): NamedWithDocTag(parent) {

    private val includes: MutableList<NamedWithDocTag> = ArrayList()
    private val functions: MutableList<MethodTag> = ArrayList()
    private val constants: MutableList<ParameterTag> = ArrayList()

    private var packageTag = NamedWithDocTag(this)

    @Throws(IOException::class)
    override fun started() {
        getBuilder().buildNamespaceStart(this, getNamespaceConfig())
    }


    @Throws(IOException::class)
    override fun end() {
        getBuilder().buildNamespaceEnd()
    }

    override fun getChild(name: String): TagWithParent {
        if ("class" == name) {
            return StructureTag(this, name)
        }
        if ("record" == name) {
            return StructureTag(this, name)
        }
        if ("union" == name) {
            return StructureTag(this, name)
        }
        if ("interface" == name) {
            return StructureTag(this, name)
        }
        if ("enumeration" == name) {
            return EnumerationTag(this)
        }
        if ("constant" == name) {
            val c = ParameterTag(this)
            constants.add(c)
            return c
        }
        if ("bitfield" == name) {
            return EnumerationTag(this)
        }
        if ("alias" == name) {
            return AliasTag(this)
        }
        if ("function" == name) {
            val f = MethodTag(this)
            functions.add(f)
            return f
        }
        if ("callback" == name) {
            return CallbackTag(this)
        }
        return super.getChild(name)
    }

    fun getIncludes(): List<NamedWithDocTag> {
        return includes
    }

    fun getFunctions(): List<MethodTag> {
        return functions
    }

    fun getConstants(): List<ParameterTag> {
        return constants
    }

    fun getPackage(): String {
        return packageTag.getName()
    }

    fun addInclude(include: NamedWithDocTag): TagWithParent {
        includes.add(include)
        return include
    }

    fun addPackage(pkg: NamedWithDocTag): TagWithParent {
        packageTag = pkg
        return pkg
    }
}
