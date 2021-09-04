package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

import ch.bailu.gtk.tag.AliasTag;

public class AliasTable {
    private final Map<NamespaceType, NamespaceType> table = new HashMap<>();


    private final static AliasTable INSTANCE = new AliasTable();

    public static AliasTable instance() {
        return INSTANCE;
    }

    private AliasTable() {
        NamespaceType from = new NamespaceType("glib", "String");
        NamespaceType to = new NamespaceType("glib", "GString");
        add(from, to);
    }

    public void add(NamespaceType from, NamespaceType to) {
        table.put(from, to);
    }


    public NamespaceType convert(NamespaceType from) {
        NamespaceType to = table.get(from);
        if (to == null) {
            to = from;
        }
        return to;
    }

    public void add(String namespace, String fromName, String toName) {
        var from = new NamespaceType(namespace, fromName);
        var to = new NamespaceType(namespace, toName);
        add(from, to);
    }


}
