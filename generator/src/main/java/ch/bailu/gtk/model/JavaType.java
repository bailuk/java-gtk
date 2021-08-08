package ch.bailu.gtk.model;

import ch.bailu.gtk.converter.EnumTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.PrimitivesTable;
import ch.bailu.gtk.tag.ParameterTag;

public class JavaType {

    private final String type;

    public JavaType(String type) {
        this.type = setType(type);
    }

    
    public JavaType(String namespace, ParameterTag parameter) {
	this(namespace, parameter.getTypeName(), parameter.getType());
    }
    
    public JavaType(String namespace, String typeName, String type) {
        if (isEnum(namespace, typeName)) {
            this.type = setType("int");
        } else {
            this.type = setType(PrimitivesTable.instance().convert(type));
        }
	
    }

    private boolean isEnum(String namespace, String typeName) {
        return EnumTable.instance().contains(new NamespaceType(namespace, typeName));
    }

    private static String setType(String type) {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public boolean isValid() {
        return ! "".equals(type);
    }

    public String getName() {
        return type;
    }

    public boolean isVoid() {
        return "void".equals(type);
    }
}
