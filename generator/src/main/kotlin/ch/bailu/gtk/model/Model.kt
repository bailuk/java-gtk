package ch.bailu.gtk.model

abstract class Model {
    var isSupported = true
        private set

    var supportedState = "Supported"
        private set

    var isPublic = true
        private set

    var visibleState = "Public"
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

    fun setPrivate(reason: String, private: Boolean) {
        if (private) {
            if (isPublic) {
                isPublic = false
                this.visibleState = reason
            } else {
                this.visibleState += ":${reason}"
            }
        }
    }

    fun setPublic(reason: String, public: Boolean) {
        setPrivate(reason, !public)
    }

}
