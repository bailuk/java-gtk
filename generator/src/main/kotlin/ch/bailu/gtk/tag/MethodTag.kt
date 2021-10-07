package ch.bailu.gtk.tag

open class MethodTag(parent: TagWithParent): NamedWithDocTag(parent) {
    private val returnValue = ParameterTag(this)

    private val parameters = TagList<ParameterTag>()
    private val instanceParameters = TagList<ParameterTag>()

    private var identifier = ""

    private var deprecated = false
    private var throwsError = false


    private inner class Parameters : TagWithParent(this@MethodTag) {
        override fun getChild(name: String, prefix: String): TagWithParent {
            if ("parameter" == name) {
                return parameters.addTag(ParameterTag(this))
            }
            if ("instance-parameter" == name) {
                return instanceParameters.addTag(ParameterTag(this))
            }
            return ignore()
        }
    }


    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("return-value" == name) {
            return returnValue
        }
        if ("parameters" == name) {
            return Parameters()
        }
        return super.getChild(name, prefix)
    }

    override fun setAttribute(name: String, value: String) {
        if ("identifier" == name) {
            identifier = value
        } else if ("throws" == name) {
            throwsError = "1" == value
        } else if ("deprecated" == name) {
            deprecated = "1" == value
        } else {
            super.setAttribute(name, value)
        }
    }

    fun getParameters(): List<ParameterTag> {
        return parameters
    }

    fun getReturnValue(): ParameterTag {
        return returnValue
    }

    fun getIdentifier(): String {
        return identifier
    }

    fun isDeprecated(): Boolean {
        return deprecated
    }

    fun throwsError(): Boolean {
        return throwsError
    }
}