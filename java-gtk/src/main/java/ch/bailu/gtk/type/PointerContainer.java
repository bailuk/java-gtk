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
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return cPointer == 0;
        } else if (obj instanceof PointerInterface) {
            return ((PointerInterface)obj).asCPointer() == cPointer;
        } else if (obj instanceof Number) {
            return cPointer == ((Number) obj).longValue();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(cPointer);
    }
}
