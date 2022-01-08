package ch.bailu.gtk.type;

public class ImpInt {
    public static native long createInt(int value);
    public static native void set(long cPointer, int value);
    public static native int get(long cPointer);

    public static native long createLong(long value);
    public static native void setLong(long cPointer, long value);
    public static native long getLong(long cPointer);
}
