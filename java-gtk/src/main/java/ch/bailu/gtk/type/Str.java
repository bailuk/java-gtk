package ch.bailu.gtk.type;

public class Str extends Bytes {
    public final static Str NULL = new Str(PointerContainer.NULL);

    public Str(PointerContainer pointer) {
        super(pointer);
    }

    /**
     * Allocate a null terminated string in the c heap.
     * Copy str to allocated string.
     * @param str Java string to copy to c heap
     */
    public Str(String str) {
        super(str.getBytes(), (byte)0);
    }

    @Override
    public String toString() {
        if (asCPointer() == 0) {
            return "";
        }
        return ImpStr.toString(asCPointer());
    }
}
