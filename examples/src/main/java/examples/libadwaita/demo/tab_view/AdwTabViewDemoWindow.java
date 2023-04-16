package examples.libadwaita.demo.tab_view;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.TabPage;
import ch.bailu.gtk.adw.TabView;
import ch.bailu.gtk.adw.Window;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.Icon;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.gio.ThemedIcon;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.gobject.BindingFlags;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwTabViewDemoWindow extends Window {

    private final static Str TYPE_NAME = new Str(AdwTabViewDemoWindow.class.getSimpleName());
    private final static int OFFSET = TypeSystem.getTypeSize(Window.getTypeID()).instanceSize;
    private static long type = 0;

    private static Str PROP_TOOLTIP_NAME = new Str("tooltip");

    public AdwTabViewDemoWindow(PointerContainer cast) {
        super(cast);
        instance = new Instance(asCPointer());
    }


    @Structure.FieldOrder({"parent", "view", "tab_bar", "tab_overview", "tab_action_group", "menu_page", "narrow", "in_dispose"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[OFFSET];
        public long view;
        public long tab_bar;
        public long tab_overview;
        public long tab_action_group;
        public long menu_page;
        public boolean narrow;
        public boolean in_dispose;
    }

    private final Instance instance;

    public AdwTabViewDemoWindow(long self) {
        super(cast(self));
        instance = new Instance(asCPointer());
    }

    private AdwTabViewDemoWindow(TypeInstance self) {
        super(self.cast());
        initTemplate();
        instance = new Instance(asCPointer());
    }

    public AdwTabViewDemoWindow() {
        super(TypeSystem.newInstance(getTypeID()));
        instance = new Instance(asCPointer());
    }

    private static final Str TOOLTIP_TEXT = new Str("Elaborate tooltip for <b>%s</b>");

    private boolean textToTooltip(Value input, Value output) {
        var title = input.getString();
        var tooltip = Glib.markupPrintfEscaped(TOOLTIP_TEXT, title.asCPointer());
        output.takeString(tooltip);
        return true;
    }


    private TabPage addPage(TabPage parent, AdwTabViewDemoPage content) {
        var page = new TabView(cast(instance.view)).addPage(content, parent);
        content.bindProperty(AdwTabViewDemoPage.PROP_TITLE_NAME, page, AdwTabViewDemoPage.PROP_TITLE_NAME, BindingFlags.SYNC_CREATE);
        content.bindPropertyFull(
                AdwTabViewDemoPage.PROP_TITLE_NAME,
                page,
                PROP_TOOLTIP_NAME,
                BindingFlags.SYNC_CREATE,
                (__self, binding, from_value, to_value, user_data) -> textToTooltip(from_value, to_value),
                null, null, null);
        content.bindProperty(AdwTabViewDemoPage.PROP_ICON_NAME, page, AdwTabViewDemoPage.PROP_ICON_NAME, BindingFlags.SYNC_CREATE);
        page.setIndicatorActivatable(true);
        // TODO since 1.3 adw_tab_page_set_thumbnail_xalign (page, 0.5);
        // TODO since 1.3 adw_tab_page_set_thumbnail_yalign (page, 0.5);
        return page;
    }

    private static int nextPage = 1;

    private TabPage onCreateTab() {
        var title = new Str("Tab " + nextPage++);
        var page = addPage(null, new AdwTabViewDemoPage(title));
        title.destroy();
        return page;
    }

    private void tabNew() {
        var page = onCreateTab();
        var content = page.getChild();
        getTabView().setSelectedPage(page);
        content.grabFocus();
    }

    private TabPage getCurrentPage() {
        var menuPage = getMenuPage();
        if (menuPage.isNotNull()) {
            return menuPage;
        }
        return getTabView().getSelectedPage();
    }

    private void tabPin() {
        getTabView().setPagePinned(getCurrentPage(), true);
    }

    private void tabUnpin() {
        getTabView().setPagePinned(getCurrentPage(), false);
    }

    private void tabClose() {
        getTabView().closePage(getCurrentPage());
    }

    private void tabCloseOther() {
        getTabView().closeOtherPages(getCurrentPage());
    }

    private void tabCloseBefore() {
        getTabView().closePagesBefore(getCurrentPage());
    }

    private void tabCloseAfter() {
        getTabView().closePagesAfter(getCurrentPage());
    }

    private void tabMoveToNewWindow() {
        var window = new AdwTabViewDemoWindow();
        getTabView().transferPage(getMenuPage(), window.getTabView(), 0);
        window.present();
    }


    private void tabChangeNeedsAttention(Variant parameter) {
        var needsAttention = parameter.getBoolean();
        getCurrentPage().setNeedsAttention(needsAttention);
    }

    private void tabChangeLoading(SimpleAction action, Variant parameter) {
        var loading = parameter.getBoolean();
        getCurrentPage().setLoading(loading);
        action.setState(Variant.newBooleanVariant(loading));
    }

    private static Icon getIndicatorIcon(TabPage page) {
        var muted = page.getData("adw-tab-view-demo-muted");
        if (muted.isNotNull()) {
            return new Icon(new ThemedIcon("tab-audio-muted-symbolic").cast());
        }
        return new Icon(new ThemedIcon("tab-audio-playing-symbolic").cast());
    }


    private static Str getIndicatorTooltip(TabPage page) {
        var muted = page.getData("adw-tab-view-demo-muted");
        if (muted.isNotNull()) {
            return new Str("Unmute Tab");
        }
        return new Str("Mute Tab");
    }


    private void tabChangeIndicator(SimpleAction action, Variant parameter) {
        var indicator =  parameter.getBoolean();

        Icon icon = null;
        Str toolTip;

        if (indicator) {
            icon = getIndicatorIcon(getCurrentPage());
            toolTip = getIndicatorTooltip(getCurrentPage());
        } else {
            toolTip = new Str("");
        }

        getCurrentPage().setIndicatorIcon(icon);
        getCurrentPage().setTooltip(toolTip);
        action.setState(Variant.newBooleanVariant(indicator));

        toolTip.destroy();
        if (icon != null) {
            new ch.bailu.gtk.gobject.Object(icon.cast()).unref();
        }
    }

    private void tabChangeIcon(Variant parameter) {
        var enableIcon = parameter.getBoolean();
        var child = new AdwTabViewDemoPage(getCurrentPage().cast());
        child.setEnableIcon(enableIcon);
    }

    private void tabRefreshIcon() {
        var child = new AdwTabViewDemoPage(getCurrentPage().cast());
        child.refreshIcon();
    }

    private void tabDuplicate() {
        var parent = getCurrentPage();
        var parentContent = parent.getChild();
        var content = new AdwTabViewDemoPage(new AdwTabViewDemoPage(parentContent.cast()));
        var page = addPage(parent, content);
        page.setIndicatorIcon(parent.getIndicatorIcon());
        page.setIndicatorTooltip(parent.getIndicatorTooltip());
        page.setLoading(parent.getLoading());
        page.setNeedsAttention(parent.getNeedsAttention());
        page.setData( "adw-tab-view-demo-muted", parent.getData("adw-tab-view-demo-muted"));
        getTabView().setSelectedPage(page);
    }

    private void setTabActionEnabled(String name, boolean enabled) {
        var action = new ActionMap(cast(instance.tab_action_group)).lookupAction(name);
        new SimpleAction(action.cast()).setEnabled(enabled);
    }

    private void setTabActionState(String name, boolean state) {
        var action = new ActionMap(cast(instance.tab_action_group)).lookupAction(name);
        new SimpleAction(action.cast()).setState(Variant.newBooleanVariant(state));
    }

    private void onPageDetached(TabPage page) {
        if (!instance.in_dispose) {
            if (getTabView().getNPages() == 0) {
                close();
            }
        }
    }

    private void onSetupMenu() {
        System.out.println("TODO onSetupMenu");
    }

    private TabView onCreateWindow() {
        var window = new AdwTabViewDemoWindow();
        window.present();
        return window.getTabView();
    }

    private void onIndicatorActivated(TabPage page) {
        var muted = page.getData("adw-tab-view-demo-muted");
        if (muted.isNotNull()) {
            page.setData("adw-tab-view-demo-muted", Pointer.NULL);
        } else {
            page.setData("adw-tab-view-demo-muted", asPointer(1L));
        }

        // TODO Why??
        var icon = getIndicatorIcon(page);
        var tooltip = getIndicatorTooltip(page);
        page.setIndicatorIcon(icon);
        page.setIndicatorTooltip(tooltip);

        // TODO g_object_unref (icon);
        tooltip.destroy();
    }

    private boolean onExtraDragDrop() {
        System.out.println("TODO onExtra DragDrop");
        return GdkConstants.EVENT_STOP;
    }

    private void sizeAllocate(int width, int height, int baseline) {
        var narrow = width < 600;

        if (instance.narrow != narrow) {
            instance.narrow = narrow;
            instance.writeField("narrow");
            //  TODO g_object_notify_by_pspec (G_OBJECT (self), props[PROP_NARROW]);
        }

        // GTK_WIDGET_CLASS (adw_tab_view_demo_window_parent_class)->size_allocate (widget, width, height, baseline);
    }


    // TODO move to instance as 'getter' (?)
    private TabView getTabView() {
        return new TabView(cast(instance.view));
    }

    // TODO move to instance as 'getter' (?)
    private TabPage getMenuPage() {
        return new TabPage(cast(instance.menu_page));
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Window.getTypeID(), TYPE_NAME, 3*8, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                objectClass.setFieldDispose((__self1, object) -> new AdwTabViewDemoWindow(object.cast()).dispose());
                objectClass.setFieldGetProperty((__self12, object, property_id, value, pspec) -> new AdwTabViewDemoWindow(object.cast()).getProperty(property_id, value));
                widgetClass.overrideSizeAllocate((self, width, height, baseline) -> new AdwTabViewDemoWindow(self).sizeAllocate(width, height, baseline));


                var propNarrow = Gobject.paramSpecBoolean(PROP_NARROW_NAME, null, null, false, ParamFlags.READABLE | GobjectConstants.PARAM_STATIC_STRINGS);
                objectClass.installProperty(PROP_NARROW, propNarrow);
                widgetClass.setTemplateOrExit("/adw_demo/adw-tab-view-demo-window.ui");

                widgetClass.bindTemplateChildFull("view", true, OFFSET);
                widgetClass.bindTemplateChildFull("tab_bar", true, OFFSET + 8);
                widgetClass.bindTemplateChildFull("tab_overview", true, OFFSET + 16);

                widgetClass.bindTemplateCallback("page_detached_cb", new Callback() {
                    public void invoke(long self, long page) {
                        new AdwTabViewDemoWindow(self).onPageDetached(new TabPage(cast(page)));
                    }
                });
                widgetClass.bindTemplateCallback("setup_menu_cb", new Callback() {
                    public void invoke(long self, long page, long view) {
                        new AdwTabViewDemoWindow(self).onSetupMenu();
                    }
                });
                widgetClass.bindTemplateCallback("create_tab_cb", new Callback() {
                    public long invoke(long self) {
                        return new AdwTabViewDemoWindow(self).onCreateTab().asCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("create_window_cb", new Callback() {
                    public long invoke(long self) {
                        return new AdwTabViewDemoWindow(self).onCreateWindow().asCPointer();
                    }
                });

                widgetClass.bindTemplateCallback("indicator_activated_cb", new Callback() {
                    public void invoke(long self, long page) {
                        new AdwTabViewDemoWindow(self).onIndicatorActivated(new TabPage(cast(page)));
                    }
                });

                widgetClass.bindTemplateCallback("extra_drag_drop_cb", new Callback() {
                    public boolean invoke(long self, long page, long value) {
                        return new AdwTabViewDemoWindow(self).onExtraDragDrop();
                    }
                });

            }, (__self, instance, g_class) -> new AdwTabViewDemoWindow(instance));
        }
        return type;
    }

    private static Str PROP_NARROW_NAME = new Str("narrow");
    private static int PROP_NARROW = 1;
    private void getProperty(int property_id, Value value) {
        if (property_id == PROP_NARROW) {
            value.setBoolean(instance.narrow);
        } else {
            System.err.println("Invalid Property ID: " + property_id);
        }
    }

    private void dispose() {
        instance.in_dispose = true;
        instance.writeField("in_dispose");
        // TODO  g_clear_object (&self->tab_action_group);
        // TODO G_OBJECT_CLASS (adw_tab_view_demo_window_parent_class)->dispose (object);
    }
}
