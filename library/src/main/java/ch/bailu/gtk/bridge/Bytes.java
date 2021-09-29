package ch.bailu.gtk.bridge;

public class Bytes extends ch.bailu.gtk.glib.Bytes {
    public Bytes(long pointer) {
        super(pointer);
    }

    public Bytes(byte[] bytes) {
        super(ImpBytes.createFromArray(bytes));
    }

    public Bytes(ch.bailu.gtk.wrapper.Bytes bytes) {
        super(createFromWrapper(bytes));
    }


    private static long createFromWrapper(ch.bailu.gtk.wrapper.Bytes bytes) {
        bytes.throwIfNull();
        if (bytes.getSize() < 1) {
            bytes.throwNullPointerException("size == " + bytes.getSize());
        }
        return ImpBytes.createFromWrapper(bytes.getCPointer(), bytes.getSize());
    }
}
