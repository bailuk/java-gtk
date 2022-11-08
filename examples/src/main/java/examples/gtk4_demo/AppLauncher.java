package examples.gtk4_demo;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gio.AppInfo;
import ch.bailu.gtk.gio.Gio;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gio.ListStore;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.ButtonsType;
import ch.bailu.gtk.gtk.DialogFlags;
import ch.bailu.gtk.gtk.IconSize;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.ListView;
import ch.bailu.gtk.gtk.MessageDialog;
import ch.bailu.gtk.gtk.MessageType;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SignalListItemFactory;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class AppLauncher implements DemoInterface {

    private static final Str title = new Str("Application Launcher");

    @Override
    public Str getTitle() {
        return title;
    }

    @Override
    public Str getDescription() {
        return title;
    }

    @Override
    public Window runDemo() {
        var window = new Window();
        var factory = new SignalListItemFactory();
        window.setDefaultSize(640, 320);

        factory.onSetup(item -> {
            var box = new Box(Orientation.HORIZONTAL, 12);
            var image = new Image();
            image.setIconSize(IconSize.LARGE);
            box.append(image);
            var label = new Label(new Str(""));
            box.append(label);
            new ListItem(item.cast()).setChild(box);
        });

        factory.onBind(item -> {
            var image = new Image(new ListItem(item.cast()).getChild().getFirstChild().cast());
            var label = new Label(image.getNextSibling().cast());
            var appInfo = new AppInfo(new ListItem(item.cast()).getItem().cast());

            var icon = appInfo.getIcon();

            if (icon.isNotNull()) {
                image.setFromGicon(appInfo.getIcon());
            }
            label.setLabel(appInfo.getDisplayName());
        });

        var model = createApplicationList();

        var list = new ListView(new SelectionModel(new SingleSelection(model).cast()), factory);

        list.onActivate(position -> {
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
        return window;
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
