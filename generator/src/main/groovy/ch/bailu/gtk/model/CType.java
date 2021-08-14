package ch.bailu.gtk.model;

import java.util.regex.Pattern;

public class CType {

    private final static Pattern P_CONST = Pattern.compile("^const [A-Za-z]*");
    private final static Pattern P_POINTER = Pattern.compile(".*[A-Za-z]\\*$");

    final boolean isPointer;
    final boolean isConst;

    private final String type;

    public CType(String type) {
        if (type == null) {
            type = "void*";
        }


        this.type = type;

        isPointer = P_POINTER.matcher(this.type).find();
        isConst = P_CONST.matcher(this.type).find();
    }

    public boolean contains(String type) {
        return this.type.contains(type);
    }

    public boolean isSinglePointer() {
        return isPointer;
    }
    public boolean isConst() {
        return isConst;
    }
    public String getName() {
        return type;
    }
}
