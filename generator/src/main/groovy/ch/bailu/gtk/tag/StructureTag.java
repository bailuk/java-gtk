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


    private final List<NamedTag> implementsList = new ArrayList<>();
    private final List<MethodTag> constructors = new ArrayList<>();

    private final List<MethodTag> virtuals = new ArrayList<>();
    private final List<MethodTag> methods = new ArrayList<>();

    private final List<MethodTag> signals = new ArrayList<>();
    private final List<ParameterTag> fields = new ArrayList<>();

    public StructureTag(Tag parent, String structureType) {
        super(parent);
        this.structureType = structureType;
    }

    @Override
    public Tag getChild(String name, String prefix) {
        if ("field".equals(name)) {
            return add(fields, new ParameterTag(this));
        }
        if ("implements".equals(name)) {
             return add(implementsList, new NamedTag(this));
        }

        if ("constructor".equals(name)) {
            return add(constructors, new MethodTag(this));
        }

        if ("virtual-method".equals(name)) {
            return add(virtuals, new MethodTag(this));
        }

        if ("method".equals(name)) {
            return add(methods, new MethodTag(this));
        }

        if ("signal".equals(name)) {
            return add(signals, new MethodTag(this));
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

    public boolean isDisguised() {
        return disguised;
    }

    public String getType() {
        return type;
    }
}
