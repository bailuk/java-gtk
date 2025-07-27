package ch.bailu.gtk.gtk;

import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Record;
import ch.bailu.gtk.type.Str;

public class ValueFactory {
    // TODO: This should come from SizeTable.kt
    private static final int VALUE_SIZE = 24;

    public static Value initValue(long typeID) {
        return new Value(new Record(VALUE_SIZE).cast()).init(typeID);
    }

    public static Value toValue(Str str) {
        var result = initValue(Str.getTypeID());
        result.setString(str);
        return result;
    }

    public static Value toValue(String str) {
        var result = initValue(Str.getTypeID());
        result.setString(str);
        return result;
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

    public static Value toValue(boolean value) {
        var result = initValue(Int.getBooleanTypeID());
        result.setBoolean(value);
        return result;
    }
}
