package ch.bailu.gtk.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.converter.Filter;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.tag.EnumerationTag;
import ch.bailu.gtk.tag.MemberTag;
import ch.bailu.gtk.tag.MethodTag;
import ch.bailu.gtk.tag.ParameterTag;
import ch.bailu.gtk.tag.StructureTag;
import ch.bailu.gtk.writer.CodeWriter;

public class ClassModel extends Model {
    private String name;
    private NameSpaceModel nameSpace;

    private ClassModel parent;

    private List<MethodModel> privateFactories = new ArrayList();
    private List<MethodModel> factories = new ArrayList();
    private List<MethodModel> constructors = new ArrayList();
    private List<MethodModel> methods = new ArrayList();
    private List<MethodModel> signals = new ArrayList();
    private List<ParameterModel> fields = new ArrayList();
    private List<Model>       unsupported = new ArrayList();

    private List<ParameterModel> constants = new ArrayList();

    private String type;  // record, enum, class, interface, bitfield


    public ClassModel(StructureTag structure, NameSpaceModel nameSpace) {
        this.nameSpace = nameSpace;
        type = structure.getType();
        name = structure.getName();
        parent = new ClassModel(nameSpace.getNamespace(), structure.getParentName(), type);


        for (MethodTag m: structure.getConstructors()) {
            addIfSupported(privateFactories, filter(new MethodModel(nameSpace.getNamespace(), m)));
        }

        for (MethodModel factory: privateFactories) {
            if (factory.isConstructorType()) {
                constructors.add(new MethodModel(name, factory));
            } else {
                factories.add(new MethodModel(name, factory));
            }
        }

        for (MethodTag method: structure.getMethods()) {
            addIfSupported(methods, filter(new MethodModel(nameSpace.getNamespace(), method)));
        }

        for (MethodTag signal: structure.getSignals()) {
            addIfSupported(signals, new MethodModel(nameSpace.getNamespace(), signal));
        }

        for (ParameterTag field: structure.getFields()) {
            addIfSupported(fields, new ParameterModel(nameSpace.getNamespace(), field));

        }
    }

    private Model filter(MethodModel methodModel) {
        methodModel.setSupported("Filter", Filter.method(this, methodModel));
        return methodModel;
    }

    public ClassModel(EnumerationTag enumeration, NameSpaceModel namespace) {
        this.nameSpace = namespace;
        type = "enumeration";
        name = enumeration.getName();

        for (MemberTag m : enumeration.getMembers()) {
            addIfSupported(constants, new ParameterModel(m));
        }

    }

    private void addIfSupported(List models, Model model) {
        if (model.isSupported()) {
            models.add(model);
        } else {
            unsupported.add(model);
        }
    }


    // parent initializer
    private ClassModel(String defaultNamespace, String className, String structType) {
	    if (className == null) {
            nameSpace = new NameSpaceModel();

            if ("record".equalsIgnoreCase(structType)) {
                name = nameSpace.getFullNamespace() + ".Record";
            } else {
                name = nameSpace.getFullNamespace() + ".Pointer";
            }

        } else {
            NamespaceType type = new NamespaceType(defaultNamespace, className);
            nameSpace = new NameSpaceModel(type);

            if (nameSpace.isSupported()) {
                if (type.hasCurrentNamespace()) {
                    name = type.getName();
                } else {
                    name = nameSpace.getFullNamespace() + "."  + type.getName();
                }

            } else {
                nameSpace = new NameSpaceModel();
                name = nameSpace.getFullNamespace() + ".Outsider";
            }
        }
    }


    public boolean hasNativeCalls() {
        return isNameSpaceSupported() && isClassType() && (methods.size() >0 || privateFactories.size() > 0 || signals.size()>0 || fields.size()>0);
    }

    private boolean isClassType() {
        return "class".equals(type) || "record".equals(type) || "interface".equals(type);
    }

    private boolean isNameSpaceSupported() {
        return nameSpace.isSupported();
    }

    public void write(CodeWriter writer) throws IOException {
        writer.writeStart(this, nameSpace);
        writer.next();

        if (isClassType()) {
            writer.writeClass(this);

            writer.next();
            for (MethodModel m : privateFactories) {
                writer.writePrivateFactory(this, m);
            }

            writer.next();
            for (MethodModel m : factories) {
                writer.writeFactory(this, m);
            }

            writer.next();
            writer.writeInternalConstructor(this);

            writer.next();
            for (MethodModel m : constructors) {
                writer.writeConstructor(this, m);
            }

            writer.next();
            for (ParameterModel p: fields) {
                writer.writeField(this, p);
            }

            writer.next();
            for (MethodModel m : methods) {
                writer.writeNativeMethod(this, m);
            }

            writer.next();
            for (MethodModel s : signals) {
                writer.writeSignal(this, s);
            }
        } else {
            writer.writeInterface(this);

            writer.next();
            for (ParameterModel p: constants) {
                writer.writeConstant(p);
            }
        }

        writer.next();
        for (Model m : unsupported) {
            writer.writeUnsupported(m);
        }

        writer.writeEnd();
    }

    public String getImpName() {
        return "Imp" + name;
    }

    public String getApiName() {
        return name;
    }

    public String getApiParentName() {
        return parent.getApiName();
    }


    public String getHeaderFileName() {
        return nameSpace.getHeaderFileBase() + getImpName() + ".h";
    }

    public String getJniMethodName(MethodModel m) {
            return Configuration.JNI_METHOD_NAME_BASE + nameSpace.getNamespace()  +"_" + getImpName() + "_" + m.getApiName();
    }

    public String getJniSignalConnectMethodName(MethodModel m) {
        return Configuration.JNI_METHOD_NAME_BASE + nameSpace.getNamespace()  +"_" + getImpName() + "_" + m.getSignalMethodName();
    }

    public String getCSignalCallbackName(MethodModel m) {
        return nameSpace.getNamespace()  +"_" + getImpName() + "_" + m.getSignalMethodName();
    }

    public String getGlobalName(String name) {
        return nameSpace.getNamespace() + "_" + getImpName() + "_" + name;
    }

    public boolean isRecord() {
        return "record".equals(type);
    }
}
