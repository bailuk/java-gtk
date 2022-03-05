package ch.bailu.gtk.type;

public class ImpUtil {
    public static long createPointerArray(long[] pointers) {
        long size = (long) pointers.length * Long.BYTES;
        long result = CLib.API().malloc(size);
        Pointer.toJnaPointer(result).write(0, pointers, 0, pointers.length);
        return result;
    }

    public static void destroy(long cPointer) {
        CLib.API().free(cPointer);
    }
}
