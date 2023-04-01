package ch.bailu.gtk.writer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestNames {

    @Test
    fun testJavaMethodName() {
        assertEquals("cclosureMarshalBooleanBoxedBoxed", Names.getJavaMethodName("cclosure_marshal_BOOLEAN__BOXED_BOXED"))
    }

    @Test
    fun testCallbackName() {
        assertEquals("OnClassInitFunc", Names.getJavaCallbackInterfaceName("ClassInitFunc"))
    }
}
