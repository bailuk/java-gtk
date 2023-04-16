package ch.bailu.gtk.gobject;

import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;

public class ObjectClassExtended extends ObjectClass {

    public ObjectClassExtended(PointerContainer g_class) {
        super(g_class);
    }

    public void onDispose(Object instance) {
        getFieldDispose().invoke(instance.asCPointer());
    }

    public int signalNew(Str name, long returnType, Long... types) {
        return GObjectLib.INST().g_signal_new(
                name.asCPointer(),
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
