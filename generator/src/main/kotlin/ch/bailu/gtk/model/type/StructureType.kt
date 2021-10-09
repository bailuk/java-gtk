package ch.bailu.gtk.model.type


class StructureType(value: String) {

    enum class Types {PACKAGE, RECORD, CLASS, INTERFACE, BITFIELD, CALLBACK, ENUMERATION }

    val value = value.uppercase()

    constructor(type: Types) : this(type.toString())

    val isClassType: Boolean
        get() = compare(Types.CLASS) || compare(Types.RECORD) || compare(Types.INTERFACE)

    val isCallback: Boolean
        get() = compare(Types.CALLBACK)

    val isPackage: Boolean
        get() = compare(Types.PACKAGE)

    val isRecord: Boolean
        get() = compare(Types.RECORD)


    fun compare(type: Types): Boolean {
        return value == type.toString()
    }
}