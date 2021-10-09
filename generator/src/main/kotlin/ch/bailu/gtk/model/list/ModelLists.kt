package ch.bailu.gtk.model.list

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
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
    val fields = ModelList<ParameterModel>(unsupported)
    val constants = ModelList<ParameterModel>(unsupported)


    fun addIfSupportedWithCallbacks(models: ModelList<MethodModel>, methodModel: MethodModel) {
        models.addIfSupported(methodModel)
        if (methodModel.isSupported) {
            methodModel.getCallbackModel().forEach {callbacks.add(it)}
        }
    }

    fun hasNativeCalls(): Boolean {
        return methods.size > 0 || privateFactories.size > 0 || signals.size > 0 || fields.size > 0 || functions.size > 0
    }
}
