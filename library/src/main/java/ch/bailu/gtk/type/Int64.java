package ch.bailu.gtk.type;

/**
 * https://www.w3schools.com/java/java_data_types.asp
 */
public class Int64 extends Wrapper {
    private boolean created;

    public Int64() {
        this(createLong(0));
    }

    public Int64(long pointer) {
        super(pointer);
        created = pointer != 0;
    }


    private static long createLong(long value) {
        return ImpInt.createLong(value);
    }

    public static Int64 create(long value) {
        return new Int64(createLong(value));
    }

    public void set(long i) {
        ImpInt.setLong(getCPointer(), i);
    }

    public long get() {
        return ImpInt.getLong(getCPointer());
    }

    public void destroy() {
        if (created) {
            ImpUtil.destroy(getCPointer());
            created = false;
        }
    }
}
