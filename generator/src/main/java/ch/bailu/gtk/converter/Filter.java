package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;

public class Filter {
    public static boolean flags(String name, String value) {
        return !"2147483648".equals(value);
    }

    public static boolean method(ClassModel classModel, MethodModel methodModel) {

        if ("MenuItem".equals(classModel.getName()) && "activate".equals(methodModel.getName())) {
            return false;
        }

        if ("ToolPalette".equals(classModel.getName()) && "getStyle".equals(methodModel.getName())) {
            return false;
        }

        if ("Toolbar".equals(classModel.getName()) && "getStyle".equals(methodModel.getName())) {
            return false;
        }


        return true;
    }
}
