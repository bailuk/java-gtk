package ch.bailu.gtk;

import org.junit.jupiter.api.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.gtk.builder.AliasBuilder;
import ch.bailu.gtk.converter.AliasTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.RelativeNamespaceType;
import ch.bailu.gtk.converter.StructureTable;
import ch.bailu.gtk.model.ClassType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAlias {

    @Test void testAlias() {
        var from = new NamespaceType("gtk", "Allocation");
        var to = new NamespaceType("gdk", "Rectangle");


        AliasTable.instance().add(from, to);

        var test = AliasTable.instance().convert(from);

        assertEquals("Rectangle", test.getName());
        assertEquals("gdk", test.getNamespace());
    }
/*
    @Test void aliasParser() throws IOException, XmlPullParserException {
        var from = new NamespaceType("gdk", "Rectangle");
        var to = new NamespaceType("gtk", "Allocation");

        String[] args = {"-i", "/usr/share/gir-1.0",
                "-j", "library/build/generated/src/main/java/ch/bailu/gtk/",
                "-c", "${project.getRootDir()}/glue/build/generated/src/main/c/"};
        Configuration.init(args);

        App.parse(new AliasBuilder());

        var test = AliasTable.instance().convert(from);

        assertEquals("Rectangle", test.getName());
        assertEquals("gdk", test.getNamespace());


        ClassType classType = new ClassType("glib", "String", "GdkString");
        assertEquals("GString", classType.getFullName());

    }
    */

}
