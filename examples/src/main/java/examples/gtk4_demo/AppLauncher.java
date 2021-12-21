package examples.gtk4_demo;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gio.AppInfo;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Gio;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gio.ListStore;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.ButtonsType;
import ch.bailu.gtk.gtk.DialogFlags;
import ch.bailu.gtk.gtk.IconSize;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListView;
import ch.bailu.gtk.gtk.MessageDialog;
import ch.bailu.gtk.gtk.MessageType;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SignalListItemFactory;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.Str;
import examples.App;

public class AppLauncher {

    public AppLauncher() {
        Application app = new Application(App.ID, ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            window.setTitle(new Str("Application Launcher"));
            window.setDefaultSize(640,320);

            var factory = new SignalListItemFactory();

            factory.onSetup(item->{
                var box = new Box(Orientation.HORIZONTAL, 12);
                var image = new Image();
                image.setIconSize(IconSize.LARGE);
                box.append(image);
                var label = new Label(new Str(""));
                box.append(label);
                item.setChild(box);

            });

            factory.onBind(item->{
                var image = new Image(item.getChild().getFirstChild().cast());
                var label = new Label(image.getNextSibling().cast());
                var appInfo = new AppInfo(item.getItem().cast());

                var icon = appInfo.getIcon();

                if (icon.isNotNull()) {
                    image.setFromGicon(appInfo.getIcon());
                }
                label.setLabel(appInfo.getDisplayName());
            });

            var model = createApplicationList();

            var list = new ListView(new SelectionModel(new SingleSelection(model).cast()), factory);

            list.onActivate(position->{
                var appInfo = new AppInfo(model.getItem(position).cast());
                var context = list.getDisplay().getAppLaunchContext();

                var result = GTK.FALSE;
                try {
                    result = appInfo.launch(null, context);
                } catch (AllocationError allocationError) {
                    result = GTK.FALSE;
                }

                if (result == GTK.FALSE) {
                    var dialog = new MessageDialog(
                            window,
                            DialogFlags.DESTROY_WITH_PARENT | DialogFlags.MODAL,
                            MessageType.ERROR,
                            ButtonsType.CLOSE,
                            new Str("Error launching app"));

                    dialog.onResponse(id -> dialog.close());
                    dialog.formatSecondaryMarkup(new Str("Error <b>ERROR</b>"));
                    dialog.show();
                }
            });

            var scrolled = new ScrolledWindow();
            window.setChild(scrolled);
            scrolled.setChild(list);
            window.show();
        });

        app.run(0, null);
    }

    private ListModel createApplicationList() {
        var apps = Gio.appInfoGetAll();

        var store = new ListStore(AppInfo.getTypeID());

        for (; apps.isNotNull(); apps = apps.getFieldNext()) {
            store.append(apps.getFieldData());
        }
        return new ListModel(store.cast());
    }
}
