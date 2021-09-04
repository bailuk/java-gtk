package ch.bailu.gtk.converter;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NamespaceType {
    private final static Pattern NAMESPACE = Pattern.compile("^([A-Za-z]\\w+)\\.([A-Za-z]\\w+)$");
    private final static Pattern NONAMESPACE = Pattern.compile("^[A-Za-z]\\w+$");


    private final String namespace;
    private final String name;


    public NamespaceType(String fallbackNamespace, String typeName) {
        if (fallbackNamespace == null) {
            fallbackNamespace = "";
        }

        fallbackNamespace = fallbackNamespace.toLowerCase();

        if (typeName == null) {
            typeName = "";
        }


        Matcher m = NAMESPACE.matcher(typeName);

        if (m.find()) {
            namespace = m.group(1).toLowerCase();
            name = m.group(2);
        } else {
            m = NONAMESPACE.matcher(typeName);
            if (m.find()) {
                namespace = fallbackNamespace;
                name = typeName;
            } else {
                namespace = "";
                name = "";
            }
        }
    }


    public String getNamespace() {
        return namespace;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceType that = (NamespaceType) o;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    public boolean isValid() {
        return !"".equals(name);
    }
}
