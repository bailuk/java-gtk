package ch.bailu.gtk.bridge;

class ImpBytes {
    public static native long createFromWrapper(long ponter, int size);
    public static native long createFromArray(byte[] bytes);

}
