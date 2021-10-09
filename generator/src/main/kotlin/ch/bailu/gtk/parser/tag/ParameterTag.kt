package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.log.colonList


/**

## A field of struct of union structure, that is a C bit field, that is a fixed length in bits variable
element field {
Info.attrs,
## name of the field
attribute name { xsd:string },
## Binary attribute, true if the field is writeable
attribute writable { "0" | "1" }?,
## Binary attribute, true if the field is readable
attribute readable { "0" | "1" }?,
## Binary attribute, true if the field is private to the structure or has public ("0") visibility
attribute private { "0" | "1" }?,
## number of bits of the field
attribute bits { xsd:integer }?,

# Other elements a property can contain
(Info.elements
& (Callback | AnyType))
}

element parameter {
## name of the parameter
attribute name { xsd:string }?,
## Binary attribute, true if the parameter can have a null value
attribute nullable { "0" | "1" }?,
## Deprecated. Replaced by nullable and optional
attribute allow-none { "0" | "1" }?,
## Binary attribute which is "0" (false) if the element is not introspectable. It doesn't exist in the bindings, due in general to missing information in the annotations in the original C code
attribute introspectable { "0" | "1" }?,
## the parameter is a user_data for callbacks. The value points to a different parameter that is the actual callback
attribute closure { xsd:integer }?,
## the parameter is a destroy_data for callbacks. The value points to a different parameter that is the actual callback
attribute destroy { xsd:integer }?,
## the parameter is a callback, the value indicates the lifetime of the call. For language bindings which want to know when the resources required to do the call can be freed. "notified" valid until a GDestroyNotify argument is called, "async" only valid for the duration of the first callback invocationi (can only be called once), "call" only valid for the duration of the call, can be called multiple times during the call.
attribute scope { "notified" | "async" | "call" }?,
## direction of the parameter. "in" goes into the callable, "out" for output parameters from the callable (reference in C++, var in Pascal, etc...), "inout" for both (like a pre-allocated structure which will be filled-in by the callable)
attribute direction { "out" | "in" | "inout" }?,
## Binary attribute, true if the caller should allocate the parameter before calling the callable
attribute caller-allocates { "0" | "1" }?,
## Binary attribute, true if the parameter is optional
attribute optional { "0" | "1" }?,
##  Binary attribute, true if the parameter can be omitted from the introspected output
attribute skip { "0" | "1" }?,
TransferOwnership?,

(DocElements
& (AnyType | VarArgs))
}* &

 */
open class ParameterTag(parent: TagWithParent): NamedWithDocTag(parent) {
    private var value = ""
    private var inType = true


    fun getValue(): String {
        return value
    }

    private val type = TypeTag(this)
    private var isArray = false
    private var isVarargs = false

    private var isWriteable = false
    private var isPrivate = false

    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("type" == name) {
            return type
        }
        if ("array" == name) {
            isArray = true
            return type
        }
        if ("varargs" == name) {
            isVarargs = true
            return ignore()
        }
        return super.getChild(name, prefix)
    }

    override fun setAttribute(name: String, value: String) {
        if ("writable" == name) {
            isWriteable = "1" == value
        } else if ("private" == name) {
            isPrivate = "1" == value
        } else if ("value" == name) {
            this.value = value
        } else if ("direction" == name) {
            inType = "in" == value
        } else {
            super.setAttribute(name, value)
        }
    }

    fun getTypeName(): String {
        return type.getName()
    }


    open fun getType(): String {
        return type.getType()
    }

    fun isArray(): Boolean {
        return isArray
    }


    fun isWriteable(): Boolean {
        return isWriteable
    }


    fun isPrivate(): Boolean {
        return isPrivate
    }

    fun isVarargs(): Boolean {
        return isVarargs
    }

    fun isInDirection(): Boolean {
        return inType
    }

    fun isOutDirection(): Boolean {
        return !inType
    }

    override fun toString(): String {
        return colonList(arrayOf(getTypeName(), getType(), getValue()))
    }
}