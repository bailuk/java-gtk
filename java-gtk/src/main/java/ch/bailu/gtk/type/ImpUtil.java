package ch.bailu.gtk.type;

import ch.bailu.gtk.lib.CLib;

public class ImpUtil {
    public static long createPointerArray(long[] pointers) {
        long size = (long) pointers.length * Long.BYTES;
        long result = CLib.INST().malloc(size);
        Pointer.toJnaPointer(result).write(0, pointers, 0, pointers.length);
        return result;
    }

    public static void destroy(long cPointer) {
        CLib.INST().free(cPointer);
    }
}
