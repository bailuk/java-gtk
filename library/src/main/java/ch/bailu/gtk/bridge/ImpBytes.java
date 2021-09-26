package ch.bailu.gtk.bridge;

public class ImpBytes {
    public static native long createFromWrapper(long ponter, int size);
    public static native long createFromArray(byte[] bytes);
}
