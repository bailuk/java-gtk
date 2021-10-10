package ch.bailu.gtk.config

import ch.bailu.gtk.Configuration
import java.io.File
import java.io.IOException

data class NamespaceConfig(val girFile: String, val docUrl: DocUrl) {

    fun getFile(): File {
        var result = File(Configuration.GIR_DIR_CUSTOM, girFile)
        var type = "custom"
        if (!result.exists()) {
            result = File(Configuration.getInstance().girBaseDir, girFile)
            type = "system"
            if (!result.exists()) {
                result = File(Configuration.GIR_DIR_LOCAL, girFile)
                type = "local"
                if (!result.exists()) {
                    throw IOException("File does not exist $result")
                }
            }
        }
        println("  --> ${type}: ${result}")
        return result
    }
}