package ch.bailu.gtk.model;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.converter.AliasTable;
import ch.bailu.gtk.converter.CallbackTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.RelativeNamespaceType;
import ch.bailu.gtk.converter.StructureTable;
import ch.bailu.gtk.converter.WrapperTable;
import ch.bailu.gtk.tag.CallbackTag;
import ch.bailu.gtk.tag.ParameterTag;


public class ClassType implements ClassTypeInterface {

    private final RelativeNamespaceType type;


    private boolean valid = false;
    private CallbackTag callbackTag = null;


    public ClassType() {
        type = new RelativeNamespaceType("", "");
    }

    
    public ClassType(String namespace, ParameterTag parameter) {
        this(namespace, parameter.getTypeName(), parameter.getType());
    }

    public ClassType(String namespace, String typeName, String ctype) {
        this(namespace, typeName, new CType(ctype));
    }
    
    public ClassType(String namespace, String typeName, CType ctype) {
        typeName = convert(typeName);

        type = convert(namespace, typeName);
        callbackTag = getCallbackTagFromTable(type);
        valid = (callbackTag != null) || (isInStructureTable(type) && ctype.isSinglePointer());
    }

    private String convert(String typeName) {
        return WrapperTable.instance().convert(typeName);
    }

    private RelativeNamespaceType convert(String namespace, String typeName) {
        NamespaceType converted = AliasTable.instance().convert(new NamespaceType(namespace, typeName));
        return new RelativeNamespaceType(namespace, converted);
    }

    public CallbackTag getCallbackTag() {
        return callbackTag;
    }

    private CallbackTag getCallbackTagFromTable(RelativeNamespaceType n) {
        return CallbackTable.instance().get(n.getNamespace(), n.getName());
    }

    private boolean isInStructureTable(RelativeNamespaceType n) {
        return StructureTable.instance().contains(n.getNamespace(), n.getName());
    }



    public boolean isClass() {
        return valid;
    }

    public boolean isCallback() {
        return callbackTag != null;
    }


    public String getFullName() {
        if (isClass() && !type.hasCurrentNamespace()) {
                return getFullNamespace() + "." + type.getName();
        }
        return getName();
    }

    @Override
    public String getNamespace() {
        return type.getNamespace();
    }

    @Override
    public String getFullNamespace() {
        return Configuration.BASE_NAME_SPACE_DOT + type.getNamespace();
    }

    @Override
    public String getName() {
        return type.getName();
    }


}
