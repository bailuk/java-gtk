package ch.bailu.gtk.lib.bridge;

import java.io.File;
import java.io.IOException;

import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.exception.AllocationError;
import ch.bailu.gtk.gtk.Builder;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Str;

/**
 * Load user interface from XML descriptions (.ui file).
 * Facade for {@link ch.bailu.gtk.gtk.Builder}.
 */
public class UiBuilder {
    private final Builder builder = new Builder();

    private UiBuilder(String ui) throws AllocationError {
        var uiStr = new Str(ui);
        builder.addFromString(ui, uiStr.getLength());
    }
    private UiBuilder(File file) throws AllocationError {
        Str path = new Str(file.getAbsolutePath());
        builder.addFromFile(file.getAbsolutePath());
        path.destroy();
    }

    public PointerContainer getObject(String name) {
        var strName = new Str(name);
        var result = builder.getObject(name).cast();
        strName.destroy();
        return result;
    }

    /**
     * Load ui from string
     * @param ui String containing XML description of ui
     * @return UiBuilder instance for accessing widgets
     */
    public static UiBuilder fromString(String ui) throws AllocationError {
        return new UiBuilder(ui);
    }

    public static UiBuilder fromFile(String path, String file) throws AllocationError {
        return new UiBuilder(new File(path, file));
    }

    public static UiBuilder fromFile(String path) throws AllocationError {
        return new UiBuilder(new File(path));
    }

    /**
     * Load ui from file on file system
     * @param file path to file on file system
     * @return UiBuilder instance for accessing widgets
     */
    public static UiBuilder fromFile(File file) throws AllocationError {
        return new UiBuilder(file);
    }

    /**
     * Load ui file from resource
     * @param path relative path to resource
     * @return UiBuilder instance for accessing widgets
     */
    public static UiBuilder fromResource(String path) throws IOException, AllocationError {
        return new UiBuilder(new JavaResource(path).asString());
    }
}
