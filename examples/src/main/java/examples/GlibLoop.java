package examples;

import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.GlibConstants;
import ch.bailu.gtk.glib.MainLoop;

public class GlibLoop {
    private final MainLoop loop;
    static int timers = 0;

    public GlibLoop() {
        loop = new MainLoop(Glib.mainContextDefault(), false);
        new Timer("Timer one", 777);
        new Timer("Timer two", 1024);
        loop.run();
    }

    private class Timer {
        private int count = 10;

        public Timer(String name, int interval) {
            int result = Glib.timeoutAdd(interval, (cb, user_data) -> {
                if (count > 0) {
                    System.out.println(name + ": timeout " + count-- + " received");
                    return GlibConstants.SOURCE_CONTINUE;
                } else {
                    System.out.println(name + ": done");
                    return remove();
                }
            }, null);

            if (result > 0) timers++;
        }

        public boolean remove() {
            if (--timers == 0) {
                loop.quit();
            }
            return GlibConstants.SOURCE_REMOVE;
        }
    }
}
