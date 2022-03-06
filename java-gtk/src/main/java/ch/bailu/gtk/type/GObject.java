package ch.bailu.gtk.type;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

public class GObject {
    private static GObject.Api _API = null;

    public static GObject.Api API() {
        if (_API == null) {
            _API = Native.load("gobject-2.0", GObject.Api.class);
        }
        return _API;
    }

    public interface Api extends Library {
        long g_object_new(long type, String property_name, long property, long terminate);
        void g_object_class_install_property(long oclass, int property_id, long pspec);
        long g_type_register_static_simple(long parent_type,
                                         String type_name,
                                         int class_size,
                                         Callback class_init,
                                         int instance_size,
                                         Callback instance_init,
                                         int flags);
        long g_type_class_peek(long type);
        long g_type_check_class_cast(long g_class, long is_a_type);
        void g_type_add_interface_static(long instance_type, long interface_type, InterfaceInfo info);
        long g_param_spec_gtype(String name, String nick, String blurb, long is_a_type, long flags);
        void g_value_set_gtype(long value, long v_gtype);
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

        public long gtype; // TODO size of gtype?
        public long construct_properties;
        public Callback constructor;
        public Callback setProperty;
        public Callback getProperty;
        public Callback dispose;
        public Callback finalize;
        public Callback dispatchPropertiesChanged;
        public Callback notify;
        public Callback constructed;
        long flags;
        //long[6] dummy;
    }

    public static class InterfaceInfo extends Structure {
        public Callback interface_init;
        public long interface_finalize;
        public long interface_data;
    }
}
