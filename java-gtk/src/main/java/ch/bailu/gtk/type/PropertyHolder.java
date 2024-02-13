package ch.bailu.gtk.type;

import static ch.bailu.gtk.gtk.ValueFactory.initValue;
import static ch.bailu.gtk.gtk.ValueFactory.toValue;

import ch.bailu.gtk.gobject.Object;

public class PropertyHolder extends Object {

    public PropertyHolder(PointerContainer pointer) {
        super(pointer);
    }

    public void setStringProperty(String name, String value) {
        setProperty(name, toValue(value));
    }

    public String getStringProperty(String name) {
        var str = getStrProperty(name);
        var result = str.toString();
        str.destroy();
        return result;
    }

    public void setStrProperty(String name, Str value) {
        setProperty(name, toValue(value));
    }

    public Str getStrProperty(String name) {
        var value = initValue(Str.getTypeID());
        getProperty(name, value);
        return value.getString();
    }



    public Pointer getObjectProperty(String name) {
        var value = initValue(Object.getTypeID());
        getProperty(name, value);
        return value.getObject();
    }

    public int getIntProperty(String name) {
        var value = initValue(Int.getTypeID());
        getProperty(name, value);
        return value.getInt();
    }

    public void setIntProperty(String name, int value) {
        setProperty(name, toValue(value));
    }

    public boolean getBooleanProperty(String name) {
        var value = initValue(Int.getBooleanTypeID());
        getProperty(name, value);
        return value.getBoolean();
    }

    public void setBooleanProperty(String name, boolean value) {
        setProperty(name, toValue(value));
    }

    public void setObjectProperty(String name, Object value) {
        setProperty(name, toValue(value));
    }
}
