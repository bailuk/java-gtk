package ch.bailu.gtk.wrapper;

import ch.bailu.gtk.Pointer;

public class Strs extends Pointer {
    private Str[] strs;

    public Strs(long pointer) {
        super(pointer);
        strs = null;
    }

    public Strs(String[] strings) {
        this(strings, toStrs(strings));
    }


    private Strs(String[] strings, Str[] strs) {
        this(strings, strs, UtilImp.createPointerArray(Util.toPointerArray(strs)));
    }

    public Strs(String[] strings, Str[] strs, long pointers) {
        super(pointers);
        this.strs=strs;
    }


    private static Str[] toStrs(String[] strs) {
        Str[] result = new Str[strs.length];

        for (int i=0; i< strs.length; i++) {
            result[i] = new Str(strs[i]);
        }
        return result;
    }


    public int getSize() {
        if (strs == null) {
            return 0;
        }
        return strs.length * Long.BYTES;
    }

    public void destroy() {
        if (strs != null) {
            for (Pointer pointer : strs) {
                UtilImp.destroy(pointer.getCPointer());
            }
            UtilImp.destroy(getCPointer());
        }
        strs = null;
    }

}
