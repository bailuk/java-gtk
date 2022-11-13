package ch.bailu.gtk.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Util class for location-independent access to program specific resources.
 * To read files from "resources/" or from the applications jar archive
 */
public class JavaResource {
    private final String resourcePath;

    /**
     * Creates a JavaResource object holding a relative path
     * to a file in "resources/" or the root directory of the jar archive
     * @param path like "css/app.css"
     */
    public JavaResource(String path) {
        this.resourcePath = path;
    }

    /**
     * Read entire text resource into string
     * @return String containing the entire text resource
     * @throws IOException If file does not exist
     */
    public String asString() throws IOException {
        try (InputStream inputStream = asStream()) {
            var scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
            return scanner.next();
        }
    }

    /**
     * Open the program specific resource file and return it as a readable stream
     * @return InputStream for reading resource file
     * @throws IOException If file does not exist
     */
    public InputStream asStream() throws IOException {
        var resourceStream = this.getClass().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new IOException("Failed to load '" + resourcePath + "'");
        }
        return resourceStream;
    }
}
