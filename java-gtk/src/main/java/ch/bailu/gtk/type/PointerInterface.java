package ch.bailu.gtk.type;

public interface PointerInterface {
    long asCPointer();

    default boolean isNull() {
        return asCPointer() == 0;
    }
    default boolean isNotNull() {
        return asCPointer() != 0;
    }

    default void throwIfNull() {
        Type.throwIfNull(this);
    }

    default long asCPointerNotNull() {
        throwIfNull();
        return asCPointer();
    }

    default com.sun.jna.Pointer asJnaPointer() {
        return Type.asJnaPointer(asCPointer());
    }

    default PointerContainer cast() {
        return new PointerContainer(asCPointer());
    }

    default Pointer asPointer() {
        return new Pointer(cast());
    }
}
