package ch.bailu.gtk.validator

object Validator {
    fun giveUp(reason: String, condition: Boolean) {
        if (condition) {
            giveUp(reason)
        }
    }

    fun giveUp(reason: String) {
        throw RuntimeException("Validator: $reason")
    }
}
