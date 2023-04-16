package ch.bailu.gtk.lib.bridge;

import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Int64;
import ch.bailu.gtk.type.Pointer;

public class Bytes extends ch.bailu.gtk.glib.Bytes {
    public Bytes(PointerContainer pointer) {
        super(pointer);
    }

    public Bytes(byte[] bytes) {
        super(new ch.bailu.gtk.type.Bytes(bytes), bytes.length);
    }

    public Bytes(ch.bailu.gtk.type.Bytes bytes) {
        super(createFromWrapper(bytes));
    }


    private static PointerContainer createFromWrapper(ch.bailu.gtk.type.Bytes bytes) {
        bytes.throwIfNull();
        if (bytes.getSize() < 1) {
            bytes.throwNullPointerException("size == " + bytes.getSize());
        }

        return new ch.bailu.gtk.glib.Bytes(new Pointer(bytes.cast()), bytes.getSize()).cast();
    }


    private ch.bailu.gtk.type.Bytes toBytesWrapper() {
        var size = new Int64();
        var data = getData(size);
        var result = new ch.bailu.gtk.type.Bytes(new PointerContainer(data.asCPointer()), (int) size.get());
        size.destroy();
        return result;
    }


    public byte[] toBytes(int start, int end) {
        return toBytesWrapper().toBytes(start, end);
    }

    public byte[] toBytes() {
        return toBytesWrapper().toBytes();
    }

    public byte getByte(int i) {
        return toBytesWrapper().getByte(i);
    }
}
