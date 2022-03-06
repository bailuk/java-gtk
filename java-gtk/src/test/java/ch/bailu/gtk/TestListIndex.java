package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.bridge.ListIndex;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;

public class TestListIndex {


    @Test
    public void testListIndex() {
        ListIndex listIndex = new ListIndex(5);
        assertEquals(5, listIndex.getSize());
        assertEquals(0, listIndex.getIndex());
        listIndex.setIndex(2);
        assertEquals(2, listIndex.getIndex());

        var listModel = new ListModel(listIndex.cast());
        assertEquals(1, new ListIndex(listModel.getItem(1).cast()).getIndex());
        assertEquals(1, new ListIndex(listModel.getObject(1).cast()).getIndex());

        listIndex.setSize(10);
        assertEquals(10, listModel.getNItems());

        var singleSelection = new SingleSelection(listModel);
        assertEquals(0, singleSelection.getSelected());
        listIndex.setSize(10);

        singleSelection.setSelected(3);
        assertEquals(3,singleSelection.getSelected());

        var selectionModel = new SelectionModel(singleSelection.cast());
        selectionModel.selectItem(4,GTK.TRUE);
        assertEquals(GTK.TRUE, selectionModel.isSelected(4));
    }
}
