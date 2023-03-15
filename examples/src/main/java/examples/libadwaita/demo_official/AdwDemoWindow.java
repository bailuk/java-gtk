package examples.libadwaita.demo_official;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.Leaflet;
import ch.bailu.gtk.adw.NavigationDirection;
import ch.bailu.gtk.adw.StyleManager;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gdk.ModifierType;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoWindow extends ApplicationWindow {

    private static final Str                 TYPE_NAME         = new Str(AdwDemoWindow.class.getSimpleName());
    private static final long                PARENT_TYPE       = ApplicationWindow.getTypeID();
    private static final TypeSystem.TypeSize PARENT_SIZE       = TypeSystem.getTypeSize(PARENT_TYPE);
    private static final Str                 APP_PROPERTY_NAME = new Str("application");

    private static final Str                 ICON_LIGHT        = new Str("light-mode-symbolic");
    private static final Str                 ICON_DARK         = new Str("dark-mode-symbolic");
    private static int                       INSTANCE_SIZE     = 4 * 8;

    private static long type = 0;

    @Structure.FieldOrder({"parent", "color_scheme_button", "main_leaflet", "subpage_leaflet", "toasts_page"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_SIZE.instanceSize];
        public long color_scheme_button;
        public long main_leaflet;
        public long subpage_leaflet;
        public long toasts_page;
    }
    private final Instance instance;


    public AdwDemoWindow(TypeInstance instance) {
        super(instance.cast());

        TypeSystem.ensure(AdwDemoPageWelcome.getTypeID());
        TypeSystem.ensure(AdwDemoPageLeaflet.getTypeID());
        TypeSystem.ensure(AdwDemoPageClamp.getTypeID());
        TypeSystem.ensure(AdwDemoPageLists.getTypeID());

        initTemplate();
        this.instance = new Instance(getCPointer());
        StyleManager manager = StyleManager.getDefault();
        manager.connectSignal("notify::system-supports-color-schemes", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).notifySystemSupportsColorSchemes());
        notifySystemSupportsColorSchemes();
        new Leaflet(new CPointer(this.instance.main_leaflet)).navigate(NavigationDirection.FORWARD);
    }

    public AdwDemoWindow(Application app) {
        this(TypeSystem.newInstance(getTypeID(), new TypeSystem.Property(APP_PROPERTY_NAME, app)));
    }

    public AdwDemoWindow(long self) {
        this(new CPointer(self));
    }

    public AdwDemoWindow(CPointer self) {
        super(self);
        this.instance = new Instance(getCPointer());
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, INSTANCE_SIZE, classInit, instanceInit);
        }
        return type;
    }

    private static Gobject.OnClassInitFunc classInit = (__self, g_class, class_data) -> {
        var widgetClass = new WidgetClassExtended(g_class.cast());
        widgetClass.addBindingAction(GdkConstants.KEY_q, ModifierType.CONTROL_MASK, new Str("window.close"), Str.NULL);
        widgetClass.setTemplateOrExit("/adw_demo/adw-demo-window.ui");
        widgetClass.bindTemplateChildFull(new Str("color_scheme_button"), true, PARENT_SIZE.instanceSize);
        widgetClass.bindTemplateChildFull(new Str("main_leaflet"), true, PARENT_SIZE.instanceSize + 8);
        widgetClass.bindTemplateChildFull(new Str("subpage_leaflet"), true, PARENT_SIZE.instanceSize + 16);
        //widgetClass.bindTemplateChildFull(new Str("toasts_page"), true, PARENT_SIZE.instanceSize + 24);
        widgetClass.bindTemplateCallback("get_color_scheme_icon_name", (SignalCallbackSB) (self, dark) -> new AdwDemoWindow(self).getColorSchemeIconName(dark));
        widgetClass.bindTemplateCallback("color_scheme_button_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).colorSchemeButtonClicked());
        widgetClass.bindTemplateCallback("notify_visible_child_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).notifyVisibleChild());
        widgetClass.bindTemplateCallback("back_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).backClicked());
        widgetClass.bindTemplateCallback("leaflet_back_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).leafletBackClicked());
        widgetClass.bindTemplateCallback("leaflet_next_page_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(self).leafletNextPage());
        widgetClass.installAction(new Str("toast.undo"), Str.NULL, (__self1, widget, action_name, parameter) -> new AdwDemoWindow(widget.cast()).toastUndo());

        __self.unregister();
    };

    private static Gobject.OnInstanceInitFunc instanceInit = (__self, instance, g_class) -> new AdwDemoWindow(instance);

    private void toastUndo() {
    }

    private void leafletNextPage() {
    }

    private void leafletBackClicked() {
    }

    private void backClicked() {
    }

    private void notifyVisibleChild() {
    }

    private void notifySystemSupportsColorSchemes() {
        System.out.println("onNotifySystemSupportsColorSchemes");
    }

    private void colorSchemeButtonClicked() {
    }

    private long getColorSchemeIconName(boolean dark) {
        return (dark) ? ICON_LIGHT.getCPointer() : ICON_DARK.getCPointer();
    }

    @FunctionalInterface
    public interface SignalCallbackSB extends Callback {
        long invoke(long self, boolean b);
    }
}
