package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Window;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwViewSwitcherDemoWindow extends Window {
    private final static Str TYPE_NAME = new Str(AdwViewSwitcherDemoWindow.class.getSimpleName());
    private static long type = 0;

    public AdwViewSwitcherDemoWindow() {
        super(TypeSystem.newInstance(getTypeID()));
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Window.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-view-switcher-demo-window.ui");
            }, (__self, instance, g_class) -> new Window(instance.cast()).initTemplate());
        }
        return type;
    }
}
