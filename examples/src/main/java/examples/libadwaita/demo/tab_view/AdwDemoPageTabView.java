package examples.libadwaita.demo.tab_view;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageTabView extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageTabView.class.getSimpleName());

    private static long type = 0;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                System.err.println("Adw.TabOverview since: 1.3");
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-tab-view.ui");

                widgetClass.installAction("demo.run", null, (__self1, widget, action_name, parameter) -> {
                    var window = new AdwTabViewDemoWindow();
                    window.setTransientFor(new Window(widget.getRoot().cast()));
                    window.present();
                });
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
