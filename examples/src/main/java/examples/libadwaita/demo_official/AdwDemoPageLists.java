package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageLists {
    private final static Str TYPE_NAME = new Str(AdwDemoPageLists.class.getSimpleName());
    private static long type = 0;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                final var signal = objectClass.signalNew(AdwDemoPageToasts.ADD_TOAST_SIGNAL, TypeSystem.GTYPE_NONE, Toast.getTypeID());

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-list.ui");
                widgetClass.bindTemplateCallback("entry_apply_cb", (SignalHandler.SignalCallback) self -> {
                    final var toast = new Toast("Changes applied");
                    Gobject.signalEmit(Pointer.toPointer(self), signal, 0, toast.getCPointer());
                });
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
