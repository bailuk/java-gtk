package ch.bailu.gtk.tag;

public class MemberTag extends NamedTag {
    private String value = null;

    public MemberTag(Tag parent) {
        super(parent);
    }


    @Override
    public void setAttribute(String name, String value) {
        if ("value".equals(name)) {
            this.value = value;
        } else {
            super.setAttribute(name, value);
        }
    }

    public String getValue() {
        return value;
    }
}
