package ch.bailu.gtk.type;

class ImpStr {
    public static String toString(long pointer) {
        return Pointer.asJnaPointer(pointer).getString(0);
    }
}
