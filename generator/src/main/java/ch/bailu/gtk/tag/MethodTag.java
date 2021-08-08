package ch.bailu.gtk.tag;

import java.util.ArrayList;
import java.util.List;

public class MethodTag extends NamedTag {
    private final ParameterTag returnValue =  new ParameterTag(this);

    private final List<ParameterTag> parameters = new ArrayList<>(3);
    private final List<ParameterTag> instanceParameters = new ArrayList<>(3);

    private String identifier = "";

    private boolean throwsError = false;

    public MethodTag(Tag parent) {
        super(parent);
    }


    private class Parameters extends Tag {
        public Parameters() {
            super(MethodTag.this);
        }

        @Override
        public Tag getChild(String name, String prefix) {
            if ("parameter".equals(name)) {
                return add(parameters, new ParameterTag(this));
            }
            if ("instance-parameter".equals(name)) {
                return add(instanceParameters, new ParameterTag(this));
            }
            return ignore();
        }
    }


    @Override
    public Tag getChild(String name, String prefix) {
        if ("return-value".equals(name)) {
            return returnValue;
        }

        if ("parameters".equals(name)) {
            return new Parameters();
        }

        return new IgnoreTag(this);
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("identifier".equals(name)) {
            identifier = value;
        } else if ("throws".equals(name)) {
            throwsError = "1".equals(value);
        } else {
            super.setAttribute(name, value);
        }
    }

    public List<ParameterTag> getParameters() {
        return parameters;
    }
    public ParameterTag getReturnValue() {
        return returnValue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean throwsError() {
        return throwsError;
    }
}
