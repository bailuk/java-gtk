package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.GObject;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.type.Str;

public class TestGObject {

    @Test void testGObject() {
        assertTrue(Gobject.signalIsValidName(new Str("activate")));
        assertFalse(Gobject.signalIsValidName(new Str("äctivate")));
        assertEquals((20) << (2), GObject.getTypeID());
    }
}
