package ch.bailu.gtk.wrapper;

import ch.bailu.gtk.Pointer;

public class Str extends Pointer {
    private int size = 0;

    public Str(long pointer) {
        super(pointer);
    }

    public Str(String str) {
        super(StrImp.createStr(str));

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
            StrImp.destroy(getCPointer());
            size = 0;
        }
    }
}
