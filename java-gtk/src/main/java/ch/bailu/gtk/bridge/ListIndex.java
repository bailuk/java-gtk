package ch.bailu.gtk.bridge;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Sizes;
import ch.bailu.jgtk.lib.GIO;
import ch.bailu.jgtk.lib.GObject;

public class ListIndex extends ch.bailu.gtk.gobject.Object {

    private static final int PROP_ITEM_TYPE = 1;
    private static final String   PROP_NAME = "item-type";
    private static final String   TYPE_NAME = "ListIndex";
    private static final long   PARENT_TYPE = ch.bailu.gtk.gobject.Object.getTypeID();
    private static final int     CLASS_SIZE = Sizes.GOBJECT_CLASS;
    private static final int  INSTANCE_SIZE = Sizes.GOBJECT + 8;

    private final Instance instance;

    public ListIndex(ListItem item) {
        super(item.getItem().cast());
        instance = new Instance(getCPointer());
    }
    public ListIndex(CPointer cast) {
        super(cast);
        instance = new Instance(getCPointer());
    }

    public ListIndex(int size) {
        this();
        setSize(size);
    }

    public ListIndex() {
        super(create(getTypeID(), PROP_NAME, getTypeID()));
        instance = new Instance(getCPointer());
    }

    private static CPointer create(long type, String property_name, long property_value) {
        System.out.println("ListIndex::create");
        return new CPointer(GObject.API().g_object_new(type, property_name, property_value, 0));
    }

    private static long type = 0;
    public synchronized static long getTypeID() {
        System.out.println("ListIndex::getTypeID");
        if (type == 0) {
            type = registerClass();
            registerInterface(type);
        }
        return type;
    }

    private static synchronized long registerClass() {
        System.out.println("ListIndex::registerClass");
        return GObject.API().g_type_register_static_simple(
                PARENT_TYPE,
                TYPE_NAME,
                CLASS_SIZE,
                classInit,
                INSTANCE_SIZE,
                instanceInit,
                0);
    }

    private static synchronized void registerInterface(long type) {
        System.out.println("ListIndex::registerInterface");
        GObject.InterfaceInfo info = new GObject.InterfaceInfo();
        info.read();
        info.interface_init = interfaceInit;
        info.interface_data = 0;
        info.interface_finalize = 0;
        info.write();
        GObject.API().g_type_add_interface_static(type, ListModel.getTypeID(), info);
  }

    private static long parentClass = 0;
    private static Callback classInit = new Callback() {
        public void invoke(long klass) {
            System.out.println("ListIndex::classInit");
            parentClass = GObject.API().g_type_class_peek(klass);

            long object_class = GObject.API().g_type_check_class_cast(klass, PARENT_TYPE);

            GObject.ObjectClass objectClass = new GObject.ObjectClass(object_class);

            //objectClass.read();
            objectClass.dispose = instaceDispose;
            objectClass.getProperty = getProperty;
            objectClass.setProperty = setProperty;

            objectClass.writeField("dispose");
            objectClass.writeField("getProperty");
            objectClass.writeField("setProperty");

            long paramType = GObject.API().g_param_spec_gtype(PROP_NAME, "", "", PARENT_TYPE,
                      ParamFlags.CONSTRUCT   |
                            ParamFlags.READWRITE   |
                            ParamFlags.STATIC_NAME |
                            ParamFlags.STATIC_NICK |
                            ParamFlags.STATIC_BLURB);

            GObject.API().g_object_class_install_property(object_class, PROP_ITEM_TYPE, paramType);
        }
    };

    private static Callback interfaceInit = new Callback() {
        public void invoke(long inst) {
            System.out.println("ListIndex::interfaceInit");
            GIO.GListModelInterface iface = new GIO.GListModelInterface(inst);

            iface.read();
            iface.get_item      = getItem;
            iface.get_n_items   = getNItems;
            iface.get_item_type = getItemType;
            iface.write();
        }
    };
    private static Callback getItemType = new Callback() {
        public long invoke(long inst) {
            System.out.println("ListIndex::getItemType");
            return getTypeID();
        }
    };
    private static Callback getNItems = new Callback() {
        public int invoke(long inst) {
            System.out.println("ListIndex::getNItems");
            return new ListIndex(toCPointer(inst)).getSize();
        }
    };
    private static Callback getItem = new Callback() {
        public long invoke(long inst, int position) {
            System.out.println("ListIndex::cb::getItem");
            return new ListIndex(toCPointer(inst)).getItem(position).cast().getCPointer();
        }
    };

    public ListIndex getItem(int position) {
        System.out.println("ListIndex::getItem");
        ListIndex result = new ListIndex(getSize());
        result.setIndex(position);
        return result;
    }

    public int getIndex() {
        System.out.println("ListIndex::getIndex");
        instance.readField("index");
        return instance.index;
    }

    public int getSize() {
        System.out.println("ListIndex::getSize");
        instance.readField("size");
        return instance.size;
    }

    public void setSize(int size) {
        System.out.println("ListIndex::setSize");

        instance.readField("size");
        int oldSize = instance.size;
        instance.size = size;
        instance.writeField("size");

        ListModel listModel = new ListModel(cast());
        listModel.itemsChanged(0, oldSize, size);
    }

    public void setIndex(int index) {
        System.out.println("ListIndex::setIndex");
        instance.index = index;
        instance.writeField("index");
    }


    private static Callback instanceInit = new Callback() {
        public void invoke(long inst) {
            System.out.println("ListIndex::instanceInit");
            new ListIndex(toCPointer(inst)).initInstance();
        }
    };

    private void initInstance() {
        System.out.println("ListIndex::initInstance");
        instance.index = 0;
        instance.size = 0;
        instance.writeField("index");
        instance.writeField("size");
    }

    private static Callback instaceDispose = new Callback() {
        public void invoke(long instance) {
            System.out.println("ListIndex::instanceDispose (TODO)");
        }
    };

    private static Callback setProperty = new Callback() {
        public void invoke(long object, int property_id, long value, long pspec) {
            System.out.println("ListIndex::setProperty");
        }
    };

    private static Callback getProperty = new Callback() {
        public void invoke(long object, int property_id, long value, long pspec) {
            System.out.println("ListIndex::getProperty");
            if (property_id == PROP_ITEM_TYPE) {
                GObject.API().g_value_set_gtype(value, getTypeID());
            }
        }
    };



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


    @Structure.FieldOrder({"parent", "index", "size"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
        }

        public byte[] parent = new byte[Sizes.GOBJECT];
        public int index;
        public int size;
    }
}
