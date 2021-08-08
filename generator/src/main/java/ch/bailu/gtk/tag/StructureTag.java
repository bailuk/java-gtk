package ch.bailu.gtk.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Record, interface, class
 */
public class StructureTag extends NamedTag {

    private String parent;
    private final String type;


    private final List<NamedTag> implementsList = new ArrayList<>(3);
    private final List<MethodTag> constructors = new ArrayList<>(3);

    private final List<MethodTag> virtuals = new ArrayList<>(3);
    private final List<MethodTag> methods = new ArrayList<>(3);

    private final List<SignalTag> signals = new ArrayList<>(3);

    public StructureTag(Tag parent, String type) {
        super(parent);
        this.type = type;
    }

    @Override
    public Tag getChild(String name, String prefix) {
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
            return add(signals, new SignalTag(this));
        }

        return ignore();
    }

    @Override
    public void end() throws IOException {
        getBuilder().buildStructure(this);
    }


    @Override
    public void setAttribute(String name, String value) {
        if ("parent".equalsIgnoreCase(name)) {
            this.parent = value;
        } else {
            super.setAttribute(name, value);
        }
    }


    public String getParentName() {
        return parent;
    }
    public String getType() {
        return type;
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

    public List<SignalTag> getSignals() {
        return signals;
    }
}
