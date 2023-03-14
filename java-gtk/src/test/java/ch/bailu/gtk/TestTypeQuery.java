package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.GObject;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.TypeQuery;
import ch.bailu.gtk.type.Sizes;

public class TestTypeQuery {

    @Test
    public void test() {
        var type = GObject.getTypeID();
        var typeQuery = new TypeQuery();
        Gobject.typeQuery(type, typeQuery);
        typeQuery.getFieldType();
        assertEquals(type, typeQuery.getFieldType());
        assertEquals((20) << (2), typeQuery.getFieldType());

        assertEquals(Sizes.GOBJECT_CLASS, typeQuery.getFieldClassSize());
        assertEquals(Sizes.GOBJECT, typeQuery.getFieldInstanceSize());
    }
}
