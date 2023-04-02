package ch.bailu.gtk.type.gobject;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.TypeQuery;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;

public class TypeSystem {

    // TODO obtain this from api
    public static final long GTYPE_NONE = 1 << GobjectConstants.TYPE_FUNDAMENTAL_SHIFT;

    public static long registerClass(
            long parentType,
            Class<? extends ch.bailu.gtk.gobject.Object> _class,
            int instanceSize,
            Gobject.OnClassInitFunc classInit,
            Gobject.OnInstanceInitFunc instanceInit) {
            return registerClass(parentType, new Str(_class.getSimpleName()), instanceSize, classInit, instanceInit);
    }

    public static long registerClass(
            long parentType,
            Str typeName,
            int instanceSize,
            Gobject.OnClassInitFunc classInit,
            Gobject.OnInstanceInitFunc instanceInit
    ) {
        final var parentSize = getTypeSize(parentType);
        return Gobject.typeRegisterStaticSimple(
                parentType,
                typeName,
                parentSize.classSize,
                classInit,
                parentSize.instanceSize + instanceSize,
                instanceInit,
                0);
    }

    public static void ensure(long typeID) {
        Gobject.typeEnsure(typeID);
    }

    public static class Property {
        public final Str name;
        public final long value;

        public Property(Str name, long value) {
            this.name = name;
            this.value = value;
        }

        public Property(Str name, Pointer value) {
            this.name = name;
            this.value = value.asCPointer();
        }

        public static Str getFirstArgument(Property... properties) {
            if (properties.length > 0) {
                return properties[0].name;
            }
            return Str.NULL;
        }

        public static Object[] getRemainingArguments(Property... properties) {
            Long[] result = new Long[properties.length * 2];

            if (result.length > 0) {
                int r = 0;
                int i = 0;

                result[r++] = properties[i].value;

                while (++i < properties.length) {
                    result[r++] = properties[i].name.asCPointer();
                    result[r++] = properties[i].value;
                }
                result[r] = 0L;
            }
            return result;
        }
    }

    public static PointerContainer newInstance(long type, Property... properties) {
        return new ch.bailu.gtk.gobject.Object(type, Property.getFirstArgument(properties), Property.getRemainingArguments(properties)).cast();
    }

    public static class TypeSize {
        public final int instanceSize;
        public final int classSize;

        public TypeSize(int instanceSize, int classSize) {
            this.instanceSize = instanceSize;
            this.classSize = classSize;
        }
    }

    public static TypeSize getTypeSize(long type) {
        final var typeQuery = new TypeQuery();

        Gobject.typeQuery(type, typeQuery);
        final var result = new TypeSize(typeQuery.getFieldInstanceSize(), typeQuery.getFieldClassSize());

        typeQuery.destroy();
        return result;
    }
}
