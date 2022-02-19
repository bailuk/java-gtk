package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.TextWriter

class JavaJnaWriter(out: TextWriter) : CodeWriter(out) {

    override fun writeStart(structureModel: StructureModel, namespaceModel: NamespaceModel) {
        super.writeStart(structureModel, namespaceModel)
        out.a("package ${namespaceModel.fullNamespace};\n\n")
        out.a("import jnr.ffi.LibraryLoader;")
        out.end(3)
    }

    override fun writeClass(structureModel: StructureModel, namespaceModel: NamespaceModel) {
        out.start(3)
        out.a("""
            class ${structureModel.jnaName} {
                private static Instance INSTANCE;

                public static Instance INST() {
                    if (INSTANCE == null) {
                        INSTANCE = LibraryLoader.create(Instance.class).load("${namespaceModel.namespaceConfig.pkgConfigName}");
                    }
                    return INSTANCE;
                }

                public interface Instance {
        """.trimIndent())
        out.end(1)
    }

    override fun writeInterface(structureModel: StructureModel) {}

    override fun writeInternalConstructor(structureModel: StructureModel) {}

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {}

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {}

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeConstant(parameterModel: ParameterModel) {}

    override fun writeNativeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(0)
        out.a("        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSelfSignature(methodModel.parameters)});\n")
        out.end(0)
    }

    private fun getSelfSignature(parameters : List<ParameterModel>) : String {
        return "long _self${getSignature(parameters, ", ")}"
    }

    private fun getSignature(parameters : List<ParameterModel>, firstDel : String = "") : String {
        val result = StringBuilder()
        var del = firstDel

        for (p in parameters) {
            if (!p.isCallback) {
                result.append("${del}${p.impType} ${p.name}")
                del = ", "
            }
        }
        return result.toString()
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(0)
        out.a("// TODO writeSignal\n")
        out.end(0)
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {}

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(0)
        out.a("        ${methodModel.returnType.impType} ${methodModel.gtkName}(${getSignature(methodModel.parameters)});\n")
        out.end(0)
    }

    override fun writeUnsupported(model: Model) {}

    override fun writeMallocConstructor(structureModel: StructureModel) {
        out.start(0)
        out.a("// TODO writeMallocConstructor\n")
        out.end(0)
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(0)
        out.a("// TODO writeCallback\n")
        out.end(0)
    }

    override fun writeEnd() {
        out.start(0)
        out.a("    }\n")
        out.a("}\n")
        out.end(0)
    }

    override fun writeGetTypeFunction(structureModel: StructureModel) {
        out.start(0)
        out.a("        long ${structureModel.typeFunction}();\n")
        out.end(0)
    }
}
