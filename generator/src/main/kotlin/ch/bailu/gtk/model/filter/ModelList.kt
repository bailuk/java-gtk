package ch.bailu.gtk.model.filter

import ch.bailu.gtk.model.Model

class ModelList<T : Model>(unsupported: ArrayList<Model>) : java.util.ArrayList<T>() {
    private val unsupported = unsupported

    constructor() : this(ArrayList())

    fun addIfSupported(model: T) {
        if (model.isSupported) {
            add(model)
        } else {
            unsupported.add(model)
        }
    }
}
