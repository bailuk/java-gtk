package ch.bailu.gtk.builder;

import java.io.IOException;

import ch.bailu.gtk.tag.AliasTag;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.NamespaceTag;
import ch.bailu.gtk.tag.StructureTag;

public interface BuilderInterface {
    void buildStructure(StructureTag structure) throws IOException;
    void buildNamespaceStart(NamespaceTag namespace);
    void buildNamespaceEnd(NamespaceTag namespace) throws IOException;
    void buildAlias(AliasTag alias);
    void buildEnumeration(EnumerationTag enumeration) throws IOException;
    void buildCallback(CallbackTag callbackTag) throws IOException;
}
