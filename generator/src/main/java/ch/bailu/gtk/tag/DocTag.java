package ch.bailu.gtk.tag;

public class DocTag extends Tag {
    private String text = "";

    public DocTag(Tag parent) {
        super(parent);
    }

    @Override
    public Tag getChild(String name, String prefix) {
        return new IgnoreTag(this);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
