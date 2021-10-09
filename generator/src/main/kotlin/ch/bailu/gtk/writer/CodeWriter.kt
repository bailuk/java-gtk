package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.*
import java.io.Writer


abstract class CodeWriter(writer : Writer) : Append {
    private var out = writer
    private var group = GroupSpace(out) 

    open fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        a ("/* this file is machine generated */\n")
    }

    abstract fun writeClass(structureModel : StructureModel)
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

    abstract fun writeEnd() 


    override fun a(o : String) : Append {
        out.append(o)
        return this
    }

    protected fun start() : CodeWriter {
        group.start()
        return this
    }

    protected fun start(i : Int) : CodeWriter  {
        group.start(i)
        return this
    }

    fun next() : CodeWriter {
        group.next()
        return this
    }

    protected fun end(i : Int) : CodeWriter {
        group.end(i)
        return this
    }

    abstract fun writeMallocConstructor(structureModel : StructureModel)
    abstract fun writeInterfaceMethod(structureModel: StructureModel, m: MethodModel)
    abstract fun writeCallback(structureModel: StructureModel, methodModel: MethodModel)
}


