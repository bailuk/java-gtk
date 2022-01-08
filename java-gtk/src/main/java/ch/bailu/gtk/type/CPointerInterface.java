package ch.bailu.gtk.type;

public interface CPointerInterface {
    boolean isNull();
    boolean isNotNull();
    long getCPointer();
}
