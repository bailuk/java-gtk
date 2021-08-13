package ch.bailu.gtk.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NamespaceType {
    private final static Pattern NAMESPACE = Pattern.compile("^([A-Za-z]+)\\.([A-Za-z]+)$");
    private final static Pattern NONAMESPACE = Pattern.compile("^[A-Za-z]+$");


    private final String namespace;
    private final String type;

    private final boolean hasCurrentNamespace;
    private final boolean isValid;


    public NamespaceType(String currentNamespace, String type) {
        if (currentNamespace == null) {
            currentNamespace = "";
        }

        currentNamespace = currentNamespace.toLowerCase();

        if (type == null) {
            type = "";
        }



        Matcher m = NAMESPACE.matcher(type);

        if (m.find()) {
            namespace = m.group(1).toLowerCase();
            this.type = m.group(2);
            isValid = true;
        } else {
            m = NONAMESPACE.matcher(type);
            if (m.find()) {
                namespace = currentNamespace;
                this.type = type;
                isValid = true;
            } else {
                namespace = "";
                this.type = "";
                isValid = false;
            }
        }

        hasCurrentNamespace = namespace.equals(currentNamespace);
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean hasCurrentNamespace() {
        return hasCurrentNamespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return type;
    }
}
