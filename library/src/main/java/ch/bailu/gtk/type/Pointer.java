package ch.bailu.gtk.type;


public class Pointer extends Type {

    public final static Pointer NULL = new Pointer(0);

    private final long pointer;

    public Pointer(long pointer) {
        this.pointer = pointer;
    }

    public final long getCPointer() {
        return pointer;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(pointer).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pointer) {
            return ((Pointer) obj).pointer == pointer;
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
        if (pointer == 0) {
            throwNullPointerException("pointer == 0");
        }
    }

    public final boolean isNotNull() {
        return (pointer != 0);
    }

    public final boolean isNull() {
        return (pointer == 0);
    }
}
