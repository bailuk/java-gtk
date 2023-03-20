package ch.bailu.gtk.type;

/**
 * https://www.w3schools.com/java/java_data_types.asp
 */
public class Int64 extends Wrapper {
    private boolean created;

    public Int64() {
        this(createLong(0));
    }

    public Int64(CPointer pointer) {
        super(pointer);
        created = pointer.isNotNull();
    }


    private static CPointer createLong(long value) {
        return new CPointer(ImpInt.createLong(value));
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

    @Override
    public void destroy() {
        if (created) {
            ImpUtil.destroy(getCPointer());
            created = false;
        }
    }
}
