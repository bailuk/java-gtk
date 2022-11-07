package examples.test;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.Refs;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Button.OnClicked;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class MultiThreadingCallbacks implements DemoInterface {

    private int threadId = 1;
    private Button buttonTest;

    @Override
    public Window runDemo() {
        var window = new Window();

        var box = new Box(Orientation.VERTICAL, 0);

        var buttonRun = new Button();
        buttonRun.setLabel(new Str("RUN"));
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
        box.append(buttonRun);

        var buttonClose = new Button();
        buttonClose.setLabel(new Str("Close"));
        buttonClose.onClicked(window::close);
        box.append(buttonClose);

        buttonTest = new Button();
        buttonTest.setLabel(new Str("Test"));
        box.append(buttonTest);

        final var buttonOnClickedTest = new Button();
        buttonOnClickedTest.setLabel(new Str("Click me"));

        final Button.OnClicked onClicked = () -> System.out.println("onClicked");
        buttonOnClickedTest.onClicked(onClicked);

        box.append(buttonOnClickedTest);

        final var buttonAddCallback = new Button();
        buttonAddCallback.setLabel(new Str("Add Click handler"));
        buttonAddCallback.onClicked(()->buttonOnClickedTest.onClicked(onClicked));

        // TODO Refs.remove();
        box.append(buttonAddCallback);
        window.setChild(box);


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
        for(int i=0; i <10000; i++) {
            Thread.sleep(20);
            Glib.idleAdd(onSourceFunc, new Str("[" + i + "]: " + Thread.currentThread().getName()));
        }
    }

    private class MyOnClicked implements OnClicked {
        private final String label;

        public MyOnClicked(String label) {
            this.label = label;
        }

        @Override
        public void onClicked() {
            System.out.println(label);
        }
    }

    private MyOnClicked onClicked = new MyOnClicked("");


    private Glib.OnSourceFunc onSourceFunc = user_data -> {
        Str data = new Str(user_data.cast());
        buttonTest.setLabel(data);
        MyOnClicked old = onClicked;
        onClicked = new MyOnClicked(data.toString());
        buttonTest.onClicked(onClicked);
        data.destroy();

        Refs.remove(old);
        return GTK.FALSE;
    };
}
