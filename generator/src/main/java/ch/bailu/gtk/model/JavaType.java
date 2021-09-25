package ch.bailu.gtk.model;

import ch.bailu.gtk.table.PrimitivesTable;
import ch.bailu.gtk.tag.ParameterTag;

public class JavaType {

    private final String type;


    public JavaType(ParameterTag parameter) {
	    this(parameter.getType());
    }
    
    public JavaType(String type) {
        this.type = setType(PrimitivesTable.instance().convert(type));
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

    public String getType() {
        return type;
    }

    public boolean isVoid() {
        return "void".equals(type);
    }
}
