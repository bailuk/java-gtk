package ch.bailu.gtk.lib.jna;

import com.sun.jna.Library;

public class AdwLib {

    private static AdwLib.Instance _INST = null;

    public static AdwLib.Instance INST() {
        if (_INST == null) {
            _INST = Loader.load("adwaita-1", AdwLib.Instance.class);
        }
        return _INST;
    }

    public interface Instance extends Library {
        // TODO enum types are not yet supported by API
        long adw_leaflet_transition_type_get_type();
    }

}
