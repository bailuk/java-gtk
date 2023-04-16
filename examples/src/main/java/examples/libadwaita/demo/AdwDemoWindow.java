package examples.libadwaita.demo;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.ColorScheme;
import ch.bailu.gtk.adw.Leaflet;
import ch.bailu.gtk.adw.NavigationDirection;
import ch.bailu.gtk.adw.StyleManager;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gdk.ModifierType;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;
import examples.libadwaita.demo.animations.AdwDemoPageAnimations;
import examples.libadwaita.demo.styles.AdwDemoPageStyles;

public class AdwDemoWindow extends ApplicationWindow {

    private static final Str                 TYPE_NAME         = new Str(AdwDemoWindow.class.getSimpleName());
    private static final long                PARENT_TYPE       = ApplicationWindow.getTypeID();
    private static final int                 OFFSET            = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;
    private static final Str                 APP_PROPERTY_NAME = new Str("application");

    private static final Str                 ICON_LIGHT        = new Str("light-mode-symbolic");
    private static final Str                 ICON_DARK         = new Str("dark-mode-symbolic");
    private static int                       INSTANCE_SIZE     = 4 * 8;

    private static long type = 0;

    @Structure.FieldOrder({"parent", "color_scheme_button", "main_leaflet", "subpage_leaflet", "toasts_page"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[OFFSET];
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
        TypeSystem.ensure(AdwDemoPageViewSwitcher.getTypeID());
        TypeSystem.ensure(AdwDemoPageCarousel.getTypeID());
        TypeSystem.ensure(AdwDemoPageAvatar.getTypeID());
        TypeSystem.ensure(AdwDemoPageFlap.getTypeID());
        TypeSystem.ensure(AdwDemoPageToasts.getTypeID());
        TypeSystem.ensure(AdwDemoPageAbout.getTypeID());
        TypeSystem.ensure(AdwDemoPageStyles.getTypeID());
        TypeSystem.ensure(AdwDemoPageDialogs.getTypeID());
        TypeSystem.ensure(AdwDemoPageButtons.getTypeID());
        TypeSystem.ensure(AdwDemoPageAnimations.getTypeID());

        initTemplate();
        this.instance = new Instance(asCPointer());
        StyleManager manager = StyleManager.getDefault();
        manager.connectSignal("notify::system-supports-color-schemes", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).notifySystemSupportsColorSchemes());
        notifySystemSupportsColorSchemes();
        new Leaflet(cast(this.instance.main_leaflet)).navigate(NavigationDirection.FORWARD);
    }

    public AdwDemoWindow(Application app) {
        this(TypeSystem.newInstance(getTypeID(), new TypeSystem.Property(APP_PROPERTY_NAME, app)));
    }


    public AdwDemoWindow(PointerContainer self) {
        super(self);
        this.instance = new Instance(asCPointer());
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
        widgetClass.bindTemplateChildFull(new Str("color_scheme_button"), true, OFFSET);
        widgetClass.bindTemplateChildFull(new Str("main_leaflet"), true, OFFSET + 8);
        widgetClass.bindTemplateChildFull(new Str("subpage_leaflet"), true, OFFSET + 16);
        widgetClass.bindTemplateChildFull(new Str("toasts_page"), true, OFFSET + 24);
        widgetClass.bindTemplateCallback("get_color_scheme_icon_name", (SignalCallbackSB) (self, dark) -> new AdwDemoWindow(cast(self)).getColorSchemeIconName(dark));
        widgetClass.bindTemplateCallback("color_scheme_button_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).onColorSchemeButtonClicked());
        widgetClass.bindTemplateCallback("notify_visible_child_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).notifyVisibleChild());
        widgetClass.bindTemplateCallback("back_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).backClicked());
        widgetClass.bindTemplateCallback("leaflet_back_clicked_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).onLeafletBackClicked());
        widgetClass.bindTemplateCallback("leaflet_next_page_cb", (SignalHandler.SignalCallback) self -> new AdwDemoWindow(cast(self)).onLeafletNextPage());
        widgetClass.installAction(new Str("toast.undo"), Str.NULL, (__self1, widget, action_name, parameter) -> new AdwDemoWindow(widget.cast()).toastUndo());

        __self.unregister();
    };

    private static Gobject.OnInstanceInitFunc instanceInit = (__self, instance, g_class) -> new AdwDemoWindow(instance);

    private void toastUndo() {
    }

    private void onLeafletNextPage() {
        new Leaflet(cast(instance.subpage_leaflet)).navigate(NavigationDirection.FORWARD);
    }

    private void onLeafletBackClicked() {
        new Leaflet(cast(instance.subpage_leaflet)).navigate(NavigationDirection.BACK);
    }

    private void backClicked() {
        new Leaflet(cast(instance.main_leaflet)).navigate(NavigationDirection.BACK);
    }

    private void notifyVisibleChild() {
    }

    private void notifySystemSupportsColorSchemes() {
        System.out.println("onNotifySystemSupportsColorSchemes");
    }

    private void onColorSchemeButtonClicked() {
        var styleManager = StyleManager.getDefault();
        if (styleManager.getDark()) {
            styleManager.setColorScheme(ColorScheme.FORCE_LIGHT);
        } else {
            styleManager.setColorScheme(ColorScheme.FORCE_DARK);
        }
    }

    private long getColorSchemeIconName(boolean dark) {
        return (dark) ? ICON_LIGHT.asCPointer() : ICON_DARK.asCPointer();
    }

    @FunctionalInterface
    public interface SignalCallbackSB extends Callback {
        long invoke(long self, boolean b);
    }
}
