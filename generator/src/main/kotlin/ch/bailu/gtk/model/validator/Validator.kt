package ch.bailu.gtk.model.validator

object Validator {
    fun giveUp(reason: String) {
        throw RuntimeException("Validator: $reason")
    }
}
