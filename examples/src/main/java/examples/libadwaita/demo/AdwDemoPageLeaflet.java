package examples.libadwaita.demo;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.EnumListItem;
import ch.bailu.gtk.adw.LeafletTransitionType;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.lib.jna.AdwLib;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageLeaflet extends Bin {
    private final static int PROP_TRANSITION_TYPE = 1;

    private final static Str  TYPE_NAME = new Str(AdwDemoPageLeaflet.class.getSimpleName());
    private static final Str  PROP_NAME_TRANSITION_TYPE = new Str("transition-type");
    private static final long PARENT_TYPE = Bin.getTypeID();
    private static final int  PARENT_INSTANCE_SIZE = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private static final Str  SIGNAL_NEXT_PAGE = new Str("next-page");

    @Structure.FieldOrder({"parent", "transition_type"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_INSTANCE_SIZE];
        public int transition_type;
    }

    private final Instance instance;

    public AdwDemoPageLeaflet(PointerContainer self) {
        super(self);
        instance = new Instance(asCPointer());
    }

    private static long type = 0;
    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, 4, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                var prop = Gobject.paramSpecEnum(
                        PROP_NAME_TRANSITION_TYPE,
                        Str.NULL, Str.NULL,
                        AdwLib.INST().adw_leaflet_transition_type_get_type(),
                        LeafletTransitionType.OVER,
                        ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);

                objectClass.overrideSetProperty((__self1, object, property_id, value, pspec) -> new AdwDemoPageLeaflet(object.cast()).setProperty(property_id, value));
                objectClass.overrideGetProperty((__self12, object, property_id, value, pspec) -> new AdwDemoPageLeaflet(object.cast()).getProperty(property_id, value));
                objectClass.installProperty(PROP_TRANSITION_TYPE, prop);

                final var signal = objectClass.signalNew(SIGNAL_NEXT_PAGE, TypeSystem.GTYPE_NONE);

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-leaflet.ui");
                widgetClass.bindTemplateCallback("get_transition_name", new Callback() {
                        public long invoke(long self, long user_data) {
                        return getTransitionName(new EnumListItem(cast(self))).asCPointer();
                    }
                });

                widgetClass.bindTemplateCallback("next_row_activated_cb", new Callback() {
                    public void invoke(long self) {
                        Gobject.signalEmit(asPointer(self), signal, 0);
                    }
                });

            }, (__self, self, g_class) -> new Bin(self.cast()).initTemplate());

        }
        return type;
    }

    private static Str getTransitionName(EnumListItem enumList) {
        if (enumList.getValue() == LeafletTransitionType.OVER) {
            return new Str("Over");
        }
        if (enumList.getValue() == LeafletTransitionType.UNDER) {
            return new Str("Under");
        }
        if (enumList.getValue() == LeafletTransitionType.SLIDE) {
            return new Str("Slide");
        }
        return Str.NULL;
    }

    private void setProperty(int property_id, Value value) {
        if (property_id == PROP_TRANSITION_TYPE) {
            System.out.println("set transition type");
            instance.transition_type = value.getEnum();
            instance.writeField("transition_type");
        }
    }

    private void getProperty(int property_id, Value value) {
        if (property_id == PROP_TRANSITION_TYPE) {
            System.out.println("get transition type");
            value.setEnum(instance.transition_type);
        }
    }
}
