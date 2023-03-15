package ch.bailu.gtk.lib.bridge;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gobject.GObject;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.InterfaceInfo;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.TypeClass;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

/**
 * A ListModel that provides indices for n items.
 * Use this model to back a gtk.ListView with any
 * Java data structure.
 * @see ch.bailu.gtk.gtk.ListView (view for this model)
 * see examples.HugeList for example usage
 *
 * ListIndex subclasses GObject and implements ListModel.
 * This class provides a proof of concept for implementing and
 * subclassing GObject structures with java-gtk.
 * @see ch.bailu.gtk.gobject.GObject (parent class)
 * @see ch.bailu.gtk.gio.ListModel (interface)
 */
public class ListIndex extends ch.bailu.gtk.gobject.GObject {

    private static final int PROP_ITEM_TYPE = 1;
    private static final Str      PROP_NAME = new Str("item-type");
    private static final Str      TYPE_NAME = new Str(ListIndex.class.getSimpleName());
    private static final long   PARENT_TYPE = GObject.getTypeID();
    private static final Str          EMPTY = new Str("");

    @Structure.FieldOrder({"parent", "index", "size"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
        }

        public byte[] parent = new byte[TypeSystem.getTypeSize(PARENT_TYPE).instanceSize];
        public int index;
        public int size;
    }

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
        instance.size = size;
        instance.writeField("size");
    }

    public ListIndex() {
        super(TypeSystem.newInstance(getTypeID(), new TypeSystem.Property(PROP_NAME, getTypeID())));
        instance = new Instance(getCPointer());
    }

    private static long type = 0;
    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, 8, classInit, instanceInit);
            registerInterface(type);
        }
        return type;
    }


    private static synchronized void registerInterface(long type) {
        System.out.println("ListIndex::registerInterface");

        InterfaceInfo info = new InterfaceInfo();
        info.setFieldInterfaceInit(interfaceInit);
        Gobject.typeAddInterfaceStatic(type, ListModel.getTypeID(), info);
  }

    private static ObjectClass parentClass = null;
    private static Gobject.OnClassInitFunc classInit = new Gobject.OnClassInitFunc() {
        @Override
        public void onClassInitFunc(CallbackHandler __self, @Nonnull Pointer g_class, @Nullable Pointer class_data) {

            System.out.println("ListIndex::classInit");
            var typeClass = new TypeClass(g_class.cast());
            parentClass = new ObjectClass(typeClass.peekParent().cast());

            var objectClass = new ObjectClassExtended(Gobject.typeCheckClassCast(typeClass, PARENT_TYPE).cast());
            objectClass.overrideDispose(instanceDispose);
            objectClass.overridePropertyAccess(getProperty, setProperty);

            var flags =
                    ParamFlags.CONSTRUCT |
                    ParamFlags.READWRITE   |
                    ParamFlags.STATIC_NAME |
                    ParamFlags.STATIC_NICK |
                    ParamFlags.STATIC_BLURB;

            objectClass.installProperty(PROP_ITEM_TYPE, Gobject.paramSpecGtype(PROP_NAME, EMPTY, EMPTY, PARENT_TYPE, flags));
        }
    };

    private static InterfaceInfo.OnInterfaceInitFunc interfaceInit = new InterfaceInfo.OnInterfaceInitFunc() {
        @Override
        public void onInterfaceInitFunc(CallbackHandler __self, @Nonnull Pointer g_iface, @Nullable Pointer iface_data) {

            System.out.println("ListIndex::interfaceInit");
            GObjectLib.GListModelInterface iface = new GObjectLib.GListModelInterface(g_iface.getCPointer());

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
            return new ListIndex(toCPointer(inst)).getSize();
        }
    };
    private static Callback getItem = new Callback() {
        public long invoke(long inst, int position) {
            long result = 0;
            ListIndex item = new ListIndex(toCPointer(inst)).getItem(position);
            if (item != null) {
                result = item.cast().getCPointer();
            }
            return result;
        }
    };

    public ListIndex getItem(int position) {
        ListIndex result = null;
        if (position < getSize() && position > -1) {
            result = new ListIndex(getSize());
            result.setIndex(position);
        }
        return result;
    }

    public int getIndex() {
        instance.readField("index");
        return instance.index;
    }

    public int getSize() {
        instance.readField("size");
        return instance.size;
    }

    public void setSize(int size) {
        instance.readField("size");
        int oldSize = instance.size;
        instance.size = size;
        instance.writeField("size");

        ListModel listModel = new ListModel(cast());
        listModel.itemsChanged(0, oldSize, size);
    }

    public void setIndex(int index) {
        instance.index = index;
        instance.writeField("index");
    }


    private static Gobject.OnInstanceInitFunc instanceInit = (__self, instance, g_class) -> new ListIndex(instance.cast()).initInstance();

    private void initInstance() {
        instance.index = 0;
        instance.size = 0;
        instance.writeField("index");
        instance.writeField("size");
    }

    private static ObjectClassExtended.DisposeCallback instanceDispose = instance -> {
        if (parentClass.isNull()) {
            System.out.println("ListIndex::instanceDispose (no parent)");
        } else {
            GObjectLib.ObjectClass pClass = new GObjectLib.ObjectClass(parentClass.getCPointer());
            pClass.readField("dispose");
            pClass.dispose.invoke(instance);
        }
    };

    private static ObjectClassExtended.PropertyCallback setProperty = (object, property_id, value, pspec) -> {
        if (property_id != PROP_ITEM_TYPE) {
            System.out.println("ListIndex::setProperty (unknown property");
        }
    };

    private static ObjectClassExtended.PropertyCallback getProperty = (object, property_id, value, pspec) -> {
        if (property_id == PROP_ITEM_TYPE) {
            new Value(new CPointer(value)).setGtype(getTypeID());
        } else {
            System.out.println("ListIndex::getProperty (unknown property");
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
}
