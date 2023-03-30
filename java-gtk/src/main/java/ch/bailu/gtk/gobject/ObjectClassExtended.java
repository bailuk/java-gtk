package ch.bailu.gtk.gobject;

import ch.bailu.gtk.lib.jna.GObjectLib;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class ObjectClassExtended extends ObjectClass {

    public ObjectClassExtended(CPointer g_class) {
        super(g_class);
    }

    public void onDispose(Object instance) {
        getFieldDispose().invoke(instance.getCPointer());
    }

    public void overrideDispose(OnDispose dispose) {
        setFieldDispose(dispose);
    }

    public void overrideFinalize(OnFinalize finalize) {
        setFieldFinalize(finalize);
    }

    public void overrideSetProperty(OnSetProperty onSetProperty) {
        setFieldSetProperty(onSetProperty);
    }

    public void overrideGetProperty(OnGetProperty onGetProperty) {
        setFieldGetProperty(onGetProperty);
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
