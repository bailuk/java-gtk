package ch.bailu.gtk.wrapper;

public class StrImp {
    public static native long createStr(String str);
    public static native void destroy(long cPointer);
}
