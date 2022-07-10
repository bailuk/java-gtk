package ch.bailu.gtk.type;

public abstract class Array extends Wrapper {
    private int length;
    private final int bytes;

    public Array(CPointer pointer, int bytes, int length) {
        super(pointer);
        this.bytes = bytes;
        if (pointer.isNull()) {
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
        if (isLimitKnown() && !isWithinLimit(index)) {
            throw new IndexOutOfBoundsException("length: " + length + " index: " + index);
        }
    }

    private boolean isWithinLimit(int index) {
        return index < length && index > -1;
    }

    private boolean isLimitKnown() {
        return length != -1;
    }
}
