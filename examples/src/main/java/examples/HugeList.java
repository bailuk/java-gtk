package examples;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.bridge.ListIndex;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListView;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SignalListItemFactory;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.Str;

public class HugeList {
    public HugeList() {
        Application app = new Application(App.ID, ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            window.setTitle(new Str("Huge List"));
            window.setDefaultSize(640,320);

            var factory = new SignalListItemFactory();

            factory.onSetup(item->{
                var label = new Label(new Str("Test"));
                item.setChild(label);
            });

            var listIndex = new ListIndex();
            var model = new ListModel(listIndex.cast());
            String[] strings = {"Hallo", "Hans"};
            listIndex.setSize(strings.length);

            var singleSelection = new SingleSelection(model).cast();
            var selectionModel = new SelectionModel(singleSelection);
            selectionModel.isSelected(1);
            selectionModel.selectItem(1, GTK.TRUE);
            selectionModel.isSelected(1);
            var list = new ListView(null, factory);

            factory.onBind(item->{
                var label = new Label(item.getChild().cast());
                //strings[(int) listIndex.getPosition()])
                label.setLabel(new Str("test"));
            });

            var scrolled = new ScrolledWindow();
            window.setChild(scrolled);
            scrolled.setChild(list);
            window.show();
        });

        app.run(0, null);
    }
}


