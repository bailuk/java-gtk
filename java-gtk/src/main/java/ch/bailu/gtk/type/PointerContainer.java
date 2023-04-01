package ch.bailu.gtk.type;

public class PointerContainer implements PointerInterface {
    public final static PointerContainer NULL = new PointerContainer(0);

    private final long cPointer;

    public PointerContainer(long pointer) {
        this.cPointer = pointer;
    }

    @Override
    public final long asCPointer() {
        return cPointer;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(cPointer).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointerInterface) {
            return ((PointerInterface)obj).asCPointer() == cPointer;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(cPointer);
    }
}
