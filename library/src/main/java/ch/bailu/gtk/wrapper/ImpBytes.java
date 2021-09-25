package ch.bailu.gtk.wrapper;

public class ImpBytes {
    public static native long createBytes(byte[] bytes);
    public static native byte getByte(long cPointer, int index);
}
