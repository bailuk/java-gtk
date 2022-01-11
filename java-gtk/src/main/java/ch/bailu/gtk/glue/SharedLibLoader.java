package ch.bailu.gtk.glue;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

class SharedLibLoader {
    private final static String LIB_SHARED_NAME = "libjava-gtk-";

    public void load() throws IOException {
        System.loadLibrary(getSharedName());
    }

    private String getVersion() throws IOException {
        var result = "";
        URLClassLoader cl = (URLClassLoader) LibResourceLoader.class.getClassLoader();
        URL url = cl.findResource("META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(url.openStream());
        Attributes attr = manifest.getMainAttributes();
        var version = manifest.getMainAttributes().getValue("build-version");

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
