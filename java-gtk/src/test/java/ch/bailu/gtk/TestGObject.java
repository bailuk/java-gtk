package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.type.Str;

public class TestGObject {

    @Test void testGObject() {
        assertEquals(GTK.TRUE,  Gobject.signalIsValidName(new Str("activate")));
        assertEquals(GTK.FALSE, Gobject.signalIsValidName(new Str("äctivate")));
        assertEquals((20) << (2), ch.bailu.gtk.gobject.Object.getTypeID());
    }
}
