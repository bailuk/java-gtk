package ch.bailu.gtk.tag;

import java.io.IOException;
import java.util.List;

import ch.bailu.gtk.builder.BuilderInterface;

public abstract class Tag {

    private final Tag parent;

    public Tag(Tag parent) {
        this.parent = parent;
    }


    public abstract Tag getChild(String name, String prefix);

    public void started() throws IOException {}
    public void end() throws IOException {}

    public void setText(String text) {}
    public void setAttribute(String name, String value) {}

    public Tag getParent() {
        return parent;
    }


    protected Tag ignore() {
        return new IgnoreTag(this);
    }

    protected static Tag add(List list, Tag tag) {
        list.add(tag);
        return tag;
    }

    public BuilderInterface getBuilder() {
        return parent.getBuilder();
    }
}
