package ch.bailu.gtk.lib.bridge;

import com.sun.jna.Structure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gio.ListModelInterface;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.InterfaceInfo;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gtk.ListItem;
import ch.bailu.gtk.gtk.SelectionModel;
import ch.bailu.gtk.gtk.SingleSelection;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.PropertyHolder;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

/**
 * A ListModel that provides indices for n items.
 * Use this model to back a gtk.ListView with any
 * Java data structure.
 * @see ch.bailu.gtk.gtk.ListView (view for this model)
 * see examples.HugeList for example usage
 * ListIndex subclasses GObject and implements ListModel.
 * This class provides a proof of concept for implementing and
 * subclassing GObject structures with java-gtk.
 * @see ch.bailu.gtk.gobject.Object (parent class)
 * @see ch.bailu.gtk.gio.ListModel (interface)
 */
public class ListIndex extends PropertyHolder {

    private static final int PROP_ITEM_TYPE = 1;
    private static final Str      PROP_NAME = new Str("item-type");
    private static final Str      TYPE_NAME = new Str(ListIndex.class.getSimpleName());
    private static final long   PARENT_TYPE = Object.getTypeID();
    private static final Str          EMPTY = new Str("");

    @Structure.FieldOrder({"parent", "index", "size"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(asJnaPointer(_self));
        }

        public byte[] parent = new byte[Object.getTypeSize().instanceSize];
        public int index;
        public int size;
    }

    private final Instance instance;

    public ListIndex(ListItem item) {
        super(item.getItem().cast());
        instance = new Instance(asCPointer());
    }

    public ListIndex(PointerContainer cast) {
        super(cast);
        instance = new Instance(asCPointer());
    }

    public ListIndex(int size) {
        this();
        instance.size = size;
        instance.writeField("size");
    }

    public ListIndex() {
        super(TypeSystem.newInstance(getTypeID(), new TypeSystem.Property(PROP_NAME, getTypeID())));
        instance = new Instance(asCPointer());
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

    private static ObjectClassExtended parentClass = null;
    private static final Gobject.OnClassInitFunc classInit = new Gobject.OnClassInitFunc() {
        @Override
        public void onClassInitFunc(CallbackHandler __self, @Nonnull Pointer g_class, @Nullable Pointer class_data) {

            System.out.println("ListIndex::classInit");

            var objectClass = new ObjectClassExtended(g_class.cast());
            parentClass = objectClass.getParentClass();
            objectClass.setFieldDispose(instanceDispose);
            objectClass.setFieldGetProperty(getProperty);
            objectClass.setFieldSetProperty(setProperty);

            var flags =
                    ParamFlags.CONSTRUCT |
                    ParamFlags.READWRITE   |
                    ParamFlags.STATIC_NAME |
                    ParamFlags.STATIC_NICK |
                    ParamFlags.STATIC_BLURB;

            objectClass.installProperty(PROP_ITEM_TYPE, Gobject.paramSpecGtype(PROP_NAME, EMPTY, EMPTY, PARENT_TYPE, flags));
        }
    };

    private static final InterfaceInfo.OnInterfaceInitFunc interfaceInit = new InterfaceInfo.OnInterfaceInitFunc() {
        @Override
        public void onInterfaceInitFunc(CallbackHandler __self, @Nonnull Pointer g_iface, @Nullable Pointer iface_data) {
            System.out.println("ListIndex::interfaceInit");

            var listModelInterface = new ListModelInterface(cast(g_iface.asCPointer()));
            listModelInterface.setFieldGetItem(getItem);
            listModelInterface.setFieldGetNItems(getNItems);
            listModelInterface.setFieldGetItemType(getItemType);
        }
    };

    private static final ListModelInterface.OnGetItemType getItemType = (__self, list) -> {
        System.out.println("ListIndex::getItemType");
        return getTypeID();
    };

    private static final ListModelInterface.OnGetNItems getNItems = (__self, list) -> new ListIndex(list.cast()).getSize();

    private static final ListModelInterface.OnGetItem getItem = (__self, list, position) -> {
        Pointer result = Pointer.NULL;
        ListIndex item = new ListIndex(list.cast()).getItem(position);
        if (item != null) {
            result = item;
        }
        return result;
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


    private static final Gobject.OnInstanceInitFunc instanceInit = (__self, instance, g_class) -> new ListIndex(instance.cast()).initInstance();

    private void initInstance() {
        instance.index = 0;
        instance.size = 0;
        instance.writeField("index");
        instance.writeField("size");
    }

    private static final ObjectClass.OnDispose instanceDispose = (__self, object) -> {
        if (parentClass.isNull()) {
            System.out.println("ListIndex::instanceDispose (no parent)");
        } else {
            parentClass.onDispose(new ListIndex(object.cast()));
        }
    };

    private static final ObjectClass.OnSetProperty setProperty = (__self, object, property_id, value, pspec) -> {
        if (property_id != PROP_ITEM_TYPE) {
            System.out.println("ListIndex::setProperty (unknown property");
        }
    };

    private static final ObjectClass.OnGetProperty getProperty = (__self, object, property_id, value, pspec) -> {
        if (property_id == PROP_ITEM_TYPE) {
            value.setGtype(getTypeID());
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
