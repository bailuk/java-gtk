package ch.bailu.gtk.type;

import ch.bailu.gtk.gobject.Object;

/**
 * Base class for gobject interfaces
 */
public class Interface extends Object {

    public Interface(CPointer pointer) {
        super(pointer);
    }
}
