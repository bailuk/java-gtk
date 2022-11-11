package ch.bailu.gtk.model

import ch.bailu.gtk.log.DebugPrint
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.writer.Names

class MethodModel(namespace: String, parameterNamespace: String, method: MethodTag, preferNative: Boolean) : Model() {
    val parameters: MutableList<ParameterModel> = ArrayList()

    val name: String = method.getName()
    val gtkName: String = method.getIdentifier()

    val returnType: ParameterModel = ParameterModel(
        namespace,
        method.getReturnValue(),
        preferNative = false,
        isConstant = false,
        supportsDirectAccess = false
    )

    var hasNativeVariant = false
        private set

    var isNativeVariant = false
        private set

    val isConstructorType: Boolean
    val throwsError: Boolean = method.throwsError()
    val callbackModel: MutableList<MethodModel> = ArrayList()

    val doc : String = method.getDoc()

    init {
        setSupported("deprecated", !method.isDeprecated())
        setSupported("return-value-not-supported", returnType.isSupported)
        setSupported("returns-callback", !returnType.isCallback)

        for (t in method.getParameters()) {
            val parameterModel = ParameterModel(
                parameterNamespace, t,
                preferNative = preferNative,
                isConstant = false,
                supportsDirectAccess = false
            )
            hasNativeVariant = hasNativeVariant || parameterModel.hasNativeVariant
            isNativeVariant = isNativeVariant || parameterModel.isNativeVariant

            parameters.add(parameterModel)
            setSupported(parameterModel.supportedState, parameterModel.isSupported)
            if (parameterModel.isCallback) {
                parameterModel.callbackModel?.let { callbackModel.add(it) }
            }
        }
        isConstructorType = "new" == method.getName()
    }

    fun hasCallback(): Boolean {
        return callbackModel.isNotEmpty()
    }

    override fun toString(): String {
        val result = StringBuilder().append(DebugPrint.colon(supportedState,returnType.toString(),apiName))
        for (p in parameters) {
            result.append("\n        ").append(p.toString())
        }
        return result.toString()
    }

    val apiName: String
        get() = Names.getJavaMethodName(name)

    val signalNameVariable: String
        get() = Names.getSignalNameConstantName(name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodModel

        if (name != other.name) return false
        if (gtkName != other.gtkName) return false
        if (isNativeVariant != other.isNativeVariant) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + gtkName.hashCode()
        return result
    }
}
