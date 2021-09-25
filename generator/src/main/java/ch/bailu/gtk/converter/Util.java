package ch.bailu.gtk.converter;

import ch.bailu.gtk.table.EnumTable;
import ch.bailu.gtk.tag.ParameterTag;

public class Util {

    public static boolean isEnum(String namespace, ParameterTag parameter) {
        return (isEnum(namespace, parameter.getTypeName()) || isEnum(namespace, parameter.getType()));
    }

    private static boolean isEnum(String namespace, String typeName) {
        return EnumTable.instance().contains(new NamespaceType(namespace, typeName));
    }

}
