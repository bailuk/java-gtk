package ch.bailu.gtk.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.converter.NamespaceTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.tag.NamedTag;
import ch.bailu.gtk.tag.NamespaceTag;


public class NameSpaceModel extends Model implements NamespaceInterface {

    private List<String> includes = new ArrayList<>();

    private final String namespace;


    public NameSpaceModel(NamespaceTag namespace) {
        this.namespace = namespace.getName().toLowerCase();

        for (NamedTag i : namespace.getIncludes()) {
            includes.add(i.getName());
        }

        setSupported("Namespace", NamespaceTable.instance().contains(this.namespace));
    }

    public NameSpaceModel() {
        this.namespace = "";
        setSupported("",true);
    }


    public NameSpaceModel(NamespaceType type) {
        namespace = type.getNamespace();
        setSupported("Namespace", NamespaceTable.instance().contains(this.namespace));
    }

/*
    public static String expandNamespace(String className) {
        String[] names = className.split("\\.");

        if (names.length > 1) {
            className = Configuration.BASE_NAME_SPACE_DOT + names[0].toLowerCase() + "." + names[1];
        }
        return className;
    }

    public static String expandOrAddNamespace(String className) {
        String[] names = className.split("\\.");

        if (names.length > 1) {
            className = Configuration.BASE_NAME_SPACE_DOT + names[0].toLowerCase() + "." + names[1];
        } else {
            className = Configuration.BASE_NAME_SPACE_DOT + className;
        }
        return className;
    }*/


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

    public String getHeaderFileBase() {
        return Configuration.HEADER_FILE_BASE + namespace + "_";
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
