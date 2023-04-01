package ch.bailu.gtk.type;

public class Bytes extends Array {

    public Bytes(PointerContainer pointer, int size) {
        super(pointer, 1, size);
    }

    public Bytes(PointerContainer pointer) {
        super(pointer, 1, -1);
    }

    public Bytes(byte[] bytes) {
        super(createBytes(bytes), 1, bytes.length);

    }

    private static PointerContainer createBytes(byte[] bytes) {
        if (bytes.length == 0) {
            return PointerContainer.NULL;
        }
        return new PointerContainer(ImpBytes.createBytes(bytes));
    }

    public byte getByte(int index) {
        checkLimit(index);
        return ImpBytes.getByte(asCPointer(), index);
    }

    public void setByte(int index, byte value) {
        checkLimit(index);
        ImpBytes.setByte(asCPointer(), index, value);
    }

    /**
     * Write value to the next four bytes starting at index
     * @param index index in byte array for the first byte
     * @param value integer (four bytes) to write into byte array
     */
    public void setInt(int index, int value) {
        checkLimit(index);
        checkLimit(index+3);
        ImpBytes.setInt(asCPointer(), index, value);
    }

    public byte[] toBytes() {
        return toBytes(0, getSize()-1);
    }

    public byte[] toBytes(int start, int end) {
        final int size = end - start + 1;
        checkLimit(start);
        checkLimit(end);
        if (size <= 0) return new byte[]{};
        return ImpBytes.toBytes(asCPointer(), start, size);
    }
}
