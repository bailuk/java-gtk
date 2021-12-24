package examples;

import java.util.Objects;

import ch.bailu.gtk.Callback;
import ch.bailu.gtk.GTK;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.GlibConstants;
import ch.bailu.gtk.glib.MainLoop;

public class GlibLoop {
    private final MainLoop loop;
    static int timers = 0;

    public GlibLoop() {
        loop = new MainLoop(Glib.mainContextDefault(), GTK.FALSE);
        new Timer("Timer one", 777);
        new Timer("Timer two", 1024);
        loop.run();
    }

    private class Timer {
        private final Callback.EmitterID emitterID = new Callback.EmitterID();
        private int count = 10;

        public Timer(String name, int interval) {
            int result = Glib.timeoutAdd(interval, user_data -> {

                if (Objects.equals(user_data, emitterID)) {
                    if (count > 0) {
                        System.out.println(name + ": timeout " + count-- + " received");
                        return GlibConstants.SOURCE_CONTINUE;
                    } else {
                        System.out.println(name + ": done");
                        return remove();
                    }

                } else {
                    System.err.println(name + ": emitterID does not equal user_data");
                    return remove();
                }
            }, emitterID);

            if (result > 0) timers ++;
        }

        public int remove() {
            Callback.remove(emitterID);
            if (--timers == 0) {
                loop.quit();
            }
            return GlibConstants.SOURCE_REMOVE;
        }
    }
}
