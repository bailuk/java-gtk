package ch.bailu.gtk.type;


import java.util.Objects;

public class Pointer extends Type implements CPointerInterface {

    public final static Pointer NULL = new Pointer(CPointer.NULL);

    private final CPointer pointer;

    public Pointer(CPointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public final long getCPointer() {
        return pointer.getCPointer();
    }

    public final CPointer getCPointerWrapper() {
        return pointer;
    }

    @Override
    public int hashCode() {
        return pointer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Pointer) {
            return Objects.equals(((Pointer)obj).pointer, pointer);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }


    public final void throwNullPointerException(String msg) {
        final String name = this.getClass().getCanonicalName();
        msg = name + ": " + msg;
        System.out.println(msg);
        throw new NullPointerException(msg);
    }

    public final void throwIfNull() {
        if (pointer.isNull()) {
            throwNullPointerException("pointer == 0");
        }
    }

    @Override
    public final boolean isNotNull() {
        return pointer.isNotNull();
    }

    @Override
    public final boolean isNull() {
        return pointer.isNull();
    }
}
