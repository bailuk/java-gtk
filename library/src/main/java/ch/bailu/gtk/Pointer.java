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
}
