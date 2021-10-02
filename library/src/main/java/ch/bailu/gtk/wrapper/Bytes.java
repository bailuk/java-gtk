package ch.bailu.gtk.wrapper;

public class Bytes extends Ary {


    public Bytes(long pointer) {
        super(pointer, 1, 0);
    }

    public Bytes(byte[] bytes) {
        super(createBytes(bytes), 1, bytes.length);

    }

    private static long createBytes(byte[] bytes) {
        if (bytes.length == 0) {
            return 0;
        }
        return ImpBytes.createBytes(bytes);
    }

    public byte getByte(int index) {
        checkLimit(index);
        return ImpBytes.getByte(getCPointer(), index);
    }
}
