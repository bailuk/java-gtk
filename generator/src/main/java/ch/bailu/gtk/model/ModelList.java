package ch.bailu.gtk.model;

import java.util.ArrayList;
import java.util.List;

public class ModelList<T extends Model> extends ArrayList<T> {

    private final List<Model> unsupported;

    public ModelList() {
        this.unsupported = new ArrayList<>();
    }

    public ModelList(List<Model> unsupported) {
        this.unsupported = unsupported;
    }


    public void addIfSupported(T model) {
        if (model.isSupported()) {
            add(model);
        } else {
            unsupported.add(model);
        }
    }


}
