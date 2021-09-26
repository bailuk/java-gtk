package ch.bailu.gtk.wrapper;

public class ImpDbl {
    public static native long createDbl(double value);
    public static native void set(long pointer, double value);
    public static native double get(long poinger);
}
