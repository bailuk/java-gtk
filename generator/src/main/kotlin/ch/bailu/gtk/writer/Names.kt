package ch.bailu.gtk.writer

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.table.ReservedTokenTable.convert

object Names {

    /**
     * Convert any name into a valid and conventional Java method name
     * Convert to lower camel case and replace reserved words
     * Example "get_file_manager" -> "getFileManager"
     */
    fun getJavaMethodName(name: String): String {
        if (name.length < 3) {
            return name
        }
        val result = StringBuilder()
        val names = name.split("_".toRegex()).filter { it.isNotEmpty() }

        result.append(names[0])
        for (i in 1 until names.size) {
            firstUpper(result, names[i])
        }
        return fixToken(result.toString())
    }


    /**
     * Convert any name into a valid and conventional Java class name
     * Convert to upper camel case and change reserved words
     * Example "file_manager" -> "FileManager"
     */
    fun getJavaClassName(name: String): String {
        val result = StringBuilder()
        val names = name.split("_".toRegex()).toTypedArray()
        for (i in names.indices) {
            firstUpper(result, names[i])
        }
        return fixToken(result.toString())
    }


    private fun firstUpper(result: StringBuilder, s: String) {
        if (s.isNotEmpty()) {
            result.append(Character.toUpperCase(s[0]))
            if (s.length > 1) {
                result.append(s.substring(1))
            }
        } else {
            result.append(s)
        }
    }


    fun getJavaCallbackMethodName(name: String): String {
        return getJavaSignalName("on", name)
    }

    /**
     * Add callback interface prefix to already valid class name
     */
    fun getJavaCallbackInterfaceName(name: String): String {
        return getJavaSignalName("On", name)
    }

    private fun getJavaSignalName(prefix: String, name: String): String {
        val result = StringBuilder()
        result.append(prefix)
        val names = name.split("[\\-_]".toRegex()).toTypedArray()
        for (i in names.indices) {
            firstUpper(result, names[i])
        }
        return fixToken(result.toString())
    }


    private fun fixToken(token: String): String {
        return convert(token)
    }


    /**
     * Get valid and conventional (lower camel case) function name with
     * field setter prefix
     */
    fun getJavaFieldSetterName(name: String): String {
        return getJavaMethodName("set_field_$name")
    }

    /**
     * Get valid and conventional (lower camel case) function name with
     * field getter prefix
     */
    fun getJavaFieldGetterName(name: String): String {
        return getJavaMethodName("get_field_$name")
    }


    /**
     * Convert to valid and conventional Java class Name
     * @see getJavaClassName
     * and add "Constants" postfix
     */
    fun getJavaPackageConstantsInterfaceName(namespace: String): String {
        return getJavaClassName(namespace) + "Constants"
    }

    /**
     * Add prefix for implementation class to already valid class name
     *
     */
    fun getImpClassName(apiName: String): String {
        return "Jna${apiName}"
    }

    /**
     * Return name into valid and conventional java constant name
     * Replaces reserved keywords
     */
    fun getJavaConstantName(name: String): String {
        return fixToken(name.uppercase())
    }

    /**
     * Return name into valid and conventional java name
     * Replaces reserved keywords
     */
    fun getJavaVariableName(name: String): String {
        return fixToken(name)
    }

    /**
     * Add complete namespace prefix to already valid Java name
     * @param namespace subpackage name like "gobject"
     * @param name valid java class name
     */
    fun getJavaClassNameWithNamespacePrefix(namespace: String, name: String): String {
        return if (name.startsWith(Configuration.BASE_NAME_SPACE_DOT)) {
            name
        } else {
            "${Configuration.BASE_NAME_SPACE_DOT}${namespace}.${name}"
        }
    }

    /**
     * Convert to valid and conventional Java constant name
     * Add signal prefix
     */
    fun getSignalNameConstantName(name: String): String {
        return "SIGNAL_ON_${name.uppercase().replace('-', '_')}"
    }

    /**
     * Return API type name relative to namespace
     * example "Widget" or "ch.bailu.java-gtk.gtk.Widget"
     */
    fun getApiTypeName(type: NamespaceType, namespace: String = ""): String {
        return if (!type.isCurrentNameSpace(namespace)) {
            getJavaClassNameWithNamespacePrefix(type.namespace, type.name)
        } else type.name
    }


}
