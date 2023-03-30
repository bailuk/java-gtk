package ch.bailu.gtk.model.type

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.log.DebugPrint


class StructureType(value: String) : Type() {

    enum class Types {PACKAGE, UNION, RECORD, CLASS, INTERFACE, BITFIELD, CALLBACK, ENUMERATION }

    val value = value.uppercase()

    val apiParentClassName: String
        get() =
            when {
                isRecord -> {
                    Configuration.BASE_NAME_SPACE_DOT + "type.Record"
                }
                isPackage -> {
                    Configuration.BASE_NAME_SPACE_DOT + "type.Package"
                }
                isCallback -> {
                    Configuration.BASE_NAME_SPACE_DOT + "type.Callback"
                }
                isInterface -> {
                    Configuration.BASE_NAME_SPACE_DOT + "type.Interface"
                }
                else -> {
                    Configuration.BASE_NAME_SPACE_DOT + "type.Pointer"
                }
            }


    constructor(type: Types) : this(type.toString())

    val isClassType: Boolean
        get() = compare(Types.CLASS) || compare(Types.RECORD) || compare(Types.INTERFACE) || compare(Types.UNION)

    private val isCallback: Boolean
        get() = compare(Types.CALLBACK)

    val isPackage: Boolean
        get() = compare(Types.PACKAGE)

    val isRecord: Boolean
        get() = compare(Types.RECORD) || compare(Types.UNION)

    val isInterface: Boolean
        get() = compare(Types.INTERFACE)

    fun compare(type: Types): Boolean {
        return value == type.toString()
    }

    override fun getDebugIdentifier(): String {
        return "S"
    }

    override fun toString(): String {
        return DebugPrint.colon(this, value)
    }
}
