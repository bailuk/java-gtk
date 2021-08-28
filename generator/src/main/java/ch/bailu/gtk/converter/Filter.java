package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;

public class Filter {
    public static boolean flags(String name, String value) {
        return !"2147483648".equals(value);
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

        if ("Cairo".equals(classModel.getApiName()) && "imageSurfaceCreate".equals(methodModel.getApiName())) {
            return false;
        }



        return true;
    }

    private static final String[] MALLOC = {
            "RGBA"
    };


    public static boolean createMalloc(ClassModel classModel) {
        for (String s : MALLOC) {
            if (s.equals(classModel.getApiName())) {
                return true;
            }
        }

        return false;
    }
}
