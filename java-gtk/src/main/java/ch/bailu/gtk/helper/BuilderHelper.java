package ch.bailu.gtk.helper;

import java.io.File;
import java.io.IOException;

import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gtk.Builder;
import ch.bailu.gtk.lib.resources.JavaResource;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class BuilderHelper {
    private final Builder builder = new Builder();

    private BuilderHelper(String ui) throws AllocationError {
        var uiStr = new Str(ui);
        builder.addFromString(ui, uiStr.getLength());
    }
    private BuilderHelper(File file) throws AllocationError {
        Str path = new Str(file.getAbsolutePath());
        builder.addFromFile(file.getAbsolutePath());
        path.destroy();
    }

    public CPointer getObject(String name) {
        var strName = new Str(name);
        var result = builder.getObject(name).cast();
        strName.destroy();
        return result;
    }

    public static BuilderHelper fromString(String ui) throws AllocationError {
        return new BuilderHelper(ui);
    }

    public static BuilderHelper fromFile(String path, String file) throws AllocationError {
        return new BuilderHelper(new File(path, file));
    }

    public static BuilderHelper fromFile(String path) throws AllocationError {
        return new BuilderHelper(new File(path));
    }

    public static BuilderHelper fromFile(File file) throws AllocationError {
        return new BuilderHelper(file);
    }

    public static BuilderHelper fromResource(String path) throws IOException, AllocationError {
        return new BuilderHelper(new JavaResource(path).asString());
    }
}
