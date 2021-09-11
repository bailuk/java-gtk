package ch.bailu.gtk.tag;

import java.util.List;

public class MethodTag extends NamedTag {
    private final ParameterTag returnValue =  new ParameterTag(this);

    private final TagList<ParameterTag> parameters = new TagList<>();
    private final TagList<ParameterTag> instanceParameters = new TagList<>();

    private String identifier = "";

    private boolean deprecated = false;
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
                return parameters.addTag(new ParameterTag(this));
            }
            if ("instance-parameter".equals(name)) {
                return instanceParameters.addTag(new ParameterTag(this));
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
        } else if ("deprecated".equals(name)) {
            deprecated = "1".equals(value);
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

    public boolean isDeprecated() {
        return deprecated;
    }

    public boolean throwsError() {
        return throwsError;
    }

}
