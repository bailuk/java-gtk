package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

public class Strs extends Pointer {
    public final static Strs NULL = new Strs(PointerContainer.NULL);

    private Str[] strs;
    private boolean destroyAll = false;

    public Strs(PointerContainer pointer) {
        super(pointer);
        strs = null;
    }

    public Strs(String[] strings) {
        this(toStrs(strings));
        destroyAll = true;
    }

    public Strs(Str[] strs) {
        this(strs, new PointerContainer(Imp.createPointerArray(Util.toPointerArray(strs))));
    }

    public Strs(Str[] strs, PointerContainer pointers) {
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

    public static Strs nullTerminated(String[] strs) {
        Str[] result = new Str[strs.length+1];

        int i=0;
        for (; i< strs.length; i++) {
            if (strs[i] == null) {
                result[i] = Str.NULL;
            } else {
                result[i] = new Str(strs[i]);
            }
        }
        result[i] = Str.NULL;
        return new Strs(result);
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
                Glib.free(pointer);
            }
            destroyArray();
        }
        strs = null;
    }

    private void destroyArray() {
        if (strs != null) {
            Glib.free(asPointer());
        }
        strs = null;
    }


}
