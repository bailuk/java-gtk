package ch.bailu.gtk.tag;

public class IgnoreTag extends Tag
{
    public IgnoreTag(Tag parent) {
        super(parent);
    }

    @Override
    public Tag getChild(String name, String prefix) {
        return ignore();
    }
}
