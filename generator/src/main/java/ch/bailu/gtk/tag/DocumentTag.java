package ch.bailu.gtk.tag;

import ch.bailu.gtk.builder.BuilderInterface;

public class DocumentTag extends Tag {

    private final BuilderInterface builder;

    public DocumentTag(BuilderInterface builder) {
        super(null);
        this.builder = builder;
    }

    @Override
    public BuilderInterface getBuilder() {
        return builder;
    }

    @Override
    public Tag getChild(String name, String prefix) {
        if ("repository".equals(name)) {
            return new RepositoryTag(this);
        }
        return ignore();
    }
}
