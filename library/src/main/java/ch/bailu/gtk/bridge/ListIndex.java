package ch.bailu.gtk.bridge;

import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Pointer;

public class ListIndex extends Pointer {

    public ListIndex() {
        super(new CPointer(ImpListIndex.create()));
    }

    public ListIndex(int size) {
        super(new CPointer(ImpListIndex.create()));
        ImpListIndex.setSize(getCPointer(), size);
    }

    public ListIndex(ListItem item) {
        super(item.getItem().cast());
    }

    public ListIndex(CPointer cast) {
        super(cast);
    }

    public int getIndex() {
        return ImpListIndex.getIndex(getCPointer());
    }

    public int getSize() {
        return ImpListIndex.getSize(getCPointer());
    }

    public void setSize(int size) {
        ImpListIndex.setSize(getCPointer(), size);
    }

    public void setIndex(int index) {
        ImpListIndex.setIndex(getCPointer(), index);
    }

    public ListModel asListModel() {
        return new ListModel(cast());
    }

    public SingleSelection inSingleSelection() {
        return new SingleSelection(asListModel());
    }

    public SelectionModel inSelectionModel() {
        return new SelectionModel(inSingleSelection().cast());
    }

    public static int toIndex(ListItem item) {
        return new ListIndex(item).getIndex();
    }
}
