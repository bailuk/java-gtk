package ch.bailu.gtk.converter;

import java.util.HashMap;
import java.util.Map;

public class WrapperTable {

    private final Map<String, String> table = new HashMap<>(50);

    private static WrapperTable INSTANCE = null;

    public static WrapperTable instance() {
        if (INSTANCE == null) {
            INSTANCE = new WrapperTable();
        }
        return INSTANCE;
    }

    private WrapperTable() {
        add("unsigned char*", "Bytes");
    }


    public void add(String ctype, String wrapper) {
        table.put(ctype, wrapper);
        StructureTable.instance().add("wrapper", wrapper);
    }

    public boolean contains(String cType) {
        return table.containsKey(cType);
    }

    public String convert(String typeName) {
        if (contains(typeName)) {
            return  "wrapper." + table.get(typeName);
        }
        return typeName;
    }


}
