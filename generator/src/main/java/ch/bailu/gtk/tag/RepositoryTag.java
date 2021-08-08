package ch.bailu.gtk.tag;

public class RepositoryTag extends Tag {
    public RepositoryTag(Tag parent) {
        super(parent);
    }

    private final NamespaceTag namespace = new NamespaceTag(this);

    @Override
    public Tag getChild(String name, String prefix) {
        if ("namespace".equals(name)) {
            return namespace;
        }

        if ("package".equals(name)) {
            return namespace.addPackage(new NamedTag(this));
        }

        if ("c".equals(prefix) && "include".equals(name)) {
            return namespace.addInclude(new NamedTag(this));
        }

        return new IgnoreTag(this);
    }

}
