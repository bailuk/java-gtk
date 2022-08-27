package ch.bailu.gtk.lib.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JavaResource {
    private final String path;

    public JavaResource(String path) {
        this.path = path;
    }

    public InputStream asStream() throws IOException {
        var resource = this.getClass().getResourceAsStream(path);
        if (resource == null) {
            throw new IOException("Failed to load '" + path + "'");
        }
        return resource;
    }

    public String asString() throws IOException {
        try (InputStream inputStream = asStream()) {
            var scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
            return scanner.next();
        }
    }
}
