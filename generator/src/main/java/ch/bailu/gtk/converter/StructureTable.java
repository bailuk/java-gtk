package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class StructureTable {
    private final Map<String, Map<String, String>> table = new HashMap<>();


    private final static StructureTable INSTANCE = new StructureTable();

    public static StructureTable instance() {
        return INSTANCE;
    }

    private StructureTable() {}

    public void add(String namespace, String name) {
        getTable(namespace).put(name, name);
    }

    private Map<String, String> getTable(String namespace) {
        Map<String, String> result = table.get(namespace);

        if (result == null) {
            result = new HashMap<String, String>();
            table.put(namespace, result);
        }
        return result;
    }

    public boolean contains(String namespace, String name) {
        return getTable(namespace).get(name) != null;
    } 
}
