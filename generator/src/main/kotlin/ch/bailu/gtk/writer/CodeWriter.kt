package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.*
import java.io.Writer


interface CodeWriter  {
    fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel)

    fun writeClass(structureModel : StructureModel)
    fun writeInterface(structureModel : StructureModel)

    fun writeInternalConstructor(structureModel : StructureModel)
    fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel)

    fun writeFactory(structureModel : StructureModel, methodModel : MethodModel)
    fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel)

    fun writeConstant(parameterModel : ParameterModel) 
    fun writeMethod(structureModel : StructureModel, methodModel : MethodModel)
    fun writeSignal(structureModel : StructureModel, methodModel : MethodModel)
    fun writeField(structureModel : StructureModel, parameterModel : ParameterModel)
    fun writeFunction(structureModel : StructureModel, methodModel : MethodModel)
    fun writeUnsupported(model : Model)

    fun writeMallocConstructor(structureModel : StructureModel)
    fun writeCallback(structureModel: StructureModel, methodModel: MethodModel)

    fun writeEnd()
    fun writeGetTypeFunction(structureModel: StructureModel)
    fun writeBeginStruct()
    fun writeEndStruct()
    fun writeBeginInstace(namespaceModel: NamespaceModel)
    fun writeEndInstance()
}
