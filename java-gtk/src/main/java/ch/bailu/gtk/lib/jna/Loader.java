package ch.bailu.gtk.lib.jna;

import com.sun.jna.Library;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.Nonnull;

import ch.bailu.gtk.lib.util.JavaResource;

/**
 * Configurable loader for loading JNA API instances
 */
public class Loader {
    private static final String LOADER_PROPERTY = "/jna/loader.properties";

    private static Loader INST = null;
    private LibraryList libraries = new LibraryList();

    /**
     * Load Interface from library.
     * This also tries to load library from alternative library names
     *
     * @param libraryName default libraryName
     * @param interfaceClass JNA interface
     * @return Instance of interface
     */
    public static <T extends Library> T load(@Nonnull String libraryName, @Nonnull Class<T> interfaceClass) {
        return Loader.getInstance().loadInterface(libraryName, interfaceClass);
    }

    /**
     * Get Instance of Loader
     * @return Loader instance
     */
    public static Loader getInstance() {
        if (INST == null) {
            INST = new Loader();
        }
        return INST;
    }

    private Loader() {
        try {
            loadAlternatives(LOADER_PROPERTY);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Load Interface from library.
     * This also tries to load library from alternative library names
     *
     * @param libraryName default libraryName
     * @param interfaceClass JNA interface
     * @return Instance of interface
     */
    public <T extends Library> T loadInterface(@Nonnull String libraryName, @Nonnull Class<T> interfaceClass) {
        libraries.addInitial(libraryName);

        for (var lib: libraries.get(libraryName)) {
            var result = _loadInterface(lib, interfaceClass);
            if (result != null) {
                libraries.addSingle(libraryName, lib);
                return result;
            }
        }

        libraries.clear(libraryName);
        throw new UnsatisfiedLinkError("Failed to load'" + libraryName + "'");
    }

    private <T extends Library> T _loadInterface(@Nonnull String libraryName, @Nonnull Class<T> interfaceClass) {
        try {
            return com.sun.jna.Native.load(libraryName, interfaceClass);
        } catch (UnsatisfiedLinkError exception) {
            System.err.println("Failed to load " + libraryName + ": " + exception.getMessage());
            return null;
        }
    }

    /**
     * Add alternative library name to default library name
     * @param libraryName default library name
     * @param lib alternative library name
     */
    public void addAlternative(String libraryName, String lib) {
        libraries.addInitial(libraryName);
        libraries.add(libraryName, lib);
    }

    /**
     * Load alternative library names from java property file
     * Format of property file:
     *  Key: default library name
     *  Value: alt-name1,alt-name2,...
     *
     * @param resourcePath path to java property file (from resource root)
     * @throws IOException if property file can't be loaded
     */
    public void loadAlternatives(String resourcePath) throws IOException {
        var resource = new JavaResource(resourcePath);
        try (InputStream stream = resource.asStream()) {
            loadAlternatives(stream);
        }
    }

    /**
     * Load alternative library names from stream to java property file
     * Format of property file:
     *  Key: default library name
     *  Value: alt-name1,alt-name2,...
     *
     * @param stream java property file as stream
     * @throws IOException If property can't be loaded
     */
    public void loadAlternatives(InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);

        properties.stringPropertyNames().forEach(key -> {
            for(var value: properties.getProperty(key).split(",")) {
                addAlternative(key, value.trim());
            }
        });
    }
}
