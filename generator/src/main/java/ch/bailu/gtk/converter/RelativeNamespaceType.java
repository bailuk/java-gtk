package ch.bailu.gtk.converter;

public class RelativeNamespaceType {

    private final NamespaceType type;
    private final boolean hasCurrentNamespace;



    public RelativeNamespaceType(String currentNamespace, String typeName) {
        this(currentNamespace, new NamespaceType(currentNamespace, typeName));
    }

    public RelativeNamespaceType(String currentNamespace, NamespaceType namespaceType) {
        type = namespaceType;
        hasCurrentNamespace = type.getNamespace().equals(currentNamespace);

    }

    public boolean isValid() {
        return type.isValid();
    }

    public boolean hasCurrentNamespace() {
        return hasCurrentNamespace;
    }

    public String getNamespace() {
        return type.getNamespace();
    }

    public String getName() {
        return type.getName();
    }
}
