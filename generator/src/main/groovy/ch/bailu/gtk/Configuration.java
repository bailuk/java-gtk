package ch.bailu.gtk;

import java.io.File;

public class Configuration {

    public static final String BASE_NAME_SPACE_DOT = "ch.bailu.gtk.";
    public static final String BASE_NAME_SPACE_NODOT = "ch.bailu.gtk";
    public static final String HEADER_FILE_BASE = "ch_bailu_gtk_";

    public static final String JNI_METHOD_NAME_BASE = "Java_ch_bailu_gtk_";

    public  final static String[][] GIR_FILES= {
        {"gobject", "GObject-2.0.gir", "glib-2.0"},
        {"gtk", "Gtk-3.0.gir", "gtk-3.0"},
        {"gio", "Gio-2.0.gir"},

/*
        {"gtk", "/usr/share/gir-1.0/Gtk-3.0.gir"},
        {"gio", "/usr/share/gir-1.0/Gio-2.0.gir"},
        {"glib", "/usr/share/gir-1.0/GLib-2.0.gir"},

        {"atk", "/usr/share/gir-1.0/Atk-1.0.gir"},


        {"pango", "/usr/share/gir-1.0/Pango-1.0.gir"},
        {"gdk", "/usr/share/gir-1.0/Gdk-3.0.gir"},
        {"gdkpixbuf", "GdkPixbuf-2.0.gir"},
*/
};

    private File javaBaseDir, cBaseDir, girBaseDir;
    
    
    private static Configuration INSTANCE;
    
    
    public static void init(String args[]) {
        INSTANCE = new Configuration(args);
    }

    public static Configuration instance() {
    	if (INSTANCE == null) {
    		throw new RuntimeException("Instance is not set");
    	}
    	return INSTANCE;
    }
    
    private Configuration(String args[]) throws RuntimeException {
        for (int i = 0; i< args.length-1; i++) {

            if ("-c".equals(args[i])) {
                cBaseDir = getDirectory(args[++i], true);

            } else if ("-j".equals(args[i])) {
                javaBaseDir = getDirectory(args[++i], true);

            } else if ("-i".equals(args[i])) {
                girBaseDir = getDirectory(args[++i], true);

            }
        }

        if (javaBaseDir == null || cBaseDir == null || girBaseDir == null) {
            System.out.println("Usage: call -i <introspective directory> -c <c sources output directory> -j <java sources output directory>\n");
            throw new RuntimeException("Missing parameter");
        }
    }
    
    private static File getDirectory(String dir, boolean create) {
        File result = new File(dir);
        
        if (create) {
            result.mkdirs();
        }

        if (!result.exists()) {
            throw new RuntimeException(dir + " does not exist.");
        }

        return result;
    }


    public String getJavaBaseDir() {
    	return javaBaseDir.getAbsolutePath();
    }
    
    public String getCBaseDir() {
    	return cBaseDir.getAbsolutePath();
    }
    

    public File getGirBaseDir() {
        return girBaseDir;
    }
}
