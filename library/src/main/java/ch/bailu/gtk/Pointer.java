package ch.bailu.gtk;


public class Pointer {

    private final long pointer;

    public Pointer(long pointer) {
        this.pointer = pointer;
    }

    public static Pointer[] toPointerArray(long[] in) {
        Pointer[] result = new Pointer[in.length];
        for (int i =0; i < in.length; i++) {
            result[i] = new Pointer(in[i]);
        }
        return result;
    }


    public static long[] toLongArray(Pointer[] in) {
        long[] result = new long[in.length];

        for (int i = 0; i < in.length; i++) {
            result[i] = in[i].pointer;
        }
        return result;
    }

    public long toLong() {
        return pointer;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(pointer).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pointer) {
            return ((Pointer) obj).pointer == pointer;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }
}