package ch.bailu.gtk.type;

import java.util.Objects;

public abstract class Type {
    public static void throwIfNull(Pointer pointer) {
        Objects.requireNonNull(pointer, "pointer == null");
        pointer.throwIfNull();
    }

    public static long toCPointerNotNull(Pointer pointer) {
        // TODO throwIfNull(pointer);
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
