package ch.bailu.gtk.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NamespaceTag extends NamedTag {
    public NamespaceTag(Tag parent) {
        super(parent);
    }


    private List<NamedTag> includes = new ArrayList<>();
    private List<MethodTag> functions = new ArrayList<>();

    private NamedTag _package = new NamedTag(this);

    @Override
    public void started() throws IOException {
        getBuilder().buildNamespaceStart(this);
    }


    @Override
    public void end() throws IOException {
        getBuilder().buildNamespaceEnd(this);
    }

    @Override
    public Tag getChild(String name, String prefix) {
        if ("class".equals(name)) {
            return new StructureTag(this, name);
        }

        if ("record".equals(name)) {
            return new StructureTag(this, name);
        }

        if ("interface".equals(name)) {
            return new StructureTag(this, name);
        }

        if ("enumeration".equals(name)) {
            return new EnumerationTag(this);
        }

        if ("bitfield".equals(name)) {
            return new EnumerationTag(this);
        }

        if ("alias".equals(name)) {
            return new AliasTag(this);
        }

        if ("function".equals(name)) {
            MethodTag f = new MethodTag(this);
            functions.add(f);
            return f;
        }

        return ignore();
    }

    public List<NamedTag> getIncludes() {
        return includes;
    }

    public List<MethodTag> getFunctions() {
        return functions;
    }

    public String getPackage() {
        return _package.getName();
    }

    public Tag addInclude(NamedTag include) {
        includes.add(include);
        return include;
    }

    public Tag addPackage(NamedTag pkg) {
        _package = pkg;
        return pkg;
    }
}
