package ch.bailu.gtk.type;

import ch.bailu.gtk.type.Pointer;

public abstract class Ary extends Pointer {
    private int length;
    private final int bytes;

    public Ary(long pointer, int bytes, int length) {
        super(pointer);
        this.bytes = bytes;
        if (pointer == 0) {
            this.length = 0;
        } else {
            this.length = length;
        }
    }

    public void destroy() {
        if (length != 0) {
            ImpUtil.destroy(getCPointer());
            length = 0;
        }
    }

    public int getLength() {
        return length;
    }

    public int getSize() {
        if (length < 0) {
            return -1;
        }
        return length * bytes;
    }

    public void checkLimit(int index) {
        // -1 means unknown
        if (index >= length || index < -1) {
            throw new IndexOutOfBoundsException("length: " + length + " index: " + index);
        }
    }
}
