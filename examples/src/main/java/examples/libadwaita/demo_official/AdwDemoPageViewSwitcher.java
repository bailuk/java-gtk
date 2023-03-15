package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageViewSwitcher extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageViewSwitcher.class.getSimpleName());

    private static long type = 0;

    public AdwDemoPageViewSwitcher(CPointer pointer) {
        super(pointer);
    }


    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-view-switcher.ui");
                widgetClass.installAction("demo.run", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageViewSwitcher(widget.cast()).runDemo());
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }

    private void runDemo() {
        var window = new AdwViewSwitcherDemoWindow();
        window.setTransientFor(new Window(getRoot().cast()));
        window.present();
    }
}
