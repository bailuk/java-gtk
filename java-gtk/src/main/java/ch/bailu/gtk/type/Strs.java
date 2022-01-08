package ch.bailu.gtk.type;

public class Strs extends Pointer {
    private Str[] strs;
    private boolean destroyAll = false;

    public Strs(CPointer pointer) {
        super(pointer);
        strs = null;
    }

    public Strs(String[] strings) {
        this(toStrs(strings));
        destroyAll = true;
    }

    public Strs(Str[] strs) {
        this(strs, new CPointer(ImpUtil.createPointerArray(Util.toPointerArray(strs))));
    }

    public Strs(Str[] strs, CPointer pointers) {
        super(pointers);
        this.strs=strs;
    }


    private static Str[] toStrs(String[] strs) {
        Str[] result = new Str[strs.length];

        for (int i=0; i< strs.length; i++) {
            if (strs[i] == null) {
                result[i] = Str.NULL;
            } else {
                result[i] = new Str(strs[i]);
            }
        }
        return result;
    }


    public int getLength() {
        return strs.length;
    }

    public Str get(int index) {
        return strs[index];
    }

    public int getSize() {
        if (strs == null) {
            return 0;
        }
        return strs.length * Long.BYTES;
    }


    /**
     * If object was initialized with java String constructor destroy
     * all strings and pointer array.
     * Else only destroy pointer array.
     */
    public void destroy() {
        if (destroyAll) {
            destroyAll();
        } else {
            destroyArray();
        }
    }

    /**
     * Destroy pointer array and strings
     */
    public void destroyAll() {
        if (strs != null) {
            for (Pointer pointer : strs) {
                ImpUtil.destroy(pointer.getCPointer());
            }
            destroyArray();
        }
        strs = null;
    }

    private void destroyArray() {
        if (strs != null) {
            ImpUtil.destroy(getCPointer());
        }
        strs = null;
    }


}
