package ch.bailu.gtk.wrapper;

public class BytesImp {
    public static native long createBytes(byte[] bytes);
    public static native byte getByte(long cPointer, int index);
}
