package ch.bailu.gtk.builder;

import ch.bailu.gtk.table.AliasTable;
import ch.bailu.gtk.table.CallbackTable;
import ch.bailu.gtk.table.EnumTable;
import ch.bailu.gtk.table.NamespaceTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.table.StructureTable;
import ch.bailu.gtk.tag.AliasTag;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.NamespaceTag;
import ch.bailu.gtk.tag.StructureTag;

public class AliasBuilder implements BuilderInterface{


    private String namespace = "";


    @Override
    public void buildStructure(StructureTag structure) {
        StructureTable.instance().add(namespace, convert(namespace, structure.getName()));
    }

    private String convert(String namespace, String name) {
        return AliasTable.instance().convert(new NamespaceType(namespace, name)).getName();
    }

    @Override
    public void buildNamespaceStart(NamespaceTag namespace) {
        this.namespace = namespace.getName().toLowerCase();
        NamespaceTable.instance().add(this.namespace);
    }

    @Override
    public void buildNamespaceEnd(NamespaceTag namespace) {}

    @Override
    public void buildAlias(AliasTag aliasTag) {
        AliasTable.instance().add(namespace, aliasTag.getName(), aliasTag.getTypeName());
    }

    @Override
    public void buildEnumeration(EnumerationTag enumeration) {
        EnumTable.instance().add(new NamespaceType(namespace, enumeration.getName()));
        EnumTable.instance().add(new NamespaceType(namespace, enumeration.getType()));
    }

    @Override
    public void buildCallback(CallbackTag callbackTag) {
        CallbackTable.instance().add(namespace, callbackTag);
    }
}
