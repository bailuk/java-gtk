package ch.bailu.gtk.writer

import ch.bailu.gtk.model.*
import java.io.Writer


abstract class CodeWriter(writer : Writer) : Append {
    private var out = writer
    private var group = GroupSpace(out) 

    open fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        a ("/* this file is machine generated */\n")
    }

    abstract fun writeClass(classModel : ClassModel) 
    abstract fun writeInterface(classModel : ClassModel) 

    abstract fun writeInternalConstructor(classModel : ClassModel) 
    abstract fun writeConstructor(classModel : ClassModel, methodModel : MethodModel) 

    abstract fun writeFactory(classModel : ClassModel, methodModel : MethodModel) 
    abstract fun writePrivateFactory(classModel : ClassModel, methodModel : MethodModel) 

    abstract fun writeConstant(parameterModel : ParameterModel) 
    abstract fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) 
    abstract fun writeSignal(classModel : ClassModel,  methodModel : MethodModel) 
    abstract fun writeField(classModel : ClassModel, parameterModel : ParameterModel)
    abstract fun writeFunction(classModel : ClassModel, methodModel : MethodModel)
    abstract fun writeUnsupported(model : Model) 

    abstract fun writeEnd() 


    override fun a(toOut : String) : Append {
        out.append(toOut)
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

    abstract fun writeMallocConstructor(classModel : ClassModel)
    abstract fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel)
    abstract fun writeCallback(classModel: ClassModel, methodModel: MethodModel)
}


