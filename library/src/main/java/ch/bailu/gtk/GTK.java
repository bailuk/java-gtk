package ch.bailu.gtk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class GTK {
    private final static String LIB_NAME = "glue.so";
    private final static String TMP_DIR = "java-gtk-library";

    
    public static void init() throws IOException {
        try {
            System.loadLibrary(LIB_NAME);
        } catch (UnsatisfiedLinkError e) {
            loadLibrary();
        }
    }


    private static void loadLibrary() throws IOException {
        URL src = GTK.class.getResource("/lib" + LIB_NAME);
        File lib = getLibFile(getTempDir());

        if (src == null) throw new IOException("no library found");
        try (InputStream in = src.openStream()) {
            Files.copy(in, lib.toPath());
            System.load(lib.getAbsolutePath());
        }
    }


    private static File getLibFile(File dir) {
        File result = new File(dir, LIB_NAME);
        result.deleteOnExit();
        return result;
    }

    private static File getTempDir() throws IOException {
        File result = Files.createTempDirectory(TMP_DIR).toFile();
        result.deleteOnExit();
        return result;
    }

}
