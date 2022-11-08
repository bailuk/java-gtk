package ch.bailu.gtk.lib;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import ch.bailu.gtk.type.Sizes;

public class GIO {
    private static GIO.Api _API = null;

    public static GIO.Api API() {
        if (_API == null) {
            _API = Native.load("gobject-2.0", GIO.Api.class);
        }
        return _API;
    }

    public interface Api extends Library {

    }

    @Structure.FieldOrder({"g_iface", "get_item_type", "get_n_items", "get_item"})
    public static class GListModelInterface extends Structure {

        public GListModelInterface(long _self) {
            super(ch.bailu.gtk.type.Pointer.toJnaPointer(_self));
        }

        public byte[] g_iface = new byte[Sizes.GTYPE_INTERFACE];
        public Callback get_item_type;
        public Callback get_n_items;
        public Callback get_item;
    }
}
