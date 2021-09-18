package ch.bailu.gtk;

public class BytesImp {
    public static native long createBytes(byte[] bytes);

    public static native byte getByte(long cPointer, int index);

    public static native void destroy(long cPointer);
}
