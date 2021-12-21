package ch.bailu.gtk.helper;

import java.io.File;

import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gtk.Builder;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class BuilderHelper {
    private final Builder builder = new Builder();

    public BuilderHelper(String path, String file) throws AllocationError {
        this(new File(path, file));
    }

    public BuilderHelper(File file) throws AllocationError {
        Str path = new Str(file.getAbsolutePath());
        builder.addFromFile(path);
        path.destroy();
    }

    public CPointer getObject(String name) {
        var n = new Str(name);
        var result = builder.getObject(n).cast();
        n.destroy();
        return result;
    }
}
