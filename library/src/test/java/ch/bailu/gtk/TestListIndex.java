package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import ch.bailu.gtk.bridge.ListIndex;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gio.ListModelInterface;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Int64;

public class TestListIndex {

    static {
        try {
            GTK.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListIndex() {
        ListIndex listIndex = new ListIndex(5);
        assertEquals(5, listIndex.getSize());
        assertEquals(0, listIndex.getPosition());
        listIndex.setPosition(2);
        assertEquals(2, listIndex.getPosition());

        var listModel = new ListModel(listIndex.cast());
        assertEquals(1, new Int64(listModel.getItem(1).cast()).get());

        var singleSelection = new SingleSelection(listModel);
        //singleSelection.setSelected(3);
        //assertEquals(3,singleSelection.getSelected());

        //var selectionModel = new SelectionModel(singleSelection.cast());
        //selectionModel.selectItem(4,GTK.TRUE);
        //assertEquals(GTK.TRUE, selectionModel.isSelected(4));
    }
}
