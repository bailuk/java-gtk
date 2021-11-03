package ch.bailu.gtk;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.type.Pointer;

public class Callback {

    private final static MMap<Long, String, List<Object>> observers = new MMap<>();

    public static void put(long emitter, String signal, Object observer) {
        if (observer != null) {
            if (!observers.containsKey(emitter, signal)) {
                observers.put(emitter, signal, new ArrayList<>());
            }
            observers.get(emitter, signal).add(observer);
        }
    }

    public static List<Object> get(long emitter, String signal) {
        return observers.get(emitter, signal);
    }


    /**
     * A unique emitter id. Will be used when dispatching callbacks.
     * This can be passed as "user_data" callback parameter.
     * Prerequisite for multiple callback per callback interface.
     */
    public static class EmitterID extends Pointer {
        private static long nextID = 1;

        public EmitterID() {
            super(nextID++);
        }
    }
}
