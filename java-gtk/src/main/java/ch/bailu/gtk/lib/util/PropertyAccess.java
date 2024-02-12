package ch.bailu.gtk.lib.util;

import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Record;
import ch.bailu.gtk.type.Str;


public class PropertyAccess {

    public static Value initValue(long typeID) {
        return new Value(new Record(5).cast()).init(typeID);
    }

    public static Value toValue(String str) {
        var result = initValue(Str.getTypeID());
        result.setString(str);
        return result;
    }

    public static void setStringProperty(Object obj, String name, String value) {
        obj.setProperty(name, toValue(value));
    }

    public static String getStringProperty(Object obj, String name) {
        var str = getStrProperty(obj, name);
        var result = str.toString();
        str.destroy();
        return result;
    }

    public static Value toValue(Str str) {
        var result = initValue(Str.getTypeID());
        result.setString(str);
        return result;
    }

    public static void setStrProperty(Object obj, String name, Str value) {
        obj.setProperty(name, toValue(value));
    }

    public static Str getStrProperty(Object obj, String name) {
        var value = initValue(Str.getTypeID());
        obj.getProperty(name, value);
        return value.getString();
    }

    public static Value toValue(int value) {
        var result = initValue(Int.getTypeID());
        result.setInt(value);
        return result;
    }

    public static Value toValue(Object obj) {
        var result = initValue(Object.getTypeID());
        result.setObject(obj);
        return result;
    }

    public static Pointer getObjectProperty(Object obj, String name) {
        var value = initValue(Object.getTypeID());
        obj.getProperty(name, value);
        return value.getObject();
    }

    public static int getIntProperty(Object obj, String name) {
        var value = initValue(Int.getTypeID());
        obj.getProperty(name, value);
        return value.getInt();
    }

    public static void setIntProperty(Object obj, String name, int value) {
        obj.setProperty(name, toValue(value));
    }

    public static boolean getBooleanProperty(Object obj, String name) {
        var value = initValue(Int.getBooleanTypeID());
        obj.getProperty(name, value);
        return value.getBoolean();
    }

    public static void setBooleanProperty(Object obj, String name, boolean value) {
        obj.setProperty(name, toValue(value));
    }

    private static Value toValue(boolean value) {
        var result = initValue(Int.getBooleanTypeID());
        result.setBoolean(value);
        return result;
    }
}
