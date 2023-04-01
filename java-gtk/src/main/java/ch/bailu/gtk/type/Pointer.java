package ch.bailu.gtk.type;


import com.sun.jna.Callback;

import java.util.Objects;

import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;

public class Pointer extends Type implements PointerInterface {

    public final static Pointer NULL = new Pointer(PointerContainer.NULL);

    private final PointerContainer pointerContainer;

    /**
     * Casting constructor to access another interface.
     *
     * @see ch.bailu.gtk.type.Pointer#cast()
     *
     * @param pointerContainer Wraps a C pointerContainer of a GTK class or record
     */
    public Pointer(PointerContainer pointerContainer) {
        this.pointerContainer = pointerContainer;
    }

    @Override
    public final long asCPointer() {
        return pointerContainer.asCPointer();
    }


    /**
     * Pass the return value of this function to the casting
     * constructor of any class derived from Pointer
     *
     * @see ch.bailu.gtk.type.Pointer#Pointer(PointerContainer)
     * @see ch.bailu.gtk.type.Pointer
     *
     * @return CPointer (wraps a C pointer of a GTK class or record)
     */
    public final PointerContainer cast() {
        return pointerContainer;
    }

    @Override
    public int hashCode() {
        return pointerContainer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return pointerContainer.equals(obj);
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }

    public final void throwNullPointerException(String msg) {
        final String name = this.getClass().getCanonicalName();
        msg = name + ": " + msg;
        System.out.println(msg);
        throw new NullPointerException(msg);
    }

    public final void throwIfNull() {
        if (pointerContainer.isNull()) {
            throwNullPointerException("pointer == 0");
        }
    }

    /**
     * Connect GTK signal to JNA callback.
     *
     * @param detailedSignal GTK signal name
     * @param callback JNA callback function (lambda).
     * @return {@link ch.bailu.gtk.lib.handler.SignalHandler}. Can be used to disconnect signal and to release callback function.
     */
    public SignalHandler connectSignal(String detailedSignal, Callback callback) {
        return new ch.bailu.gtk.lib.handler.SignalHandler(this, detailedSignal, callback);
    }

    public final void disconnectSignals() {
        SignalHandler.disconnect(this);
    }

    public final void disconnectSignals(String detailedSignal) {
        SignalHandler.disconnect(this, detailedSignal);
    }

    public final void unregisterCallbacks() {
        CallbackHandler.unregister(this);
    }

    public final void unregisterCallbacks(String detailedName) {
        CallbackHandler.unregister(this, detailedName);
    }
}
