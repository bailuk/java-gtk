package ch.bailu.gtk;

import java.util.HashMap;
import java.util.Map;

public class Refs {
    private static final Map<Object, Object> REFS = new HashMap<>();
    private static long lastLog = System.currentTimeMillis();
    private static long lastSize = 0;

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
        long now = System.currentTimeMillis();

        if (now - lastLog > 5000) {
            long size = REFS.size();
            if (lastSize != size) {
                System.out.println("REFS: " + REFS.size());
            }
            lastLog = now;
            lastSize = size;
        }
    }
}
