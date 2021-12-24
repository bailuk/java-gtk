package ch.bailu.gtk.bridge;

public class ImpListIndex {
    public static native long create();
    public static native void setSize(long cPointer, long size);
    public static native long getPosition(long cPointer);
    public static native long getSize(long cPointer);
    public static native void setPosition(long cPointer, long position);
}
