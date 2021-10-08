package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.RelativeNamespaceType;
import ch.bailu.gtk.model.type.CType;
import ch.bailu.gtk.model.type.ClassType;
import ch.bailu.gtk.model.type.JavaType;
import ch.bailu.gtk.table.StructureTable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTypeModel {


	@Test
	void testCType() {
		CType ctype = new CType("void");

		assertEquals(false, ctype.isConst());
		assertEquals(false, ctype.isSinglePointer());
		assertEquals("void", ctype.getType());
		assertEquals(false, ctype.contains("ApplicationFlags"));


		ctype = new CType("GApplicationFlags");

		assertEquals(false, ctype.isConst());
		assertEquals(false, ctype.isSinglePointer());
		assertEquals(true, ctype.isDirectType());
		assertEquals("GApplicationFlags", ctype.getType());
		assertEquals(true, ctype.contains("ApplicationFlags"));


		ctype = new CType("PangoAttrShape*");

		assertEquals(false, ctype.isConst());
		assertEquals(true, ctype.isSinglePointer());
		assertEquals(false, ctype.isDirectType());
		assertEquals("PangoAttrShape*", ctype.getType());


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


		classType = new ClassType("namespace", "Application", "GtkApplication", false);
		assertEquals(false, classType.isClass());
		assertEquals(false, classType.isDirectType());

		classType = new ClassType("namespace", "Application", "GtkApplication", true);
		assertEquals(false, classType.isClass());


		StructureTable.INSTANCE.add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication", false);
		assertEquals(false, classType.isClass());
		assertEquals(false, classType.isDirectType());

		classType = new ClassType("namespace", "Application", "GtkApplication", true);
		assertEquals(true, classType.isClass());
		assertEquals(true, classType.isDirectType());

		StructureTable.INSTANCE.add("namespace", "Application");
		classType = new ClassType("namespace", "Application", "GtkApplication*", true);
		assertEquals(true, classType.isClass());
		assertEquals("Application", classType.getFullName());
		assertEquals(false, classType.isDirectType());

		classType = new ClassType("mynamespace", "Application", "GtkApplication*", false);
		assertEquals(false, classType.isClass());
		assertEquals(false, classType.isDirectType());

		classType = new ClassType("mynamespace", "namespace.Application", "GtkApplication*", false);
		assertEquals("ch.bailu.gtk.namespace.Application", classType.getFullName());
		assertEquals(true, classType.isClass());
		assertEquals(false, classType.isDirectType());

	}
}
