package ch.bailu.gtk.tag;

public class AliasTag extends ParameterTag {
    public AliasTag(Tag parent) {
        super(parent);
    }

    @Override
    public void end() {
        getBuilder().buildAlias(this);
    }
}
