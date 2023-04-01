package ch.bailu.gtk.lib.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Structure;

public class GObjectLib {
    private static Instance _INST = null;

    public static final int GTYPE_INTERFACE_SIZE = 16;

    @Structure.FieldOrder({"g_iface", "get_item_type", "get_n_items", "get_item"})
    private static class GListModelInterface extends Structure {

        public GListModelInterface(long _self) {
            super(ch.bailu.gtk.type.Pointer.asJnaPointer(_self));
        }

        public byte[] g_iface = new byte[GTYPE_INTERFACE_SIZE];
        public Callback get_item_type;
        public Callback get_n_items;
        public Callback get_item;
    }

    public static Instance INST() {
        if (_INST == null) {
            _INST = Loader.load("gobject-2.0", Instance.class);
        }
        return _INST;
    }

    public interface Instance extends Library {
        long g_signal_connect_data(long instance, String detailed_signal, Callback cb, long data, long destroy_data, int flag);
        void g_signal_handler_disconnect (long instance, long handler_id);
        int g_signal_new(long signal_name, long itype, int singal_flags, int class_offset, long accumulator, long accu_data, long c_marshaller, long return_type, int n_params, Object ... objects);
    }
}
