package ch.bailu.gtk.wrapper;

public class ImpUtil {
    public static native long createPointerArray(long[] pointers);
    public static native void destroy(long cPointer);

}
