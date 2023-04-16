package ch.bailu.gtk.validator

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model

object Validator {
    fun giveUp(reason: String, condition: Boolean) {
        if (condition) {
            giveUp(reason)
        }
    }

    fun giveUp(reason: String) {
        throw RuntimeException("Validator: $reason")
    }

    fun validMethodName(methodModel: MethodModel) {
        giveUp("Invalid method name ${methodModel.apiName}", methodModel.apiName == "Register")

    }

    fun validateAlias(apiName: String) {
        giveUp("Type not aliased $apiName", apiName.endsWith(".String"))
    }

    fun validateNotEmpty(name: String) {
        giveUp("Empty name", name.isEmpty())
    }

    fun validateSupported(model: Model) {
        giveUp("Model not supported", !model.isSupported)
    }
}
