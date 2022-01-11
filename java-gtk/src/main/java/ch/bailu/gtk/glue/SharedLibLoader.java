package ch.bailu.gtk.glue;

import java.io.IOException;
import java.util.jar.Manifest;

class SharedLibLoader {
    private final static String LIB_SHARED_NAME = "/usr/lib/jni/libjava-gtk-";

    public void load() throws IOException {
        System.load(getSharedName());
    }

    private String getVersion() throws IOException {
        var result = "";
        var cl = LibResourceLoader.class.getClassLoader();
        var url = cl.getResource("META-INF/MANIFEST.MF");
        var manifest = new Manifest(url.openStream());
        var attr = manifest.getMainAttributes();
        var version = attr.getValue("build-version");

        if (version != null) {
            result = version;
        }
        return result;
    }

    private String getSharedName() throws IOException {
        var version = getVersion();
        return LIB_SHARED_NAME + version + ".so";
    }
}
