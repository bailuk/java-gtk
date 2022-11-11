package ch.bailu.gtk.model.type

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.converter.isEnum
import ch.bailu.gtk.parser.tag.CallbackTag
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.AliasTable
import ch.bailu.gtk.table.CallbackTable
import ch.bailu.gtk.table.StructureTable.contains
import ch.bailu.gtk.table.WrapperTable
import ch.bailu.gtk.writer.Names

class ClassType {
    private var type: NamespaceType

    // Non-pointer access needed to access records inside records as fields
    private var directType = false

    private var valid = false
    private var wrapper = false

    private var callbackTag: CallbackTag? = null


    constructor(namespace: String, parameter: ParameterTag, supportsDirectType: Boolean) : this(namespace,
                parameter.getTypeName(),
                CType(parameter.getType()),
                !parameter.inDirection && isEnum(namespace, parameter), supportsDirectType)

    constructor(namespace: String, typeName: String, ctype: String, supportsDirectType: Boolean) : this(namespace, typeName, CType(ctype), false, supportsDirectType)

    constructor(namespace: String, typeName: String, ctype: CType, isOutEnum: Boolean, supportsDirectType: Boolean) {
        var t = typeName

        if (WrapperTable.contains(ctype.type)) {
            t = WrapperTable.convert(ctype.type)
            wrapper = true

        } else if (isOutEnum) {
            t = WrapperTable.convert("gint*")
            wrapper = true
        }

        type = convert(namespace, t)
        callbackTag = getCallbackTagFromTable(type)
        valid = wrapperOrCallback() || supportedClass(type, ctype, supportsDirectType)
        directType = supportsDirectType && !wrapperOrCallback() && supportedClass(type, ctype, supportsDirectType) && ctype.isDirectType
    }

    private fun wrapperOrCallback(): Boolean {
        return wrapper || isCallback()
    }

    private fun supportedClass(type: NamespaceType, ctype: CType, supportsDirectType: Boolean): Boolean {
        return isInStructureTable(type) && isPointerSupported(ctype, supportsDirectType)
    }

    private fun isPointerSupported(ctype: CType, supportsDirectType: Boolean): Boolean {
        return ctype.isSinglePointer || supportsDirectType && ctype.isDirectType
    }

    private fun convert(namespace: String, typeName: String): NamespaceType {
        return AliasTable.convert(NamespaceType(namespace, typeName))
    }

    fun getCallbackTag(): CallbackTag? {
        return callbackTag
    }

    private fun getCallbackTagFromTable(n: NamespaceType): CallbackTag? {
        return CallbackTable[n.namespace, n.name]
    }

    private fun isInStructureTable(n: NamespaceType): Boolean {
        return contains(n.namespace, n.name)
    }


    fun isClass(): Boolean {
        return valid
    }

    fun isCallback(): Boolean {
        return callbackTag != null
    }


    fun getApiTypeName(namespace: String): String {
        return if (isClass() && !type.isCurrentNameSpace(namespace)) {
            Names.getJavaClassNameWithNamespacePrefix(this.namespace, this.name)
        } else name

    }

    val namespace: String
        get() = type.namespace

    val name : String
        get() =  type.name

    fun isDirectType(): Boolean {
        return isClass() && directType
    }
}
