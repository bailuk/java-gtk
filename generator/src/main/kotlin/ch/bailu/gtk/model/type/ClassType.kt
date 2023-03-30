package ch.bailu.gtk.model.type

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.log.DebugPrint
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.AliasTable
import ch.bailu.gtk.table.StructureTable.contains
import ch.bailu.gtk.table.WrapperTable
import ch.bailu.gtk.writer.Names

class ClassType : Type {
    private var type: NamespaceType

    /**
     * Non-pointer access needed to access records inside records as fields
     * direct as opposite to reference
     */
    val directType : Boolean
    val referenceType: Boolean
        get() = !directType

    val valid: Boolean
    val wrapper: Boolean

    val namespace: String
        get() = type.namespace

    val name : String
        get() =  type.name


    constructor(namespace: String, parameter: ParameterTag)
            : this(namespace, parameter.getTypeName(), EnumType.toCTypeName(namespace, parameter))

    constructor(namespace: String, typeName: String, cTypeName: String) {
        var classOrWrapperTypeName = typeName
        val cType = CType(cTypeName)

        wrapper = if (WrapperTable.contains(cType.type)) {
            classOrWrapperTypeName = WrapperTable.convert(cType.type)
            true
        } else {
            false
        }

        type = convert(namespace, classOrWrapperTypeName)
        valid = wrapper || supportedClass(type, cType)
        directType = wrapper || (supportedClass(type, cType) && cType.isDirectType)
    }

    private fun supportedClass(type: NamespaceType, ctype: CType): Boolean {
        return (isInStructureTable(type) && isPointerSupported(ctype))
    }

    private fun isPointerSupported(ctype: CType): Boolean {
        return ctype.isSinglePointer || ctype.isDirectType
    }

    private fun convert(namespace: String, typeName: String): NamespaceType {
        return AliasTable.convert(NamespaceType(namespace, typeName))
    }

    private fun isInStructureTable(n: NamespaceType): Boolean {
        return contains(n.namespace, n.name)
    }

    /**
     * Return API type name relative to namespace
     * example "Widget" or "ch.bailu.java-gtk.gtk.Widget"
     */
    fun getApiTypeName(namespace: String = ""): String {
        return Names.getApiTypeName(type, namespace)
    }

    override fun getDebugIdentifier(): String {
        return "G"
    }

    override fun toString(): String {
        return DebugPrint.colon(this, name, type.name)
    }
}
