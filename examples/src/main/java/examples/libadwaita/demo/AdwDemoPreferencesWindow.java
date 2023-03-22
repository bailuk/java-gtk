package examples.libadwaita.demo;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.PreferencesWindow;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPreferencesWindow  extends PreferencesWindow {

    private final static Str TYPE_NAME = new Str(AdwDemoPreferencesWindow.class.getSimpleName());
    private final static long PARENT_TYPE = PreferencesWindow.getTypeID();
    private final static int PARENT_INSTANCE_SIZE = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    public AdwDemoPreferencesWindow(TypeInstance self) {
        super(self.cast());
        initTemplate();
        this.instance = new Instance(getCPointer());
    }

    public AdwDemoPreferencesWindow(long self) {
        super(toCPointer(self));
        this.instance = new Instance(getCPointer());
    }

    public AdwDemoPreferencesWindow(CPointer cast) {
        super(cast);
        this.instance = new Instance(getCPointer());
    }

    public AdwDemoPreferencesWindow() {
        this(TypeSystem.newInstance(getTypeID()));
    }


    @Structure.FieldOrder({"parent", "subpage1", "subpage2"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_INSTANCE_SIZE];
        public long subpage1;
        public long subpage2;
    }

    private final Instance instance;

    private static long type = 0;
    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, PARENT_INSTANCE_SIZE + 2*8, classInit, instanceInit);
        }
        return type;
    }

    private static Gobject.OnClassInitFunc classInit = (__self, g_class, class_data) -> {
        var widgetClass = new WidgetClassExtended(g_class.cast());
        widgetClass.setTemplateOrExit("/adw_demo/adw-demo-preferences-window.ui");

        widgetClass.bindTemplateChildFull("subpage1", true, PARENT_INSTANCE_SIZE);
        widgetClass.bindTemplateChildFull("subpage2", true, PARENT_INSTANCE_SIZE+8);

        widgetClass.bindTemplateCallback("return_to_preferences_cb", new Callback() {
            public void invoke(long self) {
                new AdwDemoPreferencesWindow(self).onReturnToPreferences();
            }

        });

        widgetClass.bindTemplateCallback("subpage1_activated_cb", new Callback() {
            public void invoke(long self) {
                new AdwDemoPreferencesWindow(self).onSubpage1Activated();
            }

        });

        widgetClass.bindTemplateCallback("subpage2_activated_cb", new Callback() {
            public void invoke(long self) {
                new AdwDemoPreferencesWindow(self).onSubpage2Activated();
            }
        });

        widgetClass.installAction("toast.show", null, (__self1, widget, action_name, parameter) -> new AdwDemoPreferencesWindow(widget.cast()).onShowToast());

    };

    private void onShowToast() {
        addToast(new Toast("Example Toast"));
    }

    private void onSubpage1Activated() {
        presentSubpage(new Widget(toCPointer(instance.subpage1)));
    }

    private void onSubpage2Activated() {
        presentSubpage(new Widget(toCPointer(instance.subpage2)));
    }

    private void onReturnToPreferences() {
        closeSubpage();
    }

    private static Gobject.OnInstanceInitFunc instanceInit = (__self, instance, g_class) -> new AdwDemoPreferencesWindow(instance);

}
