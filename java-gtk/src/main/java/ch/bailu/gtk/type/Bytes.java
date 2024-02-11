package ch.bailu.gtk.type;

public class Bytes extends Array {

    /**
     * Create a reference to a byte array of specific size
     * @param pointer pointer to byte array
     * @param size size of byte array
     */
    public Bytes(PointerContainer pointer, int size) {
        super(pointer, 1, size);
    }

    /**
     * Create a reference to a byte array of unknown size
     * @param pointer pointer to byte array
     */
    public Bytes(PointerContainer pointer) {
        super(pointer, 1, -1);
    }

    /**
     * Allocates a byte array in the c heap  of size bytes.length.
     * Copies bytes to allocated array.
     * @param bytes bytes to copy to c heap
     */
    public Bytes(byte[] bytes) {
        super(createBytes(bytes), 1, bytes.length);
    }

    /**
     * Allocates a byte array of size bytes.length + 1) in the c heap.
     * Copies bytes to allocated array and adds one byte at the end.
     * @param bytes bytes to copy to the allocated array
     * @param terminate byte to add at the end of the allocated array
     */
    public Bytes(byte[] bytes, byte terminate) {
        super(createBytes(bytes, terminate), 1, bytes.length + 1);
    }

    /**
     * Allocates a byte array in the c heap of a specific size.
     * Array is initialized with zeros.
     * @param size size of new array
     */
    public Bytes(int size) {
        super(createBytes(size), 1, size);
    }

    private static PointerContainer createBytes(byte[] bytes, byte terminate) {
        return new PointerContainer(ImpBytes.createBytes(bytes, terminate));
    }

    private static PointerContainer createBytes(byte[] bytes) {
        return new PointerContainer(ImpBytes.createBytes(bytes));
    }

    private static PointerContainer createBytes(int length) {
        if (length == 0) {
            return PointerContainer.NULL;
        }
        return new PointerContainer(ImpBytes.createBytes(length));
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
