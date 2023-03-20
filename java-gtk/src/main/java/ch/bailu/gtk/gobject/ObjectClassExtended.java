package ch.bailu.gtk.gobject;

import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class ObjectClassExtended extends ObjectClass {

    public ObjectClassExtended(CPointer g_class) {
        super(g_class);
    }

    public void onDispose(Object instance) {
        GObjectLib.ObjectClass pClass = new GObjectLib.ObjectClass(getCPointer());
        pClass.readField("dispose");
        pClass.dispose.invoke(instance.getCPointer());
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

    public void overrideFinalize(DisposeCallback callback) {
        var objectClassInstance = new GObjectLib.ObjectClass(getCPointer());
        objectClassInstance.finalize = callback;
        objectClassInstance.writeField("finalize");
    }

    public void overrideSetProperty(PropertyCallback cb) {
        var objectClassInstance = new GObjectLib.ObjectClass(getCPointer());
        objectClassInstance.setProperty = cb;
        objectClassInstance.writeField("setProperty");
    }

    public void overrideGetProperty(PropertyCallback cb) {
        var objectClassInstance = new GObjectLib.ObjectClass(getCPointer());
        objectClassInstance.getProperty = cb;
        objectClassInstance.writeField("getProperty");
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
                (java.lang.Object[]) types);
    }


    public ObjectClassExtended getParentClass() {
        final var typeClass = new TypeClass(cast());
        return new ObjectClassExtended(typeClass.peekParent().cast());
    }
}
