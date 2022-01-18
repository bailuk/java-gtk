package ch.bailu.gtk.glue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class SharedLibLoader {

    private final static String LIB_SHARED_PATH = "/usr/lib/jni/";
    private final static String LIB_SHARED_PREFIX = "libjava-gtk-";

    public SharedLibLoader() throws IOException, UnsatisfiedLinkError {
        try  {
            System.loadLibrary(getSharedName());
        } catch (Throwable e) {
            System.load(getSharedPath());
        }
    }


    private static String getVersion() throws IOException {
        var result = "";

        var input = getVersionReader();
        var version = input.readLine();
        input.close();

        if (version != null) {
            result = version;
        }
        return result;
    }

    private static BufferedReader getVersionReader() throws IOException {
        var url = SharedLibLoader.class.getResource("/glue/version");
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }


    private static String getSharedName() throws IOException {
        var version = getVersion();
        return LIB_SHARED_PREFIX + version + ".so";
    }

    private static String getSharedPath() throws IOException {
        var result = LIB_SHARED_PATH + getSharedName();
        System.out.println("Shared library: " + result);
        return result;
    }
}
