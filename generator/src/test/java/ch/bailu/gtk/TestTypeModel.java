package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.model.type.CType;
import ch.bailu.gtk.model.type.ClassType;
import ch.bailu.gtk.model.type.JavaType;
import ch.bailu.gtk.table.StructureTable;

public class TestTypeModel {


	@Test
	void testCType() {
		CType ctype = new CType("void");

		assertFalse(ctype.isConst());
		assertFalse(ctype.isSinglePointer());
		assertEquals("void", ctype.getType());
		assertFalse(ctype.contains("ApplicationFlags"));


		ctype = new CType("GApplicationFlags");

		assertFalse(ctype.isConst());
		assertFalse(ctype.isSinglePointer());
		assertTrue(ctype.isDirectType());
		assertEquals("GApplicationFlags", ctype.getType());
		assertTrue(ctype.contains("ApplicationFlags"));


		ctype = new CType("PangoAttrShape*");

		assertFalse(ctype.isConst());
		assertTrue(ctype.isSinglePointer());
		assertFalse(ctype.isDirectType());
		assertEquals("PangoAttrShape*", ctype.getType());


		ctype = new CType("const gchar*");
		assertTrue(ctype.isConst());
		assertTrue(ctype.isSinglePointer());


		ctype = new CType("gchar**");
		assertFalse(ctype.isConst());
		assertFalse(ctype.isSinglePointer());

	}


	@Test
	void testJavaType() {
		JavaType jtype = new JavaType("int");

		assertEquals("int", jtype.getApiTypeName());
		assertTrue(jtype.isValid());


		jtype = new JavaType("void");
		assertEquals("void", jtype.getApiTypeName());
		assertTrue(jtype.isValid());
		assertTrue(jtype.isVoid());


		jtype = new JavaType("void");
		assertEquals("void", jtype.getApiTypeName());
		assertTrue(jtype.isValid());
		assertTrue(jtype.isVoid());

		jtype = new JavaType("guint8");
		assertEquals("int", jtype.getApiTypeName());
		assertTrue(jtype.isValid());
		assertFalse(jtype.isVoid());
	}

	@Test
	void testNamespaceType() {
		NamespaceType type = new NamespaceType("namespace", "namespace.Application");

		assertTrue(type.isValid());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());


		type = new NamespaceType("namespace", "Application");

		assertTrue(type.isValid());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());



		type = new NamespaceType("namespace2", "namespace.Application");

		assertTrue(type.isValid());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());


		NamespaceType ntype = new NamespaceType("GObject", "ParamSpecInt64");
		assertEquals("ParamSpecInt64", ntype.getName());
		assertEquals("gobject", ntype.getNamespace());
		assertTrue(ntype.isValid());
	}


	@Test
	void testClassType() {
		ClassType classType = new ClassType("namespace", "Application", "GtkApplication", false);
		assertFalse(classType.isClass());
		assertFalse(classType.isDirectType());

		classType = new ClassType("namespace", "Application", "GtkApplication", true);
		assertFalse(classType.isClass());


		StructureTable.INSTANCE.add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication", false);
		assertFalse(classType.isClass());
		assertFalse(classType.isDirectType());

		classType = new ClassType("namespace", "Application", "GtkApplication", true);
		assertTrue(classType.isClass());
		assertTrue(classType.isDirectType());

		StructureTable.INSTANCE.add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication*", true);
		assertTrue(classType.isClass());
		assertEquals("Application", classType.getApiTypeName("namespace"));
		assertFalse(classType.isDirectType());

		classType = new ClassType("mynamespace", "Application", "GtkApplication*", false);
		assertFalse(classType.isClass());
		assertFalse(classType.isDirectType());

		classType = new ClassType("mynamespace", "namespace.Application", "GtkApplication*", false);
		assertEquals("ch.bailu.gtk.namespace.Application", classType.getApiTypeName(""));
		assertTrue(classType.isClass());
		assertFalse(classType.isDirectType());

	}
}
