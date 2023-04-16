package ch.bailu.gtk.type;

public abstract class Array extends Wrapper {
    private final static int SIZE_UNKNOWN = -1;


    private int length;
    private final int bytes;

    public Array(PointerContainer pointer, int bytes, int length) {
        super(pointer);
        this.bytes = bytes;
        if (pointer.isNull()) {
            this.length = 0;
        } else {
            this.length = length;
        }
    }

    @Override
    public void destroy() {
        if (length != 0) {
            ImpUtil.destroy(asCPointer());
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
        if (isSizeKnown() && !isWithinLimit(index)) {
            throw new IndexOutOfBoundsException("length: " + length + " index: " + index);
        }
    }

    private boolean isWithinLimit(int index) {
        return index < length && index >= 0;
    }

    public boolean isSizeKnown() {
        return length != SIZE_UNKNOWN;
    }
}
