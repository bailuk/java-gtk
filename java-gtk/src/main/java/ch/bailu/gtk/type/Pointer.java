package ch.bailu.gtk.type;


import com.sun.jna.Callback;

import java.util.Objects;

import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;

public class Pointer extends Type implements CPointerInterface {

    public final static Pointer NULL = new Pointer(CPointer.NULL);

    private final CPointer pointer;

    /**
     * Casting constructor to access another interface.
     *
     * @see ch.bailu.gtk.type.Pointer#cast()
     *
     * @param pointer Wraps a C pointer of a GTK class or record
     */
    public Pointer(CPointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public final long getCPointer() {
        return pointer.getCPointer();
    }

    public static com.sun.jna.Pointer toJnaPointer(Pointer p) {
        return toJnaPointer(p.getCPointer());
    }

    public static com.sun.jna.Pointer toJnaPointer(long p) {
        return new com.sun.jna.Pointer(p);
    }

    public static Pointer toPointer(com.sun.jna.Pointer jnaPointer) {
        return new Pointer(toCPointer(jnaPointer));
    }

    public static Pointer toPointer(long nativePointer) {
        return new Pointer(toCPointer(nativePointer));
    }

    public static CPointer toCPointer(long p) {
        return new CPointer(p);
    }

    public static CPointer toCPointer(com.sun.jna.Pointer p) {
        return new CPointer(toNativePointer(p));
    }

    public static long toNativePointer(com.sun.jna.Pointer p) {
        return com.sun.jna.Pointer.nativeValue(p);
    }

    /**
     * Pass the return value of this function to the casting
     * constructor of any class derived from Pointer
     *
     * @see ch.bailu.gtk.type.Pointer#Pointer(CPointer)
     * @see ch.bailu.gtk.type.Pointer
     *
     * @return CPointer (wraps a C pointer of a GTK class or record)
     */
    public final CPointer cast() {
        return pointer;
    }

    @Override
    public int hashCode() {
        return pointer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Pointer) {
            return Objects.equals(((Pointer)obj).pointer, pointer);
        }
        return false;
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
        if (pointer.isNull()) {
            throwNullPointerException("pointer == 0");
        }
    }

    @Override
    public final boolean isNotNull() {
        return pointer.isNotNull();
    }

    @Override
    public final boolean isNull() {
        return pointer.isNull();
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
