package ch.bailu.gtk.type;

public class ImpBytes {
    public static native long createBytes(byte[] bytes);
    public static native byte getByte(long cPointer, int index);
    public static native byte[] toBytes(long cPointer, int start, int size);
    public static native int getSizeOfNullTerminatedSequence(long cPointer);
}
