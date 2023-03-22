package examples.libadwaita.demo;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageFlap extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageFlap.class.getSimpleName());
    private static long type = 0;

    private AdwDemoPageFlap(CPointer self) {
        super(self);
    }

    private void onRunDemo() {
        var window = new AdwFlapDemoWindow();
        window.setTransientFor(new Window(this.getRoot().cast()));
        window.present();
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-flap.ui");
                widgetClass.installAction("demo.run", null, (__self1, self, action_name, parameter) -> new AdwDemoPageFlap(self.cast()).onRunDemo());
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }
}
