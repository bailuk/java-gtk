package ch.bailu.gtk.type;

public class CPointer implements CPointerInterface {
    public final static CPointer NULL = new CPointer(0);

    private final long pointer;
    
    public CPointer(long pointer) {
        this.pointer = pointer;
    }

    @Override
    public final long getCPointer() {
        return pointer;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(pointer).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CPointer) {
            return ((CPointer) obj).pointer == pointer;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(pointer);
    }


    @Override
    public final boolean isNotNull() {
        return (pointer != 0);
    }

    @Override
    public final boolean isNull() {
        return (pointer == 0);
    }
}
