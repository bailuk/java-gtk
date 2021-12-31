package ch.bailu.gtk.bridge;

public class ImpListIndex {
    public static native long create();
    public static native void setSize(long cPointer, int size);
    public static native int getIndex(long cPointer);
    public static native int getSize(long cPointer);
    public static native void setIndex(long cPointer, int position);
}
