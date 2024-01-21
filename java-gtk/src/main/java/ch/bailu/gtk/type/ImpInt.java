package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

class ImpInt {

    public static long createInt(int value) {
        var result = Glib.malloc(Integer.BYTES);
        result.asJnaPointer().setInt(0, value);
        return result.asCPointer();
    }

    public static void set(long cPointer, int value) {
        Pointer.asJnaPointer(cPointer).setInt(0, value);
    }

    public static int get(long cPointer) {
        return Pointer.asJnaPointer(cPointer).getInt(0);
    }


    public static long createLong(long value) {
        var result = Glib.malloc(Long.BYTES);
        result.asJnaPointer().setLong(0, value);
        return result.asCPointer();
    }

    public static void setLong(long cPointer, long value) {
        Pointer.asJnaPointer(cPointer).setLong(0, value);
    }

    public static long getLong(long cPointer) {
        return Pointer.asJnaPointer(cPointer).getLong(0);
    }
}
