package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.InitiallyUnowned;
import ch.bailu.gtk.gobject.InitiallyUnownedClass;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClass;

public class TestIntrospection {
    @Test
    void testTypeSize() {
        assertTrue(ObjectClass.getInstanceSize()>0);
        assertEquals(Object.getTypeSize().classSize, ObjectClass.getInstanceSize());

        assertTrue(WidgetClass.getInstanceSize()>0);
        assertEquals(Widget.getTypeSize().classSize, WidgetClass.getInstanceSize());

        assertEquals(Widget.getParentTypeSize().instanceSize, InitiallyUnowned.getInstanceSize());
        assertEquals(Widget.getParentTypeSize().classSize, InitiallyUnownedClass.getInstanceSize());
        assertEquals(Widget.getParentTypeSize().classSize, InitiallyUnowned.getTypeSize().classSize);
        assertEquals(Widget.getParentTypeSize().instanceSize, InitiallyUnowned.getTypeSize().instanceSize);
    }

    @Test
    void testTypeId() {
        assertEquals(Widget.getParentTypeID(), InitiallyUnowned.getTypeID());
    }
}
