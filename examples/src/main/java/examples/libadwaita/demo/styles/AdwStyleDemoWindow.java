package examples.libadwaita.demo.styles;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Leaflet;
import ch.bailu.gtk.adw.NavigationDirection;
import ch.bailu.gtk.adw.Window;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.SelectionMode;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwStyleDemoWindow extends Window {


    private final static Str TYPE_NAME = new Str(AdwStyleDemoWindow.class.getSimpleName());
    private final static int OFFSET = TypeSystem.getTypeSize(Window.getTypeID()).instanceSize;
    private static long type = 0;

    private final static int PROP_DEVEL = 1;
    private final static Str PROP_DEVEL_NAME = new Str("devel");
    private final static Str PROP_DEVEL_ACTION = new Str("style.devel");
    private final static int PROP_PROGRESS = 2;
    private final static Str PROP_PROGRESS_NAME = new Str("progress");
    private final static Str PROP_PROGRESS_ACTION = new Str("style.progress");

    private final static Str HEADER_BAR_ACTION = new Str("style.header-bar");
    private final static Str STATUS_PAGE_ACTION = new Str("style.status-page");
    private final static Str SIDEBAR_ACTION = new Str("style.sidebar");
    private final static Str DUMMY_ACTION = new Str("style.dummy");

    @Structure.FieldOrder({"parent", "header_bar_window", "status_page_window", "sidebar_window", "sidebar_leaflet",  "progress"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[OFFSET];
        public long header_bar_window;
        public long status_page_window;
        public long sidebar_window;
        public long sidebar_leaflet;
        public boolean progress;

    }

    private final Instance instance;


    public AdwStyleDemoWindow() {
        this(TypeSystem.newInstance(getTypeID()));
    }

    public AdwStyleDemoWindow(long self) {
        this(cast(self));
    }

    public AdwStyleDemoWindow(PointerContainer self) {
        super(self);
        instance = new Instance(asCPointer());
    }


    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Window.getTypeID(), TYPE_NAME, 4 * 8 + 4, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());


                objectClass.overrideSetProperty((__self16, object, property_id, value, pspec) -> new AdwStyleDemoWindow(object.cast()).setProperty(property_id, value));
                objectClass.overrideGetProperty((__self17, object, property_id, value, pspec) -> new AdwStyleDemoWindow(object.cast()).getProperty(property_id, value));

                objectClass.overrideDispose((__self15, object) -> new AdwStyleDemoWindow(object.cast()).onDispose(objectClass.getParentClass()));

                var propDevel = Gobject.paramSpecBoolean(PROP_DEVEL_NAME, null, null, false, ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                objectClass.installProperty(PROP_DEVEL, propDevel);

                var probProgress = Gobject.paramSpecBoolean(PROP_PROGRESS_NAME, null, null, false, ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                objectClass.installProperty(PROP_PROGRESS, probProgress);

                widgetClass.setTemplateOrExit("/adw_demo/adw-style-demo-window.ui");
                widgetClass.bindTemplateChildFull("header_bar_window", true, OFFSET);
                widgetClass.bindTemplateChildFull("status_page_window", true, OFFSET + 8);
                widgetClass.bindTemplateChildFull("sidebar_window", true, OFFSET + 16);
                widgetClass.bindTemplateChildFull("sidebar_leaflet", true, OFFSET + 24);

                widgetClass.bindTemplateCallback("selection_mode_for_folded", new Callback() {
                    public int invoke(long data, boolean folded) { return folded ? SelectionMode.NONE : SelectionMode.BROWSE; }
                });

                widgetClass.bindTemplateCallback("sidebar_back_cb", new Callback() {
                    public void invoke(long self) { new AdwStyleDemoWindow(self).onSidebarBack(); }
                });

                widgetClass.bindTemplateCallback("sidebar_forward_cb", new Callback() {
                    public void invoke(long self) { new AdwStyleDemoWindow(self).onSidebarForward(); }
                });

                widgetClass.installPropertyAction(PROP_DEVEL_ACTION, PROP_DEVEL_NAME);
                widgetClass.installPropertyAction(PROP_PROGRESS_ACTION, PROP_PROGRESS_NAME);

                widgetClass.installAction(HEADER_BAR_ACTION, null, (__self1, widget, action_name, parameter) -> new AdwStyleDemoWindow(widget.cast()).onHeaderBar());
                widgetClass.installAction(STATUS_PAGE_ACTION, null, (__self12, widget, action_name, parameter) -> new AdwStyleDemoWindow(widget.cast()).onStatusBar());
                widgetClass.installAction(SIDEBAR_ACTION, null, (__self13, widget, action_name, parameter) -> new AdwStyleDemoWindow(widget.cast()).onSidebar());
                widgetClass.installAction(DUMMY_ACTION, null, (__self14, widget, action_name, parameter) -> new AdwStyleDemoWindow(widget.cast()).onDummy());
            }, (__self, instance, g_class) -> new Window(instance.cast()).initTemplate());
        }
        return type;
    }

    private void onDummy() {
        System.out.println("onDummy()");
    }

    private void onSidebar() {
        new ch.bailu.gtk.gtk.Window(cast(instance.sidebar_window)).present();
    }

    private void onStatusBar() {
        new ch.bailu.gtk.gtk.Window(cast(instance.status_page_window)).present();
    }

    private void onHeaderBar() {
        new ch.bailu.gtk.gtk.Window(cast(instance.header_bar_window)).present();
    }

    private void onSidebarForward() {
        new Leaflet(cast(instance.sidebar_leaflet)).navigate(NavigationDirection.FORWARD);
    }

    private void onSidebarBack() {
        new Leaflet(cast(instance.sidebar_leaflet)).navigate(NavigationDirection.BACK);
    }

    private void onDispose(ObjectClassExtended parentClass) {
        new Window(cast(instance.header_bar_window)).destroy();
        new Window(cast(instance.status_page_window)).destroy();
        new Window(cast(instance.sidebar_window)).destroy();

        parentClass.onDispose(this);
    }

    private void getProperty(int propertyId, Value value) {
        if (propertyId == PROP_DEVEL) {
            value.setBoolean(hasCssClass(PROP_DEVEL_NAME));
        } else if (propertyId == PROP_PROGRESS) {
            value.setBoolean((instance.progress));
        } else {
            System.err.println("Invalid property ID: " +  propertyId);
        }
    }

    private void setProperty(int propertyId, Value value) {
        if (propertyId == PROP_DEVEL) {
            setDevelStyle(value.getBoolean());
        } else if (propertyId == PROP_PROGRESS) {
            instance.progress = value.getBoolean();
            instance.writeField("progress");
        } else {
            System.err.println("Invalid property ID: " + propertyId);
        }
    }

    private void setDevelStyle(boolean devel) {
        if (devel) {
            addCssClass(PROP_DEVEL_NAME);
            new Widget(cast(instance.header_bar_window)).addCssClass(PROP_DEVEL_NAME);
            new Widget(cast(instance.status_page_window)).addCssClass(PROP_DEVEL_NAME);
        } else {
            removeCssClass(PROP_DEVEL_NAME);
            new Widget(cast(instance.header_bar_window)).removeCssClass(PROP_DEVEL_NAME);
            new Widget(cast(instance.status_page_window)).removeCssClass(PROP_DEVEL_NAME);
        }
    }
}
