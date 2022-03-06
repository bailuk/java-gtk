package ch.bailu.gtk.bridge;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gio.ListModelInterface;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.GObject;
import ch.bailu.gtk.type.Pointer;

public class ListIndex extends Pointer {

    private static int PROP_ITEM_TYPE = 1;
    private static final String PROP_NAME = "item-type";
    private static final String TYPE_NAME = "ListIndex";
    
    private static final long PARENT_TYPE = (20) << (2); // Todo GObject.getType()

    public ListIndex(ListItem item) {
        super(item.getItem().cast());
    }
    public ListIndex(CPointer cast) {
        super(cast);
    }

    public ListIndex(int size) {
        this();
        // TODO ImpListIndex.setSize(getCPointer(), size);
    }

    public ListIndex() {
        super(create(getType(), PROP_NAME, getType()));
    }

    private static CPointer create(long type, String property_name, long property_value) {
        return new CPointer(GObject.API().g_object_new(type, property_name, property_value, 0));
    }

    private static long type = 0;
    private synchronized static long getType() {
        if (type == 0) {
            type = getTypeOnce();
        }
        return type;
    }

    private static long getTypeOnce() {
        int sizeOfClassStruct=0; // TODO size
        int sizeOfInstanceStruct=0; // TODO size
        long typeId = GObject.API().g_type_register_static_simple(
                PARENT_TYPE,
                TYPE_NAME,
                sizeOfClassStruct,
                classInit,
                sizeOfInstanceStruct,
                instanceInit,
                0);

        GObject.InterfaceInfo info = new GObject.InterfaceInfo();
        info.interface_init = interfaceInit;
        info.interface_data = 0;
        info.interface_finalize = 0;
        info.write();
        GObject.API().g_type_add_interface_static(typeId, ListModel.getTypeID(), info);

        return typeId;
    }

    private static long parentClass = 0;
    private static Callback classInit = new Callback() {
        public void invoke(long klass) {
            parentClass = GObject.API().g_type_class_peek(klass);

            long object_class = GObject.API().g_type_check_class_cast(klass, PARENT_TYPE);

            GObject.ObjectClass objectClass = new GObject.ObjectClass(object_class);

            objectClass.dispose = instaceDispose;
            objectClass.getProperty = getProperty;
            objectClass.setProperty = setProperty;

            objectClass.writeField("dispose");
            objectClass.writeField("getProperty");
            objectClass.writeField("setProperty");

            long paramType = GObject.API().g_param_spec_gtype(PROP_NAME, "", "", PARENT_TYPE,
                    ParamFlags.CONSTRUCT | ParamFlags.READWRITE | ParamFlags.STATIC_NAME | ParamFlags.STATIC_NICK | ParamFlags.STATIC_BLURB);

            GObject.API().g_object_class_install_property(object_class, PROP_ITEM_TYPE, paramType);
        }
    };

    private static Callback interfaceInit = new Callback() {
        public void invoke(long inst) {
            ListModelInterface instance = new ListModelInterface(new CPointer(inst));
            // TODO init interface
        }
    };

    private static Callback instanceInit = new Callback() {
        public void invoke(long inst) {
            Instance instance = new Instance(inst);
            instance.index = 0;
            instance.size = 0;
            instance.writeField("index");
            instance.writeField("size");
        }
    };

    private static Callback instaceDispose = new Callback() {
        public void invoke(long instance) {
            System.out.println("dispose called");
        }
    };

    private static Callback setProperty = new Callback() {
        public void invoke(long object, int property_id, long value, long pspec) {
            System.out.println("set property");
        }
    };

    private static Callback getProperty = new Callback() {
        public void invoke(long object, int property_id, long value, long pspec) {
            if (property_id == PROP_ITEM_TYPE) {
                GObject.API().g_value_set_gtype(value, getType());
            }
        }
    };

    public int getIndex() {
        Instance instance = new Instance(getCPointer());
        instance.readField("index");
        return instance.index;
    }

    public int getSize() {
        Instance instance = new Instance(getCPointer());
        instance.readField("size");
        return instance.size;
    }

    public void setSize(int size) {
        Instance instance = new Instance(getCPointer());
        instance.readField("size");

        int oldSize = instance.size;

        instance.size = size;
        instance.writeField("size");

        ListModel listModel = new ListModel(cast());
        listModel.itemsChanged(0, oldSize, size);
    }

    public void setIndex(int index) {
        Instance instance = new Instance(getCPointer());
        instance.index = index;
        instance.writeField("index");
    }

    public ListModel asListModel() {
        return new ListModel(cast());
    }

    public SingleSelection inSingleSelection() {
        return new SingleSelection(asListModel());
    }

    public SelectionModel inSelectionModel() {
        return new SelectionModel(inSingleSelection().cast());
    }

    public static int toIndex(ListItem item) {
        return new ListIndex(item).getIndex();
    }


    @Structure.FieldOrder({"parent_instance", "index", "size"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(ch.bailu.gtk.type.Pointer.toJnaPointer(_self));
        }


        GObject parent_instance; // TODO size??
        public int index;
        public int size;
    }
}
