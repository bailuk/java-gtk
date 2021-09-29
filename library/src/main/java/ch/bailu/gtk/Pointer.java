package ch.bailu.gtk;


public class Pointer {

    private final long pointer;

    public Pointer(long pointer) {
        this.pointer = pointer;
    }

    public long getCPointer() {
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


    public void throwNullPointerException(String msg) {
        final String name = this.getClass().getCanonicalName();
        msg = name + ": " + msg;
        System.out.println(msg);
        throw new NullPointerException(msg);
    }

    public void throwIfNull() {
        if (pointer == 0) {
            throwNullPointerException("pointer == 0");
        }
    }
}
