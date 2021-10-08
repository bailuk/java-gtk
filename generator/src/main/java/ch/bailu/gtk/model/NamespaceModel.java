package ch.bailu.gtk.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.table.NamespaceTable;
import ch.bailu.gtk.converter.RelativeNamespaceType;
import ch.bailu.gtk.tag.NamedWithDocTag;
import ch.bailu.gtk.tag.NamespaceTag;


public class NamespaceModel extends Model implements NamespaceInterface {

    private List<String> includes = new ArrayList<>();

    private final String namespace;


    public NamespaceModel(NamespaceTag namespace) {
        this.namespace = namespace.getName().toLowerCase();

        for (NamedWithDocTag i : namespace.getIncludes()) {
            includes.add(i.getName());
        }

        setSupported("Namespace", NamespaceTable.instance().contains(this.namespace));
    }

    public NamespaceModel() {
        this.namespace = "";
        setSupported("",true);
    }


    public NamespaceModel(RelativeNamespaceType type) {
        namespace = type.getNamespace();
        setSupported("Namespace", NamespaceTable.instance().contains(this.namespace));
    }


    public File getJavaSourceDirectory() {
        File result = new File(Configuration.instance().getJavaBaseDir(), namespace);
        return result;
    }

    public File getCSourceDirectory() {
        return new File(Configuration.instance().getCBaseDir());
    }

    public List<String> getIncludes() {
        return includes;
    }


    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getFullNamespace() {
        if ("".equals(namespace)) {
            return Configuration.BASE_NAME_SPACE_NODOT;
        } else {
            return Configuration.BASE_NAME_SPACE_DOT + namespace;
        }
    }
}
