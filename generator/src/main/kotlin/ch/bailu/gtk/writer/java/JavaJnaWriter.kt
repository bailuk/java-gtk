package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.TextWriter
import ch.bailu.gtk.writer.getJavaSignalInterfaceName
import ch.bailu.gtk.writer.getJavaSignalMethodName

class JavaJnaWriter(private val out: TextWriter) : CodeWriter {

    override fun writeStart(structureModel: StructureModel, namespaceModel: NamespaceModel) {
        JavaJnaApiWriter.writeHeader(out, namespaceModel)
        out.end(3)
    }

    override fun writeClass(structureModel: StructureModel) {
        out.l(3,"class ${structureModel.jnaName} {", 1)
    }


    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0,"        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSignature(methodModel)});", 0)
    }

    override fun writeConstant(parameterModel: ParameterModel) {}

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
                result.append("${del}${getJavaSignalInterfaceName(it.callbackModel.name)} ${it.name}")
            } else {
                result.append("${del}${it.impType} ${it.name}")
            }
            del = ", "

        }

        if (isSignal) {
            result.append("${del}long _data")
        }
        return result.toString()
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0,"        long g_signal_connect_data(long _self, long detailed_signal, ${getJavaSignalInterfaceName(methodModel.name)} cb, long data, long destroy_data, int flag);", 0)
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        out.start(0)
        out.a("        // FIELD: ${parameterModel.impType} ${parameterModel.name};\n")
        out.end(0)
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        out.l(0, "        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSignature(methodModel)});", 0)
    }

    override fun writeMallocConstructor(structureModel: StructureModel) {
        out.start(0)
        out.a("// TODO writeMallocConstructor\n")
        out.end(0)
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        out.start(1)

        out.a("""
            public interface ${getJavaSignalInterfaceName(methodModel.name)} {
                @jnr.ffi.annotations.Delegate
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

    override fun writeBeginStruct() {
        out.start(0)
        out.a("""
            public static class Fields extends jnr.ffi.Struct {
                public Fields(jnr.ffi.Runtime runtime) {
                    super(runtime);
                }
        """, 4)
        out.end(1)


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
                    INSTANCE = jnr.ffi.LibraryLoader.create(Instance.class).load("${namespaceModel.namespaceConfig.pkgConfigName}");
                }
                return INSTANCE;
            }
                
            public interface Instance {
        """, 4)
        out.end(1)
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
