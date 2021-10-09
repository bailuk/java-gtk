package ch.bailu.gtk.model

import ch.bailu.gtk.log.colonList
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.writer.getJavaMethodName
import java.util.*

class MethodModel : Model {
    private var parameters: MutableList<ParameterModel> = ArrayList()

    val name: String
    val gtkName: String

    val returnType: ParameterModel

    val isConstructorType: Boolean
    val throwsError: Boolean
    private val callbackModel: MutableList<MethodModel> = ArrayList()

    val doc : String


    // simple method with return type and parameters can be a factory
    constructor(namespace: String, method: MethodTag) {
        throwsError = method.throwsError()
        gtkName = method.getIdentifier()
        name = method.getName()
        doc = method.getDoc()

        returnType = ParameterModel(namespace, method.getReturnValue(), false, false)
        setSupported("Deprecated", !method.isDeprecated())
        setSupported("Return value", returnType.isSupported)
        setSupported("Return cb", !returnType.isCallback)
        for (t in method.getParameters()) {
            if (!t.isVarargs()) {
                val parameterModel = ParameterModel(namespace, t, false, false)
                parameters.add(parameterModel)
                setSupported(parameterModel.supportedState, parameterModel.isSupported)
                if (parameterModel.isCallback) {
                    parameterModel.callbackModel?.let { callbackModel.add(it) }
                }
            }
        }
        isConstructorType = "new" == method.getName()
    }

    fun hasCallback(): Boolean {
        return callbackModel.isNotEmpty()
    }

    fun getCallbackModel(): List<MethodModel> {
        return callbackModel
    }


    fun getParameters(): List<ParameterModel> {
        return parameters
    }

    override fun toString(): String {
        val result = StringBuilder().append(colonList(arrayOf(supportedState,returnType.toString(),apiName)))
        for (p in getParameters()) {
            result.append(":").append(p.toString())
        }
        return result.toString()
    }

    val apiName: String
        get() = getJavaMethodName(name)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodModel

        if (name != other.name) return false
        if (gtkName != other.gtkName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + gtkName.hashCode()
        return result
    }
}
