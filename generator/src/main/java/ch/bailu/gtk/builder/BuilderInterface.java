package ch.bailu.gtk.builder;

import java.io.IOException;

import ch.bailu.gtk.tag.AliasTag;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.NamespaceTag;
import ch.bailu.gtk.tag.StructureTag;

public interface BuilderInterface {
    void buildStructure(StructureTag structure) throws IOException;
    void buildNamespace(NamespaceTag namespace);

    void buildAlias(AliasTag alias);

    void buildCallback(CallbackTag callback);

    void buildEnumeration(EnumerationTag enumeration) throws IOException;
}
