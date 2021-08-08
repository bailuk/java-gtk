package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class EnumTable {
    private final Map<String, Map<String, String>> table = new HashMap<>();


    private final static EnumTable INSTANCE = new EnumTable();

    public static EnumTable instance() {
        return INSTANCE;
    }

    private EnumTable() {}

    public void add(NamespaceType type) {
        if (type.isValid()) {
            getTable(type.getNamespace()).put(type.getName(), type.getName());
        }
    }

    private Map<String, String> getTable(String namespace) {
        Map<String, String> result = table.get(namespace);

        if (result == null) {
            result = new HashMap<>();
            table.put(namespace, result);
        }
        return result;
    }

    public boolean contains(NamespaceType type) {
        return type.isValid() && getTable(type.getNamespace()).containsKey(type.getName());
    }
}
