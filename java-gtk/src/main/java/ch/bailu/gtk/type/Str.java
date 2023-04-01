package ch.bailu.gtk.type;

public class Str extends Bytes {
    public final static Str NULL = new Str(PointerContainer.NULL);

    public Str(PointerContainer pointer) {
        super(pointer);
    }

    public Str(String str) {
        super(strToBytes(str));
    }

    /**
     *  String.getBytes() does not return a 0 terminated result.
     *  Therefore the string needs to be copied twice
     */
    private static byte[] strToBytes(String str) {
        byte[] src=str.getBytes();
        byte[] dst=new byte[src.length+1];
        System.arraycopy(src, 0, dst, 0, src.length);
        return dst;
    }

    @Override
    public String toString() {
        if (asCPointer() == 0) {
            return "";
        }
        return ImpStr.toString(asCPointer());
    }
}
