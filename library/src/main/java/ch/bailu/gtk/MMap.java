package ch.bailu.gtk;

import java.util.HashMap;
import java.util.Map;

public class MMap <K1, K2, V> {
    private Map <K1, Map<K2, V>> map = new HashMap<>();

    public void put(K1 k1, K2 k2, V v) {
        if (!containsKey(k1)) {
            map.put(k1, new HashMap<>());
        }

        get(k1).put(k2, v);
    }


    public boolean containsKey(K1 k1, K2 k2) {
        return containsKey(k1) && get(k1).containsKey(k2);
    }

    private boolean containsKey(K1 k1) {
        return map.containsKey(k1);
    }


    public V get(K1 k1, K2 k2) {
        if (containsKey(k1)) {
            return get(k1).get(k2);
        }
        return null;
    }

    private Map<K2, V> get(K1 k1) {
        return map.get(k1);
    }

}
