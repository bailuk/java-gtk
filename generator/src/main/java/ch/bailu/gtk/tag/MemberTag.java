package ch.bailu.gtk.tag;

public class MemberTag extends ParameterTag {
    public MemberTag(Tag parent) {
        super(parent);
    }

    public String getType() {
        if (super.getType() == null)
            return "int";

        return super.getType();
    }

    @Override
    public String getName() {
        return super.getName().toUpperCase();
    }
}
