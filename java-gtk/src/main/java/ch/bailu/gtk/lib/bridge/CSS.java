package ch.bailu.gtk.lib.bridge;

import java.io.IOException;
import java.util.HashMap;

import ch.bailu.gtk.gdk.Display;
import ch.bailu.gtk.gtk.CssProvider;
import ch.bailu.gtk.gtk.GtkConstants;
import ch.bailu.gtk.gtk.StyleContext;
import ch.bailu.gtk.gtk.StyleProvider;
import ch.bailu.gtk.lib.util.JavaResource;

public class CSS {
    private HashMap<String, StyleProvider> styleProviderMap = new HashMap<>();

    private static CSS INST = null;

    private static CSS Inst() {
        if (INST == null) {
            INST = new CSS();
        }
        return INST;
    }

    /**
     * Add css resource file to applications global style providers
     * @param display display this CSS resource will by applied to
     * @param cssResourcePath path to java resource
     */
    public static synchronized void addProviderForDisplay(Display display, String cssResourcePath) throws IOException {
        var styleProvider = Inst().getStyleProvider(cssResourcePath);
        StyleContext.addProviderForDisplay(display, styleProvider, GtkConstants.STYLE_PROVIDER_PRIORITY_USER);
    }

    private StyleProvider getStyleProvider(String cssResourcePath) throws IOException {
        if (!styleProviderMap.containsKey(cssResourcePath)) {
            styleProviderMap.put(cssResourcePath, loadStyleProvider(cssResourcePath));
        }
        return styleProviderMap.get(cssResourcePath);
    }

    private static StyleProvider loadStyleProvider(String cssResourcePath) throws IOException {
        var cssProvider = new CssProvider();
        cssProvider.loadFromData(new JavaResource(cssResourcePath).asString(), -1);
        return new StyleProvider(cssProvider.cast());
    }
}
