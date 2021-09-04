package ch.bailu.gtk;

import java.util.ArrayList;
import java.util.List;

public class Signal {

    private final static MMap<Long, String, List<Object>> observers = new MMap<>();

    public static void put(long emitter, String signal, Object observer) {
        if (!observers.containsKey(emitter, signal)) {
            observers.put(emitter, signal, new ArrayList<>());
        }
        observers.get(emitter, signal).add(observer);
    }

    public static List<Object> get(long emitter, String signal) {
        return observers.get(emitter, signal);
    }
}
