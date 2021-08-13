package ch.bailu.gtk.tag;

public class ParameterTag extends NamedTag {


    private final TypeTag type = new TypeTag(this);
    private ParameterTag array = null;
    private boolean isArray = false;


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
            //array = new ParameterTag(this);
            //return array;
        }

        /*
        if ("varargs".equals(name)) {
            array = new ParameterTag(this);
        }*/
        return ignore();
    }


    public String getTypeName() {
        return type.getName();
    }

    public String getType() {
        //System.out.println(type.getType());
        return type.getType();


        //if (isArray())
        //    return array.getType();
        //return type.getType();
    }

    public boolean isArray() {
        return isArray;
        //return array != null;
    }

}
