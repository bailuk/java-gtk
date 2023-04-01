package examples.libadwaita.demo.tab_view;

import com.sun.jna.Structure;

import java.util.Random;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gdk.Display;
import ch.bailu.gtk.gio.Icon;
import ch.bailu.gtk.gio.ThemedIcon;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.IconTheme;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwTabViewDemoPage extends Bin {

    private final static Str TYPE_NAME = new Str(AdwTabViewDemoPage.class.getSimpleName());
    private final static int OFFSET = TypeSystem.getTypeSize(Bin.getTypeID()).instanceSize;

    private final static int PROP_TITLE = 1;
    private final static int PROP_ICON  = 2;
    public final static Str PROP_TITLE_NAME = new Str("title");
    public final static Str PROP_ICON_NAME = new Str("icon");

    private static long type = 0;


    @Structure.FieldOrder({"parent", "title_entry", "title", "icon", "last_icon",  "color"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[OFFSET];

        public long title_entry;
        public long title;

        public long icon;
        public long last_icon;

        public int color;
    }

    private final Instance instance;


    public AdwTabViewDemoPage(PointerContainer cast) {
        super(cast);
        instance = new Instance(asCPointer());
    }

    public AdwTabViewDemoPage(TypeInstance self) {
        super(self.cast());
        initTemplate();
        instance = new Instance(asCPointer());
        instance.icon = getRandomIcon().asCPointer();
        instance.writeField("icon");
        setColor(getRandomColor());
    }

    public AdwTabViewDemoPage(long self) {
        super(cast(self));
        instance = new Instance(asCPointer());
    }

    public AdwTabViewDemoPage(Str title) {
        super(TypeSystem.newInstance(getTypeID(), new TypeSystem.Property(PROP_TITLE_NAME, title)));
        instance = new Instance(asCPointer());
    }

    public AdwTabViewDemoPage(AdwTabViewDemoPage other) {
        super(TypeSystem.newInstance(
                getTypeID(),
                new TypeSystem.Property(PROP_TITLE_NAME, other.instance.title),
                new TypeSystem.Property(PROP_ICON_NAME, other.instance.icon)));
        instance = new Instance(asCPointer());
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 4 * 8 + 4 , (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                objectClass.overrideFinalize((__self1, object) -> new AdwTabViewDemoPage(object.cast()).finalizeInstance());
                objectClass.overrideGetProperty((__self12, object, property_id, value, pspec) -> new AdwTabViewDemoPage(object.cast()).getProperty(property_id, value));
                objectClass.overrideSetProperty((__self13, object, property_id, value, pspec) -> new AdwTabViewDemoPage(object.cast()).setProperty(property_id, value));


                var propTitle = Gobject.paramSpecString(PROP_TITLE_NAME, null, null, null, ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                var propIcon  = Gobject.paramSpecObject(PROP_ICON_NAME,  null, null, Icon.getTypeID(), ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);

                objectClass.installProperty(PROP_ICON,  propIcon);
                objectClass.installProperty(PROP_TITLE, propTitle);

                widgetClass.setTemplateOrExit("/adw_demo/adw-tab-view-demo-page.ui");
                widgetClass.bindTemplateChildFull("title_entry", true, OFFSET);

            }, (__self, instance, g_class) -> new AdwTabViewDemoPage(instance));
        }
        return type;
    }

    private static Strs iconNames = null;

    private static void initIconNames() {
        if (iconNames == null){
            var display = Display.getDefault();
            var theme = IconTheme.getForDisplay(display);
            iconNames = theme.getIconNames();
        }
    }

    private static Icon getRandomIcon() {
        initIconNames();
        final var index = new Random().nextInt(iconNames.getLength());
        return new Icon(new ThemedIcon(iconNames.get(index)).cast());
    }

    private static final int N_COLORS = 8;
    private static int getRandomColor() {
        return new Random().nextInt(N_COLORS) + 1;
    }

    private void setColor(int color) {
        if (instance.color != color) {
            if (instance.color > 0) {
                removeCssClass("tab-page-color" + instance.color);
            }

            if (color > 0) {
                removeCssClass("tab-page-color" + color);
            }
            instance.color = color;
            instance.writeField("color");
        }
    }


    private void finalizeInstance() {
        System.out.println("TODO finalize");
    }

    private void getProperty(int propertyId, Value value) {
        if (propertyId == PROP_TITLE) {
            value.setString(new Str(cast(instance.title)));
        } else if (propertyId == PROP_ICON) {
            value.setObject(new Pointer(cast(instance.icon)));
        } else {
            System.err.println("Invalid property");
        }
    }

    private void setProperty(int propertyId, Value value) {
        if (propertyId == PROP_TITLE) {
            new Str(cast(instance.title)).destroy();
            instance.title = value.getString().asCPointer();
            instance.writeField("title");
        } else if (propertyId == PROP_ICON) {
            instance.icon = value.getObject().asCPointer();
            instance.writeField("icon");
        } else {
            System.err.println("Invalid property");
        }
    }


    public void refreshIcon() {
        var icon = getRandomIcon();
        set("icon", icon.asCPointer(), null); // indirectly calls setProperty()
        new ch.bailu.gtk.gobject.Object(icon.cast()).unref(); // Icon is an interface, so no unref
    }

    public void setEnableIcon(boolean enableIcon) {
        if (enableIcon) {
            set("icon", instance.last_icon);
            new ch.bailu.gtk.gobject.Object(cast(instance.last_icon)).unref();
            instance.last_icon = 0L;
            instance.writeField("last_icon");
        } else {
            instance.last_icon = instance.icon;
            new ch.bailu.gtk.gobject.Object(cast(instance.icon)).unref();
            set("icon", 0, 0);
        }
    }
}
