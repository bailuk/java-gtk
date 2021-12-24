package examples;

import ch.bailu.gtk.bridge.ListIndex;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListView;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SignalListItemFactory;
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
                var label = new Label(new Str(""));
                item.setChild(label);
            });

            var listIndex = new ListIndex();
            String[] strings = {"Hallo", "Hans", "3"};
            listIndex.setSize(strings.length);

            var list = new ListView(listIndex.inSelectionModel(), factory);

            factory.onBind(item->{
                var label = new Label(item.getChild().cast());
                label.setLabel(new Str(strings[ListIndex.toIndex(item)]));
            });

            var scrolled = new ScrolledWindow();
            window.setChild(scrolled);
            scrolled.setChild(list);
            window.show();
        });

        app.run(0, null);
    }
}


