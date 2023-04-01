package ch.bailu.gtk.type;

import java.util.Objects;

public interface PointerCastInterface {
    static void throwIfNull(PointerInterface pointer) {
        Objects.requireNonNull(pointer, "pointer == null");
        pointer.throwIfNull();
    }

    static long asCPointerNotNull(PointerInterface pointer) {
        throwIfNull(pointer);
        return pointer.asCPointer();
    }

    static long asCPointer(PointerInterface pointer) {
        if (pointer == null) {
            return 0;
        } else {
            return pointer.asCPointer();
        }
    }

    static long asCPointer(com.sun.jna.Pointer p) {
        return com.sun.jna.Pointer.nativeValue(p);
    }

    static com.sun.jna.Pointer asJnaPointer(PointerInterface p) {
        return asJnaPointer(p.asCPointer());
    }


    static com.sun.jna.Pointer asJnaPointer(long p) {
        return new com.sun.jna.Pointer(p);
    }

    static Pointer asPointer(com.sun.jna.Pointer jnaPointer) {
        return new Pointer(cast(jnaPointer));
    }

    static Pointer asPointer(long cPointer) {
        return new Pointer(cast(cPointer));
    }

    static PointerContainer cast(long p) {
        return new PointerContainer(p);
    }

    static PointerContainer cast(com.sun.jna.Pointer p) {
        return new PointerContainer(asCPointer(p));
    }
}
