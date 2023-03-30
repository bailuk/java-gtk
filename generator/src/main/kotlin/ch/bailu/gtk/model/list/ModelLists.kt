package ch.bailu.gtk.model.list

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList

class ModelLists {

    val unsupported: ModelList<Model> = ModelList()
    val privateFactories = ModelList<MethodModel>(unsupported)
    val factories = ModelList<MethodModel>(unsupported)
    val constructors = ModelList<MethodModel>(unsupported)
    val methods = ModelList<MethodModel>(unsupported)
    val signals = ModelList<MethodModel>(unsupported)
    val callbacks = ModelList<MethodModel>(unsupported)
    val functions = ModelList<MethodModel>(unsupported)
    val fields = ModelList<FieldModel>(unsupported)
    val constants = ModelList<ParameterModel>(unsupported)
    val implements = ModelList<ImplementsModel>(unsupported)

    fun addIfSupportedWithCallbacks(models: ModelList<MethodModel>, methodModel: MethodModel) {
        models.addIfSupported(methodModel)
        if (methodModel.isSupported) {
            methodModel.callbackModel.forEach {callbacks.add(it)}
        }
    }

    fun hasNativeCalls(): Boolean {
        return methods.size > 0 || privateFactories.size > 0 || signals.size > 0 || fields.size > 0 || functions.size > 0
    }
}
