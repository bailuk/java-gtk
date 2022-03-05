package ch.bailu.gtk.type;

public class ImpStr {
    public static String toString(long pointer) {
        return Pointer.toJnaPointer(pointer).getString(0);
    }
}
