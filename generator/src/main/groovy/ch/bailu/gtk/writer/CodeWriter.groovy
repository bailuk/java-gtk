package ch.bailu.gtk.writer

import ch.bailu.gtk.model.*



abstract class CodeWriter  implements Append {
    private final Writer out;

    private final GroupSpace group;


    CodeWriter(Writer writer) {
        out = writer;
        group = new GroupSpace(out);
    }



    void writeStart(ClassModel classModel, NameSpaceModel namespaceModel) throws IOException {
        a "/* this file is machine generated */\n"
    }

    abstract void writeClass(ClassModel classModel) throws IOException;
    abstract void writeInterface(ClassModel classModel) throws IOException;

    abstract void writeInternalConstructor(ClassModel classModel) throws IOException;
    abstract void writeConstructor(ClassModel classModel, MethodModel m) throws IOException;

    abstract void writeFactory(ClassModel classModel, MethodModel m) throws IOException;
    abstract void writePrivateFactory(ClassModel classModel, MethodModel m) throws IOException;

    abstract void writeConstant(ParameterModel p) throws IOException;
    abstract void writeNativeMethod(ClassModel c, MethodModel m) throws IOException;
    abstract void writeSignal(ClassModel classModel, MethodModel signal) throws IOException;
    abstract void writeField(ClassModel classModel, ParameterModel p)
    abstract void writeFunction(ClassModel classModel, MethodModel m)
    abstract void writeUnsupported(Model m) throws IOException;

    abstract void writeEnd() throws IOException;


    @Override
    Append a(String toOut) throws IOException {
        out.append(toOut);
        return this;
    }

    protected CodeWriter start() throws IOException {
        group.start();
        return this;
    }

    protected CodeWriter start(int i) throws IOException {
        group.start(i);
        return this;
    }

    CodeWriter next() {
        group.next();
        return this;
    }

    protected CodeWriter end(int i) {
        group.end(i);
        return this;
    }


    abstract void writeMallocConstructor(ClassModel classModel)  throws IOException
}


