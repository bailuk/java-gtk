package ch.bailu.gtk.gobject;

import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class ObjectClassExtended extends ObjectClass {

    public ObjectClassExtended(CPointer g_class) {
        super(g_class);
    }

    @FunctionalInterface
    public interface PropertyCallback extends com.sun.jna.Callback {
        void invoke(long object, int property_id, long value, long pspec);
    }
    @FunctionalInterface
    public interface DisposeCallback extends com.sun.jna.Callback {
        void invoke(long pointer);
    }

    public void overrideDispose(DisposeCallback callback) {
        var objectClassInstance = new GObjectLib.ObjectClass(getCPointer());
        objectClassInstance.dispose = callback;
        objectClassInstance.writeField("dispose");
    }

    public void overridePropertyAccess(PropertyCallback get, PropertyCallback set) {
        var objectClassInstance = new GObjectLib.ObjectClass(getCPointer());
        objectClassInstance.getProperty = get;
        objectClassInstance.setProperty = set;
        objectClassInstance.writeField("getProperty");
        objectClassInstance.writeField("setProperty");
    }

    public int signalNew(Str name, long returnType, Long... types) {
        return GObjectLib.INST().g_signal_new(
                name.getCPointer(),
                new TypeClass(cast()).getFieldGType(),
                SignalFlags.RUN_FIRST,
                0,
                0,
                0,
                0,
                returnType,
                types.length,
                (Object[]) types);
    }
}
