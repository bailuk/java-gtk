package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.SignalFlags;
import ch.bailu.gtk.gobject.TypeClass;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageLists {

    private final static Str TYPE_NAME = new Str(AdwDemoPageLists.class.getSimpleName());

    private static long type = 0;

    private static int signal = 0;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());

                signal = GObjectLib.INST().g_signal_new(
                        new Str("add-toast").getCPointer(),
                        new TypeClass(g_class.cast()).getFieldGType(),
                        SignalFlags.RUN_FIRST,
                        0,
                        0,
                        0,
                        0,
                        1 << GobjectConstants.TYPE_FUNDAMENTAL_SHIFT,
                        1, Toast.getTypeID());

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-list.ui");
                widgetClass.bindTemplateCallback("entry_apply_cb", (SignalHandler.SignalCallback) self -> {
                    var toast = new Toast("Changes applied");
                    Gobject.signalEmit(Pointer.toPointer(self), signal, 0, toast.getCPointer());
                });
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
