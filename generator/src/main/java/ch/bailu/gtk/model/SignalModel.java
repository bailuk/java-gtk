package ch.bailu.gtk.model;

import ch.bailu.gtk.converter.JavaNames;
import ch.bailu.gtk.tag.SignalTag;

public class SignalModel extends Model {
    private String name;
    private String jName;

    public SignalModel(SignalTag signal) {
        name = signal.getName();
        jName = JavaNames.toJavaSignalName(signal.getName());
    }

    public String getName() {
        return name;
    }

    public String getJavaName() {
        return jName;
    }
}
