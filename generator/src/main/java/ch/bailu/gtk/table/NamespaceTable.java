package ch.bailu.gtk.table;

import java.util.HashMap;
import java.util.Map;

public class NamespaceTable {
    private final Map<String, String> table = new HashMap<>();


    private final static NamespaceTable INSTANCE = new NamespaceTable();

    public static NamespaceTable instance() {
        return INSTANCE;
    }

    private NamespaceTable() {}

    public void add(String namespace) {
        table.put(namespace.toLowerCase(), namespace);
    }


    public boolean contains(String namespace) {
        return table.containsKey(namespace);
    } 
}
