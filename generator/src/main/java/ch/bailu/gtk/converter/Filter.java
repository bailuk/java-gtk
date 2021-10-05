package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;
import ch.bailu.gtk.model.ParameterModel;
import ch.bailu.gtk.tag.ParameterTag;

public class Filter {
    public static boolean values(String name, String value) {
        return     !"2147483648".equals(value)
                && !"9223372036854775807".equals(value)
                && !"4294967295".equals(value)
                && !"18446744073709551615".equals(value)
                && !"-9223372036854775808".equals(value)
                && !"86400000000".equals(value)
                && !"3600000000".equals(value);
    }

    public static boolean method(ClassModel classModel, MethodModel methodModel) {

        if ("MenuItem".equals(classModel.getApiName()) && "activate".equals(methodModel.getApiName())) {
            return false;
        }

        if ("ToolPalette".equals(classModel.getApiName()) && "getStyle".equals(methodModel.getApiName())) {
            return false;
        }

        if ("Toolbar".equals(classModel.getApiName()) && "getStyle".equals(methodModel.getApiName())) {
            return false;
        }

        if ("Coverage".equals(classModel.getApiName()) && "ref".equals(methodModel.getApiName())) {
            return false;
        }

/*
        if ("Cairo".equals(classModel.getApiName()) && "imageSurfaceCreate".equals(methodModel.getApiName())) {
            return false;
        }
*/

        if ("PrintSettings".equals(classModel.getApiName()) && "get".equals(methodModel.getApiName())) {
            return false;
        }


        return true;
    }


    public static boolean field(ClassModel classModel, ParameterModel parameterModel) {
        if ("PixbufAnimationIterClass".equals(classModel.getApiName())) {
            return false;
        }

        if ("SettingsBackendClass".equals(classModel.getApiName())) {
            return false;
        }

        if ("PixbufFormat".equals(classModel.getApiName())) {
            return false;
        }

        if ("PixbufModule".equals(classModel.getApiName())) {
            return false;
        }

        if ("PixbufModulePattern".equals(classModel.getApiName())) {
            return false;
        }

        return true;
    }

    /**
     * List of records that support malloc constructor
     */
    private static final String[] MALLOC = {
            "RGBA",
            "Rectangle",
            "Matrix"
    };


    public static boolean createMalloc(ClassModel classModel) {
        for (String s : MALLOC) {
            if (s.equals(classModel.getApiName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean fieldDirectAccessAllowed(ClassModel classModel, ParameterTag field) {
        return "AttrShape".equals(classModel.getApiName());
    }
}
