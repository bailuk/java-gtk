package ch.bailu.gtk.glue;

import java.util.Locale;

/**
 * Detect os architecture and os name and store it as a unified value.
 * The naming for os and architecture must be exactly the same as used
 * by the build system.
 *
 */
public class PlatformDetection {

    private final String os;
    private final String arch;

    PlatformDetection() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if ("amd64".equals(arch)) {
            arch = "x86_64";
        }

        this.os = os;
        this.arch = arch;
    }

    public String getOS() {
        return os;
    }

    public String getArch() {
        return arch;
    }

    public String getGluePath() {
        return "/glue/" + os + "-" + arch + "/";
    }
}