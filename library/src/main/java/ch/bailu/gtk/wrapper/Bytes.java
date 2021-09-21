package ch.bailu.gtk.wrapper;

import ch.bailu.gtk.Pointer;

public class Bytes extends Pointer {

    private int size = 0;


    public Bytes(long pointer) {
        super(pointer);
    }

    public Bytes(byte[] bytes) {
        super(BytesImp.createBytes(bytes));

        if (getCPointer() != 0) {
            size = bytes.length;
        } else {
            size = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public byte getByte(int index) {
        checkLimit(index);
        return BytesImp.getByte(getCPointer(), index);
    }

    public void destroy() {
        if (size != 0) {
            BytesImp.destroy(getCPointer());
            size = 0;
        }
    }

    private void checkLimit(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("size: " + size + " index: " + index);
        }
    }
}
