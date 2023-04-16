package ch.bailu.gtk.model.type

import ch.bailu.gtk.log.DebugPrint
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.AliasTable
import ch.bailu.gtk.table.StructureTable
import ch.bailu.gtk.table.WrapperTable
import ch.bailu.gtk.validator.Validator
import ch.bailu.gtk.writer.Names

/**
 * Wrapper or GObject structure
 */
class ClassType : Type {
    val type: NamespaceType

    /**
     * Non-pointer access needed to access records inside records as fields
     * direct as opposite to reference
     */
    val directType : Boolean
    val referenceType: Boolean
        get() = !directType

    val valid: Boolean
    val wrapper: Boolean

    private val cType: CType

    constructor(namespace: String, parameter: ParameterTag)
            : this(namespace, parameter.getTypeName(), CTypeConverter.toCTypeName(namespace, parameter))

    constructor(namespace: String, typeName: String, cTypeName: String) {
        cType = CType(cTypeName)

        val wrapperType = WrapperTable.convertToNamespaceType(cTypeName)
        val gtkType = convertToNamespaceType(namespace, typeName)

        type = if (wrapperType.valid && gtkType.valid && cType.isDirectType) {
            // Prefer wrapper if direct type
            directType = false
            valid = true
            wrapper = true
            wrapperType
        } else if (gtkType.valid) {
            // Else prefer gtk type
            directType = cType.isDirectType
            valid = true
            wrapper = false
            gtkType
        } else {
            // Else take wrapper
            wrapper = true
            valid = wrapperType.valid
            directType = false
            wrapperType
        }
    }

    private fun isPointerSupported(cType: CType): Boolean {
        return cType.isSinglePointer || cType.isDirectType
    }

    private fun convertToNamespaceType(namespace: String, typeName: String): NamespaceType {
        Validator.giveUp("wrong namespace: '$namespace' ($typeName)", typeName == "AsyncResult" && namespace != "gio")
        if (isPointerSupported(cType)) {
            val type = AliasTable.convert(NamespaceType(namespace, typeName))
            if (StructureTable.contains(type)) {
                return type
            }
        }
        return NamespaceType.INVALID
    }

    /**
     * Return API type name relative to namespace
     * example "Widget" or "ch.bailu.java-gtk.gtk.Widget"
     */
    fun getApiTypeName(namespace: String = ""): String {
        return Names.getApiTypeName(type, namespace)
    }

    override fun getDebugIdentifier(): String {
        return "G${if (wrapper && valid) "w" else if (valid) "g" else "_"}"
    }

    override fun toString(): String {
        return DebugPrint.colon(this, type.name, cType.toString())
    }
}
