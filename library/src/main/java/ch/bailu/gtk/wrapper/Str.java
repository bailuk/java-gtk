package ch.bailu.gtk.wrapper;

import ch.bailu.gtk.Pointer;

public class Str extends Pointer {
    public static final Str NULL = new Str(0);
    private int size = 0;

    public Str(long pointer) {
        super(pointer);
    }

    public Str(String str) {
        super(ImpStr.createStr(str));

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
        if (size != 0) {
            ImpUtil.destroy(getCPointer());
            size = 0;
        }
    }
}
