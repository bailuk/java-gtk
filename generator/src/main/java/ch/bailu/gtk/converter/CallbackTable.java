package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

import ch.bailu.gtk.tag.CallbackTag;

public class CallbackTable {
    private final Map<String, Map<String, CallbackTag>> table = new HashMap<>();


    private final static CallbackTable INSTANCE = new CallbackTable();

    public static CallbackTable instance() {
        return INSTANCE;
    }

    private CallbackTable() {}

    public void add(String namespace, CallbackTag callbackTag) {
        getTable(namespace).put(callbackTag.getName(), callbackTag);
    }

    private Map<String, CallbackTag> getTable(String namespace) {
        Map<String, CallbackTag> result = table.get(namespace);

        if (result == null) {
            result = new HashMap<>();
            table.put(namespace, result);
        }
        return result;
    }

    public boolean contains(String namespace, String name) {
        return getTable(namespace).get(name) != null;
    }

    public CallbackTag get(String namespace, String name) {
        return getTable(namespace).get(name);
    }
}
