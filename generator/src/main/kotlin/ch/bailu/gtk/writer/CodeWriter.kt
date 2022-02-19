package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.*
import java.io.Writer


abstract class CodeWriter(val out: TextWriter)  {
    open fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        out.a("/* this file is machine generated */\n")
    }

    abstract fun writeClass(structureModel : StructureModel, namespaceModel: NamespaceModel)
    abstract fun writeInterface(structureModel : StructureModel)

    abstract fun writeInternalConstructor(structureModel : StructureModel)
    abstract fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel)

    abstract fun writeFactory(structureModel : StructureModel, methodModel : MethodModel)
    abstract fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel)

    abstract fun writeConstant(parameterModel : ParameterModel) 
    abstract fun writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel)
    abstract fun writeSignal(structureModel : StructureModel, methodModel : MethodModel)
    abstract fun writeField(structureModel : StructureModel, parameterModel : ParameterModel)
    abstract fun writeFunction(structureModel : StructureModel, methodModel : MethodModel)
    abstract fun writeUnsupported(model : Model)

    abstract fun writeMallocConstructor(structureModel : StructureModel)
    abstract fun writeCallback(structureModel: StructureModel, methodModel: MethodModel)

    abstract fun writeEnd()
    abstract fun writeGetTypeFunction(structureModel: StructureModel)
}

