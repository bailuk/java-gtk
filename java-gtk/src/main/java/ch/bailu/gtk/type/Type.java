package ch.bailu.gtk.type;

public abstract class Type {
    public static void throwIfNull(Pointer pointer) {
        if (pointer == null) {
            throw new NullPointerException("pointer == null");
        }
        pointer.throwIfNull();
    }


    public static long toCPointerNotNull(Pointer pointer) {
        throwIfNull(pointer);
        return pointer.getCPointer();
    }

    public static long toCPointer(Pointer pointer) {
        if (pointer == null) {
            return 0;
        } else {
            return pointer.getCPointer();
        }
    }
}
