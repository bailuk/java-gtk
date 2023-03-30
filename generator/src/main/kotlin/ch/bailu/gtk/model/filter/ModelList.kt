package ch.bailu.gtk.model.filter

import ch.bailu.gtk.model.Model

class ModelList<T : Model>(private val unsupported: ArrayList<Model> = ArrayList()) : java.util.ArrayList<T>() {

    fun addIfSupported(model: T) {
        addIfSupported(model, model.isSupported)
    }

    fun addIfSupported(model: T, supported: Boolean) {
        if (supported) {
            add(model)
        } else {
            unsupported.add(model)
        }
    }

    override fun add(element: T): Boolean {
        return if (contains(element)) {
            false
        } else {
            super.add(element)
        }
    }
}
