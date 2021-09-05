package ch.bailu.gtk.builder;

import java.io.IOException;
import java.io.Writer;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.NamespaceModel;
import ch.bailu.gtk.tag.AliasTag;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.NamespaceTag;
import ch.bailu.gtk.tag.StructureTag;
import ch.bailu.gtk.writer.CWriter;
import ch.bailu.gtk.writer.IO;
import ch.bailu.gtk.writer.JavaApiWriter;
import ch.bailu.gtk.writer.JavaImpWriter;

public class ModelBuilder implements BuilderInterface {


    private NamespaceModel namespace;

    @Override
    public void buildStructure(StructureTag structure) throws IOException {

        ClassModel model = new ClassModel(structure, namespace);
        writeJavaFile(model);

        if (model.hasNativeCalls()) {
            writeCFile(model);
            writeJavaImpFile(model);
        }
    }

    private void writeJavaImpFile(ClassModel model) throws IOException {
        Writer out = null;
        try {
            out = IO.getJavaImpWriter(model, namespace);
            model.write(new JavaImpWriter(out));
        } finally {
            IO.close(out);
        }

    }

    private void writeCFile(ClassModel model) throws IOException {
        Writer out = null;
        try {
            out = IO.getCWriter(model, namespace);
            model.write(new CWriter(out));
        } finally {
            IO.close(out);
        }
    }


    @Override
    public void buildNamespaceStart(NamespaceTag namespace) {
        this.namespace = new NamespaceModel(namespace);
    }

    @Override
    public void buildNamespaceEnd(NamespaceTag namespace) throws IOException {
        // functions
        ClassModel model = new ClassModel(namespace);
        writeJavaFile(model);

        if (model.hasNativeCalls()) {
            writeCFile(model);
            writeJavaImpFile(model);
        }


        // constants
        model = new ClassModel(new NamespaceModel(namespace), namespace.getConstants());
        writeJavaFile(model);

    }


    @Override
    public void buildAlias(AliasTag alias) {}


    @Override
    public void buildEnumeration(EnumerationTag enumeration) throws IOException {
        ClassModel model = new ClassModel(namespace, enumeration);
        writeJavaFile(model);
    }

    @Override
    public void buildCallback(CallbackTag callbackTag) {}

    private void writeJavaFile(ClassModel model) throws IOException {
        Writer out = null;
        try {
            out = IO.getJavaWriter(model.getApiName(), namespace);

            model.write(new JavaApiWriter(out));
        } finally {
            IO.close(out);
        }
    }

}
