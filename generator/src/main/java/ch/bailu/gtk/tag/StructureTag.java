package ch.bailu.gtk.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Record, interface, class
 */
public class StructureTag extends NamedTag {

    private String parent;
    private final String structureType;
    private boolean disguised = false;

    private String type = "";


    private final TagList<NamedTag> implementsList = new TagList<>();
    private final TagList<MethodTag> constructors = new TagList<>();
    private final TagList<MethodTag> functions = new TagList<>();


    private final TagList<MethodTag> virtuals = new TagList<>();
    private final TagList<MethodTag> methods = new TagList<>();

    private final TagList<MethodTag> signals = new TagList<>();
    private final TagList<ParameterTag> fields = new TagList<>();

    public StructureTag(Tag parent, String structureType) {
        super(parent);
        this.structureType = structureType;
    }

    @Override
    public Tag getChild(String name, String prefix) {
        if ("field".equals(name)) {
            return fields.addTag(new ParameterTag(this));
        }
        if ("implements".equals(name)) {
             return implementsList.addTag(new NamedTag(this));
        }

        if ("constructor".equals(name)) {
            return constructors.addTag(new MethodTag(this));
        }

        if ("virtual-method".equals(name)) {
            return virtuals.addTag(new MethodTag(this));
        }

        if ("method".equals(name)) {
            return methods.addTag(new MethodTag(this));
        }

        if ("signal".equals(name)) {
            return signals.addTag(new MethodTag(this));
        }

        if ("function".equals(name)) {
            MethodTag f = new MethodTag(this);
            functions.add(f);
            return f;
        }

        return ignore();
    }

    @Override
    public void end() throws IOException {
        if (this.isDisguised() == false) {
            getBuilder().buildStructure(this);
        }
    }


    @Override
    public void setAttribute(String name, String value) {
        if ("parent".equals(name)) {
            this.parent = value;
        } else if ("disguised".equals(name)) {
            disguised = "1".equals(value);
        } else if ("type".equals(name)) {
            this.type = value;
        } else {
            super.setAttribute(name, value);
        }
    }


    public String getParentName() {
        return parent;
    }
    public String getStructureType() {
        return structureType;
    }
    public List<NamedTag> getImplementsList() {
        return implementsList;
    }

    public List<MethodTag> getConstructors() {
        return constructors;
    }

    public List<MethodTag> getVirtuals() {
        return virtuals;
    }

    public List<MethodTag> getMethods() {
        return methods;
    }

    public List<MethodTag> getSignals() {
        return signals;
    }

    public List<ParameterTag> getFields() {
        return fields;
    }

    public List<MethodTag> getFunctions() {
        return functions;
    }

    public boolean isDisguised() {
        return disguised;
    }

    public String getType() {
        return type;
    }
}
