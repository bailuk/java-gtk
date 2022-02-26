package ch.bailu.gtk;

import java.util.HashMap;
import java.util.Map;

public class Refs {
    private static final Map<Object, Object> REFS = new HashMap<>();

    public synchronized static void add(Object object) {
        REFS.put(object, object);
        log();
    }

    public synchronized static void add(Object key, Object value) {
        REFS.put(key, value);
        log();
    }

    public synchronized static void remove(Object key) {
        REFS.remove(key);
        log();
    }


    private static void log() {
        System.out.println("REFS: " + REFS.size());
    }
}
