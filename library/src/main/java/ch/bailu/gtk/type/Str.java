package ch.bailu.gtk.type;

public class Str extends Bytes {
    public Str(long pointer) {
        super(pointer);
    }

    public Str(String str) {
        super(str.getBytes());
    }

    @Override
    public String toString() {
        if (getCPointer() == 0 || getSize() < 1) {
            return "";
        }

        return new String(toBytes());
    }
}
