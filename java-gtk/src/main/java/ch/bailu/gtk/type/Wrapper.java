package ch.bailu.gtk.type;

public abstract class Wrapper extends Pointer {
    public Wrapper(CPointer pointer) {
        super(pointer);
    }

    public abstract void destroy();
}
