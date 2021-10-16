package ch.bailu.gtk.bridge;

import ch.bailu.gtk.type.Int64;
import ch.bailu.gtk.type.Pointer;

public class Bytes extends ch.bailu.gtk.glib.Bytes {
    public Bytes(long pointer) {
        super(pointer);
    }

    public Bytes(byte[] bytes) {
        super(ImpBytes.createFromArray(bytes));
    }

    public Bytes(ch.bailu.gtk.type.Bytes bytes) {
        super(createFromWrapper(bytes));
    }


    private static long createFromWrapper(ch.bailu.gtk.type.Bytes bytes) {
        bytes.throwIfNull();
        if (bytes.getSize() < 1) {
            bytes.throwNullPointerException("size == " + bytes.getSize());
        }
        return ImpBytes.createFromWrapper(bytes.getCPointer(), bytes.getSize());
    }

    public byte[] getBytes() {
        byte[] result = {};

        if (getSize() > 0) {
            Int64 size = new Int64();
            Pointer pointer = getData(size);

            if (size.get() > 0 && pointer.isNotNull()) {
                result = ch.bailu.gtk.type.ImpBytes.toBytes(pointer.getCPointer(), 0, (int) (size.get() - 1));
            }
            size.destroy();
        }

        return result;
    }
}
