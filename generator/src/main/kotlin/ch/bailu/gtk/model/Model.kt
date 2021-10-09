package ch.bailu.gtk.model

abstract class Model {
    var isSupported = true
        private set

    var supportedState = "Supported"
        private set

    fun setSupported(reason: String, supported: Boolean) {
        if (!supported) {
            if (isSupported) {
                isSupported = false
                supportedState = reason
            } else {
                supportedState += ":${reason}"
            }
        }
    }
}
