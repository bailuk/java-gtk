package ch.bailu.gtk;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;

public class Callback {

    private final static MMap<Long, String, List<Object>> observers = new MMap<>();

    /**
     * Set a callback for an emitter/signal pair.
     * This implementation supports only one callback per emitter/signal pair.
     * If a callback exists for emitter/signal it will be replaced
     *
     * @param emitter  Unique emitter id. C-Pointer of object for signals and data parameter for callbacks
     * @param signal   The name of the signal or name of callback function
     * @param observer The observer to be called when receiving a signal
     */
    public static synchronized void put(long emitter, String signal, Object observer) {
        if (observer != null) {
            observers.put(emitter, signal, new ArrayList<>());
            observers.get(emitter, signal).add(observer);
        }
    }

    /**
     * Returns the list of observers for a emitter/signal pair
     * Returns an empty list if there are no observers
     * @param emitter Unique emitter id. C-Pointer of object for signals and data parameter for callbacks
     * @param signal The name of the signal or name of callback function
     * @return a list with zero or one observer
     */
    public static synchronized List<Object> get(long emitter, String signal) {
        if (!observers.containsKey(emitter, signal)) {
            observers.put(emitter, signal, new ArrayList<>());
        }
        return observers.get(emitter, signal);
    }

    /**
     * Remove emitter from callback observers. Call this after the receiving object has
     * been destroyed to free resources and to prevent conflicts.
     *
     * @param emitterId a unique id to reference the callback
     */
    public static synchronized void remove(Pointer emitterId) {
        remove(emitterId.getCPointer());
    }

    /**
     * Remove emitter from callback observers. Call this after the receiving object has
     * been destroyed to free resources and to prevent conflicts.
     *
     * @param emitterId a unique id to reference the callback
     */
    public static synchronized void remove(long emitterId) {
        observers.remove(emitterId);
    }


    /**
     * A unique emitter id. Will be used when dispatching callbacks.
     * This can be passed as "user_data" callback parameter.
     * Prerequisite for multiple callback per callback interface.
     */
    public static class EmitterID extends Int {}
}
