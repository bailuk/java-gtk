package ch.bailu.gtk.lib.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Structure;

public class GObject {
    private static Instance _INST = null;

    public static Instance INST() {
        if (_INST == null) {
            _INST = Loader.load("gobject-2.0", Instance.class);
        }
        return _INST;
    }

    public interface Instance extends Library {
        long g_signal_connect_data(long instance, String detailed_signal, Callback cb, long data, long destroy_data, int flag);
        void g_signal_handler_disconnect (long instance, long handler_id);
    }

    @Structure.FieldOrder({
            "gtype",
            "construct_properties",
            "constructor",
            "setProperty",
            "getProperty",
            "dispose",
            "finalize",
            "dispatchPropertiesChanged",
            "notify",
            "constructed",
            "flags"})
    public static class ObjectClass extends Structure {

        public ObjectClass(long _self) {
            super(ch.bailu.gtk.type.Pointer.toJnaPointer(_self));
        }

        public long gtype;
        public long construct_properties;
        public Callback constructor;
        public Callback setProperty;
        public Callback getProperty;
        public DisposeCallback dispose;
        public Callback finalize;
        public Callback dispatchPropertiesChanged;
        public Callback notify;
        public Callback constructed;
        public long flags;
    }


    public interface DisposeCallback extends Callback {
        void invoke(long pointer);
    }
    @Structure.FieldOrder({"interface_init", "interface_finalize", "interface_data"})
    public static class InterfaceInfo extends Structure {
        public Callback interface_init;
        public long interface_finalize;
        public long interface_data;
    }
}
