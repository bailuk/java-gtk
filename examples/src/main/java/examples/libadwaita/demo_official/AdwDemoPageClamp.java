package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageClamp extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageClamp.class.getSimpleName());

    private static long type = 0;
    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-clamp.ui");
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
