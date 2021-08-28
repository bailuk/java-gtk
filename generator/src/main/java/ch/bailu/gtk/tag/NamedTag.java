package ch.bailu.gtk.tag;

public class NamedTag extends Tag{

    private String name;

    public NamedTag(Tag parent) {
        super(parent);
    }

    @Override
    public Tag getChild(String name, String prefix) {
        return new IgnoreTag(this);
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("name".equals(name)) {
            this.name = value;
        }
    }

    public String getName() {
        return name;
    }
}
