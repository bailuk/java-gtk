package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class AliasTable {
    private final Map<String, Map<String, String>> table = new HashMap<>();


    private final static AliasTable INSTANCE = new AliasTable();

    public static AliasTable instance() {
        return INSTANCE;
    }

    private AliasTable() {}

    public void add(String namespace, String name, String to) {
        getTable(namespace).put(name, to);
    }

    private Map<String, String> getTable(String namespace) {
        Map<String, String> result = table.get(namespace);

        if (result == null) {
            result = new HashMap<String, String>();
            table.put(namespace, result);
        }
        return result;
    }

    public String convert(String namespace, String name) {
        String result = getTable(namespace).get(name);
        if (result == null) {
            result = name;
        }
        return result;
    }
}
