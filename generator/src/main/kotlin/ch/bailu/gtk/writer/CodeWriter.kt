package ch.bailu.gtk.writer

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList


interface CodeWriter  {
    fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel)

    fun writeClass(structureModel : StructureModel)
    fun writeInterface(structureModel : StructureModel)

    fun writeInternalConstructor(structureModel : StructureModel)
    fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel)

    fun writeFactory(structureModel : StructureModel, methodModel : MethodModel)
    fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel)

    fun writeConstant(structureModel: StructureModel, parameterModel : ParameterModel)
    fun writeMethod(structureModel : StructureModel, methodModel : MethodModel)

    fun writeSignal(structureModel : StructureModel, methodModel : MethodModel)
    fun writeField(structureModel : StructureModel, fieldModel : FieldModel)
    fun writeFunction(structureModel : StructureModel, methodModel : MethodModel)
    fun writeUnsupported(model : Model)

    fun writeImplements(implementsModel: ImplementsModel)

    fun writeMallocConstructor(structureModel : StructureModel)
    fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean = false)

    fun writeEnd()
    fun writeGetTypeFunction(structureModel: StructureModel)
    fun writeBeginStruct(structureModel : StructureModel, fields: ModelList<FieldModel>)
    fun writeEndStruct()
    fun writeBeginInstace(namespaceModel: NamespaceModel)
    fun writeEndInstance()
}
