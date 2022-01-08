package ch.bailu.gtk.type;

public class ImpUtil {
    public static native long createPointerArray(long[] pointers);
    public static native void destroy(long cPointer);

}
