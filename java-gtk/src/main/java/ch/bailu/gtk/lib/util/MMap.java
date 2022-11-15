package ch.bailu.gtk.lib.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MMap <K1, K2, V> {
    private Map <K1, Map<K2, V>> map = new HashMap<>();

    public synchronized void put(K1 k1, K2 k2, V v) {
        if (!containsKey(k1)) {
            map.put(k1, new HashMap<>());
        }
        get(k1).put(k2, v);
    }

    private boolean containsKey(K1 k1) {
        return map.containsKey(k1);
    }

    public synchronized V get(K1 k1, K2 k2) {
        if (containsKey(k1)) {
            return get(k1).get(k2);
        }
        return null;
    }

    private Map<K2, V> get(K1 k1) {
        return map.get(k1);
    }

    public synchronized void remove(K1 k1) {
        map.remove(k1);
    }

    public synchronized void remove(K1 k1, K2 k2) {
        if (containsKey(k1)) {
            get(k1).remove(k2);
            if (get(k1).isEmpty()) {
                remove(k1);
            }
        }
    }


    public synchronized Collection<V> getValues(K1 k1) {
        if (containsKey(k1)) {
            return get(k1).values();
        }
        return Collections.emptySet();
    }

    public synchronized long size() {
        return map.values().stream().mapToLong(Map::size).sum();
    }

    public Set<K1> keySet() {
        return map.keySet();
    }
}
