package ch.bailu.gtk.tag;


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
public class ParameterTag extends NamedTag {


    private final TypeTag type = new TypeTag(this);
    private boolean isArray = false;

    private boolean isWriteable = false;
    private boolean isPrivate = false;


    public ParameterTag(Tag parent) {
        super(parent);
    }



    @Override
    public Tag getChild(String name, String prefix) {
        if ("type".equals(name)) {
            return type;
        }
        if ("array".equals(name)) {
            isArray = true;
            return type;
        }

        return ignore();
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("writable".equals(name)) {
            isWriteable = "1".equals(value);
        } else if ("private".equals(name)) {
            isPrivate = "1".equals(value);
        } else {
            super.setAttribute(name, value);
        }
    }

    public String getTypeName() {
        return type.getName();
    }

    public String getType() {
        return type.getType();
    }

    public boolean isArray() {
        return isArray;
    }


    public boolean isWriteable() {
        return isWriteable;
    }


    public boolean isPrivate() {
        return isPrivate;
    }
}
