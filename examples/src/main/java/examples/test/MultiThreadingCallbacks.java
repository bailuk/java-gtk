package examples.test;

import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.ClassHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class MultiThreadingCallbacks implements DemoInterface {

    private int threadId = 1;
    private Button buttonTest;
    private boolean running = false;

    @Override
    public Window runDemo() {
        var window = new Window();

        var box = new Box(Orientation.VERTICAL, 0);

        buttonTest = new Button();
        buttonTest.onDestroy(buttonTest::disconnectSignals);
        box.append(buttonTest);
        var buttonRun = new Button();
        buttonRun.setLabel("RUN");
        buttonRun.onClicked( ()-> {
                var thread = new Thread(() -> {
                    try {
                        toMainThread();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.setName("Thread " + threadId);
                thread.start();
                threadId ++;
        } );
        buttonRun.onDestroy(buttonRun::disconnectSignals);
        box.append(buttonRun);

        var buttonClose = new Button();
        buttonClose.setLabel("Close");
        buttonClose.onClicked(window::close);
        buttonClose.onDestroy(buttonClose::disconnectSignals);
        box.append(buttonClose);

        var buttonDump = new Button();
        buttonDump.setLabel("Dump resources");
        buttonDump.onClicked(() -> {
            ActionHandler.dump(System.out);
            ClassHandler.dump(System.out);
            CallbackHandler.dump(System.out);
            SignalHandler.dump((System.out));
        });
        buttonDump.onDestroy(buttonDump::disconnectSignals);
        box.append(buttonDump);

        var buttonDisconnect = new Button();
        buttonDisconnect.setLabel("Disconnect signals");
        buttonDisconnect.onClicked(() -> buttonTest.disconnectSignals());
        buttonDisconnect.onDestroy(buttonDisconnect::disconnectSignals);
        box.append(buttonDisconnect);

        window.setChild(box);

        window.onDestroy(() -> running = false);
        running = true;
        return window;
    }

    @Override
    public Str getTitle() {
        return new Str("Multi Threading Callbacks");
    }

    @Override
    public Str getDescription() {
        return getTitle();
    }


    public void toMainThread() throws InterruptedException {
        for(int i=0; i  < 100; i++) {
            Thread.sleep(20);
            // Outside of main thread

            Glib.idleAdd((cb, user_data) -> {
                // Inside of main thread
                Str data = new Str(user_data.cast());

                if (running) {
                    String string = data.toString();
                    buttonTest.setLabel(data);
                    buttonTest.onClicked(() -> System.out.println(string));
                }

                data.destroy();
                cb.unregister();
                return false;
            }, new Str("[" + i + "]: " + Thread.currentThread().getName()));
        }
    }
}
