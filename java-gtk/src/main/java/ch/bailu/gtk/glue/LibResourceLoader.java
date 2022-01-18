package ch.bailu.gtk.glue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import ch.bailu.gtk.GTK;

class LibResourceLoader {
    private final static String LIB_NAME = "libglue.so";
    private final static String TMP_DIR = "java-gtk-glue";

    public LibResourceLoader() throws IOException, UnsatisfiedLinkError {
        PlatformDetection platformDetection = new PlatformDetection();
        loadLibrary(platformDetection.getGluePath(), TMP_DIR, LIB_NAME);
    }

    private static void loadLibrary(String res, String tmp, String file) throws IOException {
        URL src = GTK.class.getResource(res + file);
        File lib = getLibFile(getTempDir(tmp), file);

        if (src == null) {
            final String msg = "'" + file + "' does not exist in '" + res + "'";
            throw new IOException(msg);
        }

        try (InputStream in = src.openStream()) {
            Files.copy(in, lib.toPath());
            System.load(lib.getAbsolutePath());
        }
    }


    private static File getLibFile(File tmp, String file) {
        File result = new File(tmp, file);
        result.deleteOnExit();
        return result;
    }

    private static File getTempDir(String dir) throws IOException {
        File result = Files.createTempDirectory(dir).toFile();
        result.deleteOnExit();
        return result;
    }
}
