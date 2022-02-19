package examples.jna;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import jnr.ffi.annotations.Delegate;

public class Application extends ch.bailu.gtk.gio.Application {
    public Application(CPointer pointer) {
        super(pointer);
    }

  
    public Application(@Nullable ch.bailu.gtk.type.Str application_id, int flags) {
        super(new CPointer(JnaApplication.INST().gtk_application_new(toCPointer(application_id), flags)));
    }

    public void addWindow(@Nonnull Window window)  {
        JnaApplication.INST().gtk_application_add_window(getCPointer(), toCPointerNotNull(window));
    }

    public ch.bailu.gtk.type.Strs getAccelsForAction(@Nonnull ch.bailu.gtk.type.Str detailed_action_name)  {
        return new ch.bailu.gtk.type.Strs(new CPointer(JnaApplication.INST().gtk_application_get_accels_for_action(getCPointer(), toCPointerNotNull(detailed_action_name))));
    }
    public ch.bailu.gtk.type.Strs getActionsForAccel(@Nonnull ch.bailu.gtk.type.Str accel)  {
        return new ch.bailu.gtk.type.Strs(new CPointer(JnaApplication.INST().gtk_application_get_actions_for_accel(getCPointer(), toCPointerNotNull(accel))));
    }
    public Window getActiveWindow()  {

        return new Window(new CPointer(JnaApplication.INST().gtk_application_get_active_window(getCPointer())));
    }
    public ch.bailu.gtk.gio.Menu getMenuById(@Nonnull ch.bailu.gtk.type.Str id)  {

        return new ch.bailu.gtk.gio.Menu(new CPointer(JnaApplication.INST().gtk_application_get_menu_by_id(getCPointer(), toCPointerNotNull(id))));
    }

    public ch.bailu.gtk.gio.MenuModel getMenubar()  {

        return new ch.bailu.gtk.gio.MenuModel(new CPointer(JnaApplication.INST().gtk_application_get_menubar(getCPointer())));
    }
    public Window getWindowById(int id)  {

        return new Window(new CPointer(JnaApplication.INST().gtk_application_get_window_by_id(getCPointer(), id)));
    }
    public ch.bailu.gtk.glib.List getWindows()  {

        return new ch.bailu.gtk.glib.List(new CPointer(JnaApplication.INST().gtk_application_get_windows(getCPointer())));
    }
    public int inhibit(@Nullable Window window, int flags, @Nullable ch.bailu.gtk.type.Str reason)  {

        return JnaApplication.INST().gtk_application_inhibit(getCPointer(), toCPointer(window), flags, toCPointer(reason));
    }
    public ch.bailu.gtk.type.Strs listActionDescriptions()  {

        return new ch.bailu.gtk.type.Strs(new CPointer(JnaApplication.INST().gtk_application_list_action_descriptions(getCPointer())));
    }
    public void removeWindow(@Nonnull Window window)  {
        JnaApplication.INST().gtk_application_remove_window(getCPointer(), toCPointerNotNull(window));
    }
    public void setAccelsForAction(@Nonnull ch.bailu.gtk.type.Str detailed_action_name, @Nonnull ch.bailu.gtk.type.Strs accels)  {

        JnaApplication.INST().gtk_application_set_accels_for_action(getCPointer(), toCPointerNotNull(detailed_action_name), toCPointerNotNull(accels));
    }
    public void setMenubar(@Nullable ch.bailu.gtk.gio.MenuModel menubar)  {

        JnaApplication.INST().gtk_application_set_menubar(getCPointer(), toCPointer(menubar));
    }
    public void uninhibit(int cookie)  {

        JnaApplication.INST().gtk_application_uninhibit(getCPointer(), cookie);
    }

    public void onQueryEnd(ch.bailu.gtk.gtk.Application.OnQueryEnd observer) {
        ch.bailu.gtk.Callback.put(getCPointer(), "query-end", observer);
        //JnaApplication.INST().onQueryEnd(getCPointer());
    }

    public interface OnQueryEnd {
        void onQueryEnd();
    }

    public void onWindowAdded(ch.bailu.gtk.gtk.Application.OnWindowAdded observer) {

        ch.bailu.gtk.Callback.put(getCPointer(), "window-added", observer);
        //JnaApplication.INST().onWindowAdded(getCPointer());
    }

    public interface OnWindowAdded {
        @Delegate
        void onWindowAdded(@Nonnull Window window);
    }

    public void onWindowRemoved(ch.bailu.gtk.gtk.Application.OnWindowRemoved observer) {
        ch.bailu.gtk.Callback.put(getCPointer(), "window-removed", observer);
        Gobject.signalConnectData(this, new Str("window-removed"), observer, null, null, 0);
        //JnaApplication.INST().onWindowRemoved(getCPointer());
    }

    public interface OnWindowRemoved {
        void onWindowRemoved(@Nonnull Window window);
    }

    public static long getTypeID() { return JnaApplication.INST().gtk_application_get_type(); }

}
