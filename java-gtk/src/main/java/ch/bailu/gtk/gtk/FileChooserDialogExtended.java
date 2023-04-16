package ch.bailu.gtk.gtk;

import javax.annotation.Nullable;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.type.Str;

public class FileChooserDialogExtended extends FileChooserDialog {

    public static DialogButton button(String name, int id) {
        return new DialogButton(name, id);
    }

    public static class DialogButton {
        public final String name;
        public final int id;

        public DialogButton(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public static String getFirstButtonText(DialogButton[] buttons) {
            if (buttons.length > 0) {
                return buttons[0].name;
            }
            return "";
        }

        public static Object[] getRemainingArguments(DialogButton[] buttons) {
            var result = new Object[buttons.length * 2];

            if (buttons.length == 0) {
                return new Object[] {(Long) 0L};
            }

            int b = 0;
            int r = 0;
            result[r++] = buttons[b++].id;

            while (b < buttons.length) {
                result[r++] = buttons[b].name;
                result[r++] = buttons[b].id;
                b++;
            }
            result[r] = (Long) 0L;
            return result;
        }
    }

    public FileChooserDialogExtended(String title, @Nullable Window parent, int action, DialogButton... buttons) {
        super(title, parent, action, DialogButton.getFirstButtonText(buttons), DialogButton.getRemainingArguments(buttons));
    }


    public void setPath(String initialPath) {
        try {
            var str = new Str(initialPath);
            var file = File.newForPath(str);
            new FileChooser(cast()).setFile(file);
            unref(file);
            str.destroy();
        } catch (Exception e) {
            System.err.println("Failed to set initial path: " + initialPath);
        }
    }

    public String getPath() {
        var file = new FileChooser(cast()).getFile();
        var path = file.getPath();
        var result = path.toString();

        unref(file);
        path.destroy();
        return result;
    }

    private static void unref(File file) {
        new ch.bailu.gtk.gobject.Object(file.cast()).unref();
    }
}
