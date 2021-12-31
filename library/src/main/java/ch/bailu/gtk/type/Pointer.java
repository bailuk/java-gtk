package ch.bailu.gtk.type;


import java.util.Objects;

public class Pointer extends Type implements CPointerInterface {

    public final static Pointer NULL = new Pointer(CPointer.NULL);

    private final CPointer pointer;

    /**
     * Casting constructor to access another interface.
     *
     * @see ch.bailu.gtk.type.Pointer#cast()
     *
     * @param pointer Wraps a C pointer of a GTK class or record
     */
    public Pointer(CPointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public final long getCPointer() {
        return pointer.getCPointer();
    }

    /**
     * Pass the return value of this function to the casting
     * constructor of any class derived from Pointer
     *
     * @see ch.bailu.gtk.type.Pointer#Pointer(CPointer)
     * @see ch.bailu.gtk.type.Pointer
     *
     * @return CPointer (wraps a C pointer of a GTK class or record)
     */
    public final CPointer cast() {
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
