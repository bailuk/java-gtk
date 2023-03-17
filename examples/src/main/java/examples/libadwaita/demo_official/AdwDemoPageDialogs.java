package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageDialogs {
    private final static Str TYPE_NAME = new Str(AdwDemoPageDialogs.class.getSimpleName());

    private static long type = 0;
    private static int signal;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                signal = objectClass.signalNew(AdwDemoPageToasts.ADD_TOAST_SIGNAL, TypeSystem.GTYPE_NONE , Toast.getTypeID());

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-dialogs.ui");
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
