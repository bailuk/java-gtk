package examples.jna;

import jnr.ffi.LibraryLoader;

public class JnaApplication {
    private static Instance INSTANCE;

    public static Instance INST() {
        if (INSTANCE == null) {
            INSTANCE = LibraryLoader.create(Instance.class).load("gtk-4");
        }
        return INSTANCE;
    }

    public interface Instance {
        long gtk_application_new(long application_id, int flags);

        void gtk_application_add_window(long _self, long window);
        long gtk_application_get_accels_for_action(long _self, long detailed_action_name);
        long gtk_application_get_actions_for_accel(long _self, long accel);
        long gtk_application_get_active_window(long _self);
        long gtk_application_get_menu_by_id(long _self, long id);
        long gtk_application_get_menubar(long _self);
        long gtk_application_get_window_by_id(long _self, int id);
        long gtk_application_get_windows(long _self);
        int  gtk_application_inhibit(long _self, long window, int flags, long reason);
        long gtk_application_list_action_descriptions(long _self);
        void gtk_application_remove_window(long _self, long window);
        void gtk_application_set_accels_for_action(long _self, long detailed_action_name, long accels);
        void gtk_application_set_menubar(long _self, long menubar);
        void gtk_application_uninhibit(long _self, int __cookie);
        long gtk_application_get_type();
    }

}
