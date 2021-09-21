package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.RelativeNamespaceType;
import ch.bailu.gtk.converter.StructureTable;
import ch.bailu.gtk.model.CType;
import ch.bailu.gtk.model.ClassType;
import ch.bailu.gtk.model.JavaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTypeModel {


	@Test
	void testCType() {
		CType ctype = new CType("void");

		assertEquals(false, ctype.isConst());
		assertEquals(false, ctype.isSinglePointer());
		assertEquals("void", ctype.getName());
		assertEquals(false, ctype.contains("ApplicationFlags"));


		ctype = new CType("GApplicationFlags");

		assertEquals(false, ctype.isConst());
		assertEquals(false, ctype.isSinglePointer());
		assertEquals("GApplicationFlags", ctype.getName());
		assertEquals(true, ctype.contains("ApplicationFlags"));


		ctype = new CType("GtkApplication*");

		assertEquals(false, ctype.isConst());
		assertEquals(true, ctype.isSinglePointer());
		assertEquals("GtkApplication*", ctype.getName());


		ctype = new CType("const gchar*");
		assertEquals(true, ctype.isConst());
		assertEquals(true, ctype.isSinglePointer());


		ctype = new CType("gchar**");
		assertEquals(false, ctype.isConst());
		assertEquals(false, ctype.isSinglePointer());

	}


	@Test
	void testJavaType() {
		JavaType jtype = new JavaType("int");

		assertEquals("int", jtype.getType());
		assertEquals(true, jtype.isValid());


		jtype = new JavaType("void");
		assertEquals("void", jtype.getType());
		assertEquals(true, jtype.isValid());
		assertEquals(true, jtype.isVoid());


		jtype = new JavaType("void");
		assertEquals("void", jtype.getType());
		assertEquals(true, jtype.isValid());
		assertEquals(true, jtype.isVoid());

		jtype = new JavaType("guint8");
		assertEquals("int", jtype.getType());
		assertEquals(true, jtype.isValid());
		assertEquals(false, jtype.isVoid());

/*
		EnumTable.instance().add(new NamespaceType("namespace", "DateDMY"));
		jtype = new JavaType("namespace", "DateDMY", "GDateDMY");
		assertEquals("int", jtype.getName());
		assertEquals(true, jtype.isValid());
		assertEquals(false, jtype.isVoid());
*/
	}

	@Test
	void testNamespaceType() {
		RelativeNamespaceType type = new RelativeNamespaceType("namespace", "namespace.Application");

		assertEquals(true, type.isValid());
		assertEquals(true, type.hasCurrentNamespace());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());


		type = new RelativeNamespaceType("namespace", "Application");

		assertEquals(true, type.isValid());
		assertEquals(true, type.hasCurrentNamespace());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());



		type = new RelativeNamespaceType("namespace2", "namespace.Application");

		assertEquals(true, type.isValid());
		assertEquals(false, type.hasCurrentNamespace());
		assertEquals("namespace", type.getNamespace());
		assertEquals("Application", type.getName());


		NamespaceType ntype = new NamespaceType("GObject", "ParamSpecInt64");
		assertEquals("ParamSpecInt64", ntype.getName());
		assertEquals("gobject", ntype.getNamespace());
		assertEquals(true, ntype.isValid());
	}


	@Test
	void testClassType() {
		ClassType classType = new ClassType();

		assertEquals(false, classType.isClass());


		classType = new ClassType("namespace", "Application", "GtkApplication");
		assertEquals(false, classType.isClass());

		StructureTable.instance().add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication");
		assertEquals(false, classType.isClass());

		StructureTable.instance().add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication*");
		assertEquals(true, classType.isClass());
		assertEquals("Application", classType.getFullName());

		classType = new ClassType("mynamespace", "Application", "GtkApplication*");
		assertEquals(false, classType.isClass());

		classType = new ClassType("mynamespace", "namespace.Application", "GtkApplication*");
		assertEquals("ch.bailu.gtk.namespace.Application", classType.getFullName());
		assertEquals(true, classType.isClass());

	}
}
