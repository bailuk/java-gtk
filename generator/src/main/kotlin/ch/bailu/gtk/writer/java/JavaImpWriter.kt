package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.Names
import ch.bailu.gtk.writer.TextWriter

class JavaImpWriter(private val out: TextWriter) : CodeWriter {

    override fun writeStart(structureModel: StructureModel, namespaceModel: NamespaceModel) {
        JavaApiWriter.writeHeader(out, namespaceModel)
        out.end(3)
    }

    override fun writeClass(structureModel: StructureModel) {
        out.l(3,"class ${structureModel.jnaName} {", 1)
    }


    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0,"        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSignature(methodModel)});", 0)
    }

    override fun writeConstant(structureModel: StructureModel, parameterModel: ParameterModel) {}

    override fun writeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0,"        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSelfSignature(methodModel)});", 0)
    }

    private fun getSelfSignature(methodModel: MethodModel) : String {
        return "long _self${getSignature(methodModel, ", ")}"
    }

    private fun getSignature(methodModel: MethodModel, firstDel : String = "", isSignal: Boolean = false) : String {
        val result = StringBuilder()
        var del = firstDel

        if (isSignal) {
            result.append("long _self")
            del = (", ")
        }

        methodModel.parameters.forEach {
            if (it.isCallback && it.callbackModel != null) {
                result.append("${del}${Names.getJavaCallbackInterfaceName(it.callbackModel.name)} ${it.name}")
            } else {
                result.append("${del}${it.impType} ${it.name}")
            }
            del = ", "

        }

        if (methodModel.throwsError) {
            result.append("${del}long _error")
        }

        if (isSignal) {
            result.append("${del}long _data")
        }
        return result.toString()
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {}

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0, "        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSignature(methodModel)});", 0)
    }

    override fun writeMallocConstructor(structureModel: StructureModel) {}

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        out.start(1)

        out.a("""
            @FunctionalInterface
            public interface ${Names.getJavaCallbackInterfaceName(methodModel.name)} extends com.sun.jna.Callback {
                ${methodModel.returnType.impType} invoke(${getSignature(methodModel, "", isSignal)});
            }
        """, 4)
        out.end(1)
    }

    override fun writeEnd() {
        out.l(0,"}", 1)
    }

    override fun writeGetTypeFunction(structureModel: StructureModel) {
        out.start(0)
        out.a("        long ${structureModel.typeFunction}();\n")
        out.end(0)
    }

    override fun writeBeginStruct(structureModel : StructureModel, fields: ModelList<ParameterModel>) {
        out.start(0)
        out.a("""
            private static long _size = -1;
            public static long allocateStructure() {
                if (_size < 0) {
                    _size = new Fields().size();
                    System.out.println("${structureModel.apiName} size: " + _size + " bytes");
                }
                return ch.bailu.gtk.lib.jna.CLib.allocate(_size);
            }

            @com.sun.jna.Structure.FieldOrder({${getFields(structureModel, fields)}})
            public static class Fields extends com.sun.jna.Structure {
                public Fields() {
                    super(); 
                }

                public Fields(long _self) {
                    super(ch.bailu.gtk.type.Pointer.toJnaPointer(_self));
                }
        """, 4)
        out.end(1)
    }

    private fun getFields(structureModel: StructureModel, fields: ModelList<ParameterModel>): String {
        val result = StringBuilder()
        var del = ""

        fields.forEach {
            result.append(del).append(structureModel.apiName).append(".").append(Names.getJavaConstantName(it.name))
            del = ", "
        }
        return result.toString()
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        out.start(0)

        if (parameterModel.isCallback && parameterModel.callbackModel != null) {
            out.a("        public ${Names.getJavaCallbackInterfaceName(parameterModel.callbackModel.name)} ${parameterModel.name};\n")
        } else {
            out.a("        public ${parameterModel.impType} ${parameterModel.name};\n")
        }
        out.end(0)
    }


    override fun writeEndStruct() {
        out.l(0,"    }", 1)
    }

    override fun writeBeginInstace(namespaceModel: NamespaceModel) {
        out.start(3)
        out.a("""
            private static Instance INSTANCE;

            static Instance INST() {
                if (INSTANCE == null) {
                    INSTANCE = ch.bailu.gtk.lib.jna.Loader.load("${namespaceModel.namespaceConfig.library}", Instance.class);
                }
                return INSTANCE;
            }

            public interface Instance extends com.sun.jna.Library {
        """, 4)
        out.end(0)
    }

    override fun writeEndInstance() {
        out.l(0,"    }", 1)
    }

    override fun writeInterface(structureModel: StructureModel) {}
    override fun writeInternalConstructor(structureModel: StructureModel) {}
    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {}
    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {}
    override fun writeUnsupported(model: Model) {}
}
