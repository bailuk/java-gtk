package ch.bailu.gtk

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.model.type.CType
import ch.bailu.gtk.model.type.ClassType
import ch.bailu.gtk.model.type.JavaType
import ch.bailu.gtk.table.StructureTable

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
        assertTrue(jtype.valid)
        jtype = JavaType("void")
        assertEquals("void", jtype.getApiTypeName())
        assertTrue(jtype.valid)
        assertTrue(jtype.isVoid())
        jtype = JavaType("void")
        assertEquals("void", jtype.getApiTypeName())
        assertTrue(jtype.valid)
        assertTrue(jtype.isVoid())
        jtype = JavaType("guint8")
        assertEquals("int", jtype.getApiTypeName())
        assertTrue(jtype.valid)
        assertFalse(jtype.isVoid())
    }

    @Test
    fun testNamespaceType() {
        var type = NamespaceType("namespace", "namespace.Application")
        assertTrue(type.valid)
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        type = NamespaceType("namespace", "Application")
        assertTrue(type.valid)
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        type = NamespaceType("namespace2", "namespace.Application")
        assertTrue(type.valid)
        assertEquals("namespace", type.namespace)
        assertEquals("Application", type.name)
        val ntype = NamespaceType("GObject", "ParamSpecInt64")
        assertEquals("ParamSpecInt64", ntype.name)
        assertEquals("gobject", ntype.namespace)
        assertTrue(ntype.valid)
    }


    @Test
    fun testClassTypeNotInTable() {
        var classType = ClassType("namespace", "Application", "GtkApplication", false)
        assertFalse(classType.valid)
        assertFalse(classType.directType)
        classType = ClassType("namespace", "Application", "GtkApplication", true)
        assertTrue(classType.valid) // Wrapper is a valid type
        assertTrue(classType.wrapper)
    }

    @Test
    fun testClassTypeInTable() {
        StructureTable.add("namespace", "Application")

        var classType = ClassType("namespace", "Application", "GtkApplication", false)
        assertTrue(classType.valid)
        assertFalse(classType.wrapper)
        assertTrue(classType.directType)

        classType = ClassType("namespace", "Application", "GtkApplication", true)
        assertTrue(classType.valid)
        assertTrue(classType.wrapper)
        assertTrue(classType.directType)
        StructureTable.clear()
    }

    @Test
    fun testClassTypInTablePointer() {
        StructureTable.add("namespace", "Application")


        var classType = ClassType("namespace", "Application", "GtkApplication*", true)
        assertTrue(classType.valid)
        assertTrue(classType.wrapper)
        // is wrapper because is out enum
        assertEquals("ch.bailu.gtk.type.Int", classType.getApiTypeName("namespace"))
        assertTrue(classType.directType)

        classType = ClassType("mynamespace", "Application", "GtkApplication*", false)
        assertFalse(classType.valid)
        assertFalse(classType.directType)

        classType = ClassType("mynamespace", "namespace.Application", "GtkApplication*", false)
        assertEquals("ch.bailu.gtk.namespace.Application", classType.getApiTypeName(""))
        assertTrue(classType.valid)
        assertFalse(classType.directType)

        StructureTable.clear()
    }
}
