package ch.bailu.gtk.type;

public class Str extends Pointer {
    public static final Str NULL = new Str(0);
    private int size;

    public Str(long pointer) {
        super(pointer);

        if (pointer == 0) {
            size = 0;
        } else {
            size = -1;
        }
    }

    public Str(String str) {
        super(ch.bailu.gtk.type.ImpStr.createStr(str));

        if (getCPointer() != 0) {
            size = str.length()+1;
        } else {
            size = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public void destroy() {
        if (size > 0) {
            ch.bailu.gtk.type.ImpUtil.destroy(getCPointer());
            size = 0;
        }
    }

    @Override
    public String toString() {
        String result = "";

        if (getCPointer() != 0) {
            result = ch.bailu.gtk.type.ImpStr.toString(getCPointer());
        }
        return result;
    }
}
