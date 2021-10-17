package ch.bailu.gtk.type;

public class Bytes extends Array {



    public Bytes(long pointer, int size) {
        super(pointer, 1, size);
    }

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

    public byte[] toBytes() {
        return toBytes(0, getSize()-1);
    }

    public byte[] toBytes(int start, int end) {
        final int size = end - start + 1;
        checkLimit(start);
        checkLimit(end);
        if (size <= 0) return new byte[]{};
        return ImpBytes.toBytes(getCPointer(), start, size);
    }
}
