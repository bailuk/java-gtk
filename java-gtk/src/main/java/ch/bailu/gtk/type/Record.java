package ch.bailu.gtk.type;

/**
 * Record: a structure that is not based on gobject
 *
 */
public class Record extends Pointer {
    public Record(CPointer pointer) {
        super(pointer);
    }

    /**
     * Frees resources.
     * Free memory allocated for this record.
     * Object is invalid afterwards and should not be accessed.
     */
    public void destroy() {
        ch.bailu.gtk.lib.jna.CLib.INST().free(getCPointer());
    }
}
