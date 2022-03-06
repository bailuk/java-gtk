package ch.bailu.gtk.type;

import ch.bailu.jgtk.lib.CLib;

public class ImpInt {

    public static long createInt(int value) {
        long result = CLib.API().malloc(Integer.BYTES);
        Pointer.toJnaPointer(result).setInt(0, value);
        return result;
    }

    public static void set(long cPointer, int value) {
        Pointer.toJnaPointer(cPointer).setInt(0, value);
    }

    public static int get(long cPointer) {
        return Pointer.toJnaPointer(cPointer).getInt(0);
    }


    public static long createLong(long value) {
        long result = CLib.API().malloc(Long.BYTES);
        Pointer.toJnaPointer(result).setLong(0, value);
        return result;
    }

    public static void setLong(long cPointer, long value) {
        Pointer.toJnaPointer(cPointer).setLong(0, value);
    }

    public static long getLong(long cPointer) {
        return Pointer.toJnaPointer(cPointer).getLong(0);
    }
}
