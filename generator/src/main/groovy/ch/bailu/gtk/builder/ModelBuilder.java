package ch.bailu.gtk.builder;

import java.io.IOException;
import java.io.Writer;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.NameSpaceModel;
import ch.bailu.gtk.tag.AliasTag;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.NamespaceTag;
import ch.bailu.gtk.tag.StructureTag;
import ch.bailu.gtk.writer.IO;
import ch.bailu.gtk.writer.CWriter;
import ch.bailu.gtk.writer.JavaApiWriter;
import ch.bailu.gtk.writer.JavaImpWriter;

public class ModelBuilder implements BuilderInterface {


    private NameSpaceModel namespace;

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
    public void buildNamespace(NamespaceTag namespace) {
        this.namespace = new NameSpaceModel(namespace);
    }


    @Override
    public void buildAlias(AliasTag alias) {}


    @Override
    public void buildCallback(CallbackTag callback) {}

    @Override
    public void buildEnumeration(EnumerationTag enumeration) throws IOException {
        ClassModel model = new ClassModel(enumeration, namespace);
        writeJavaFile(model);
    }


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
