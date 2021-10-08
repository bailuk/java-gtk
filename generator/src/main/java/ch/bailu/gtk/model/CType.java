package ch.bailu.gtk.model;

import java.util.regex.Pattern;

public class CType {

    private final static Pattern P_CONST = Pattern.compile("^const [A-Za-z]+");
    private final static Pattern P_POINTER = Pattern.compile(".*[A-Za-z]+\\*$");
    private final static Pattern P_NOPOINTER = Pattern.compile(".*[A-Za-z]+$");


    private final boolean isPointer;
    private final boolean isConst;
    private final boolean isDirectType;

    private final String type;

    public CType(String type) {
        if (type == null || "".equals(type)) {
            type = "void*";
        }

        this.type = type;

        isPointer = P_POINTER.matcher(this.type).find();
        isConst = P_CONST.matcher(this.type).find();
        isDirectType = P_NOPOINTER.matcher(this.type).find();
    }

    public boolean contains(String type) {
        return this.type.contains(type);
    }

    public boolean isSinglePointer() {
        return isPointer;
    }

    public boolean isDirectType() {
        return isDirectType;
    }
    public boolean isConst() {
        return isConst;
    }
    public String getName() {
        return type;
    }

}
