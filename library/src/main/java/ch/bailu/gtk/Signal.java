package ch.bailu.gtk;

import java.util.ArrayList;
import java.util.List;

public class Signal {

    private final static MMap<Long, String, List<Object>> observers = new MMap<>();

    public static void put(long emitter, String signal, Object observer) {
        System.out.println(">signal>" + emitter + ">" + signal);
        if (!observers.containsKey(emitter, signal)) {
            observers.put(emitter, signal, new ArrayList<>());
        }
        observers.get(emitter, signal).add(observer);
    }

    public static List<Object> get(long emitter, String signal) {
        System.out.println("<signal<" + emitter + "<" + signal);
        return observers.get(emitter, signal);
    }
}
