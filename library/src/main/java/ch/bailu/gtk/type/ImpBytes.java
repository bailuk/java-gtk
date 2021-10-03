package ch.bailu.gtk.type;

public class ImpBytes {
    public static native long createBytes(byte[] bytes);
    public static native byte getByte(long cPointer, int index);
}
