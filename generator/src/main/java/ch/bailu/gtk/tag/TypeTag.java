package ch.bailu.gtk.tag;

public class TypeTag extends NamedTag {
    private String type;

    public TypeTag(Tag parent) {
        super(parent);
    }


    @Override
    public void setAttribute(String name, String value) {
        if ("type".equals(name)) {
            type = value;
        } else {
            super.setAttribute(name, value);
        }

    }


    public String getType() {
        return type;
    }
}
