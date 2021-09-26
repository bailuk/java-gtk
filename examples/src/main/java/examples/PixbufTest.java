package examples;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gdkpixbuf.PixbufFormat;

public class PixbufTest {

    public PixbufTest() {
        var list = Pixbuf.getFormats();

        int count = 1;
        while(list.getCPointer() != 0 && list.getFieldData() != 0) {
            var format = new PixbufFormat(list.getFieldData());

            System.out.println("");
            System.out.println("Format " + count + ":");
            System.out.println(format.getName());
            System.out.println(format.getDescription());
            if (format.isDisabled() == GTK.TRUE) {
                System.out.println("disabled");
            }

            if (format.isScalable() == GTK.TRUE) {
                System.out.println("scalable");
            }

            if (format.isWritable() == GTK.TRUE) {
                System.out.println("writeable");
            }

            list = list.getFieldNext();
            count ++;
        }
    }

}
