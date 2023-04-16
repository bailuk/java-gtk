package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.TypeQuery;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class TestTypeQuery {

    public static final int GOBJECT_INSTANCE_SIZE = 24;
    public static final int GOBJECT_CLASS_SIZE = 136;

    @Test
    public void test() {
        var type = ch.bailu.gtk.gobject.Object.getTypeID();
        var typeQuery = new TypeQuery();
        Gobject.typeQuery(type, typeQuery);
        typeQuery.getFieldType();
        assertEquals(type, typeQuery.getFieldType());
        assertEquals((20) << (2), typeQuery.getFieldType());

        assertEquals(GOBJECT_CLASS_SIZE, typeQuery.getFieldClassSize());
        assertEquals(GOBJECT_INSTANCE_SIZE, typeQuery.getFieldInstanceSize());

        assertEquals(TypeSystem.getTypeSize(type).classSize, typeQuery.getFieldClassSize());
    }
}
