package ch.bailu.gtk.type;

import java.util.Objects;

/**
 * Base Type for all c types and type like structures: Record, Class, Wrapper, Package, Interface...
 * Holds all static type cast functions
 */
public abstract class Type {
    public static void throwIfNull(PointerInterface pointer) {
        Objects.requireNonNull(pointer, "pointer == null");
        pointer.throwIfNull();
    }

    public static long asCPointerNotNull(PointerInterface pointer) {
        //TODO throwIfNull(pointer);
        return pointer.asCPointer();
    }

    public static long asCPointer(PointerInterface pointer) {
        if (pointer == null) {
            return 0;
        } else {
            return pointer.asCPointer();
        }
    }

    public static long asCPointer(com.sun.jna.Pointer p) {
        return com.sun.jna.Pointer.nativeValue(p);
    }

    public static com.sun.jna.Pointer asJnaPointer(PointerInterface p) {
        return asJnaPointer(p.asCPointer());
    }


    public static com.sun.jna.Pointer asJnaPointer(long p) {
        return new com.sun.jna.Pointer(p);
    }

    public static Pointer asPointer(com.sun.jna.Pointer jnaPointer) {
        return new Pointer(cast(jnaPointer));
    }

    public static Pointer asPointer(long cPointer) {
        return new Pointer(cast(cPointer));
    }

    public static PointerContainer cast(long p) {
        return new PointerContainer(p);
    }

    public static PointerContainer cast(com.sun.jna.Pointer p) {
        return new PointerContainer(asCPointer(p));
    }
}
