package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

public class Imp {
    public static long createPointerArray(long[] pointers) {
        long size = (long) pointers.length * Long.BYTES;
        Pointer result = Glib.malloc(size);
        Pointer.asJnaPointer(result).write(0, pointers, 0, pointers.length);
        return result.asCPointer();
    }

    public static long allocate(long size) {
        var result = Glib.malloc0(size);
        return result.asCPointer();
    }
}
