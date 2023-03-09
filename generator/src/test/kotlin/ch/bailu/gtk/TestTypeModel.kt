package ch.bailu.gtk

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.model.type.CType
import ch.bailu.gtk.model.type.ClassType
import ch.bailu.gtk.model.type.JavaType
import ch.bailu.gtk.table.StructureTable.add
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestTypeModel {
    @Test
    fun testCType() {
        var ctype = CType("void")
        assertFalse(ctype.isConst)
        assertFalse(ctype.isSinglePointer)
        assertEquals("void", ctype.type)
        assertFalse(ctype.contains("ApplicationFlags"))
        ctype = CType("GApplicationFlags")
        assertFalse(ctype.isConst)
        assertFalse(ctype.isSinglePointer)
        assertTrue(ctype.isDirectType)
        assertEquals("GApplicationFlags", ctype.type)
        assertTrue(ctype.contains("ApplicationFlags"))
        ctype = CType("PangoAttrShape*")
        assertFalse(ctype.isConst)
        assertTrue(ctype.isSinglePointer)
        assertFalse(ctype.isDirectType)
        assertEquals("PangoAttrShape*", ctype.type)
        ctype = CType("const gchar*")
        assertTrue(ctype.isConst)
        assertTrue(ctype.isSinglePointer)
        ctype = CType("gchar**")
        assertFalse(ctype.isConst)
        assertFalse(ctype.isSinglePointer)
    }

    @Test
    fun testJavaType() {
        var jtype = JavaType("int")
        assertEquals("int", jtype.getApiTypeName())
        assertTrue(jtype.isValid())
        jtype = JavaType("void")
        assertEquals("void", jtype.getApiTypeName())
        assertTrue(jtype.isValid())
        assertTrue(jtype.isVoid())
        jtype = JavaType("void")
        assertEquals("void", jtype.getApiTypeName())
        assertTrue(jtype.isValid())
        assertTrue(jtype.isVoid())
        jtype = JavaType("guint8")
        assertEquals("int", jtype.getApiTypeName())
        assertTrue(jtype.isValid())
        assertFalse(jtype.isVoid())
    }

    @Test
    fun testNamespaceType() {
        var type = NamespaceType("namespace", "namespace.Application")
        assertTrue(type.isValid())
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        type = NamespaceType("namespace", "Application")
        assertTrue(type.isValid())
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        type = NamespaceType("namespace2", "namespace.Application")
        assertTrue(type.isValid())
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        val ntype = NamespaceType("GObject", "ParamSpecInt64")
        assertEquals("ParamSpecInt64", ntype.name)
        assertEquals("gobject", ntype.namespace)
        assertTrue(ntype.isValid())
    }

    @Test
    fun testClassType() {
        var classType = ClassType("namespace", "Application", "GtkApplication", false)
        assertFalse(classType.isClass())
        assertFalse(classType.isDirectType())
        classType = ClassType("namespace", "Application", "GtkApplication", true)
        assertFalse(classType.isClass())
        add("namespace", "Application")
        classType = ClassType("namespace", "Application", "GtkApplication", false)
        assertFalse(classType.isClass())
        assertFalse(classType.isDirectType())
        classType = ClassType("namespace", "Application", "GtkApplication", true)
        assertTrue(classType.isClass())
        assertTrue(classType.isDirectType())
        add("namespace", "Application")
        classType = ClassType("namespace", "Application", "GtkApplication*", true)
        assertTrue(classType.isClass())
        assertEquals("Application", classType.getApiTypeName("namespace"))
        assertFalse(classType.isDirectType())
        classType = ClassType("mynamespace", "Application", "GtkApplication*", false)
        assertFalse(classType.isClass())
        assertFalse(classType.isDirectType())
        classType = ClassType("mynamespace", "namespace.Application", "GtkApplication*", false)
        assertEquals("ch.bailu.gtk.namespace.Application", classType.getApiTypeName(""))
        assertTrue(classType.isClass())
        assertFalse(classType.isDirectType())
    }
}
