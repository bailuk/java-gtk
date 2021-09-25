package ch.bailu.gtk.wrapper;

public class ImpInt {
    public static native long createInt(int value);
    public static native void set(long cPointer, int i);
    public static native int get(long cPointer);
}
