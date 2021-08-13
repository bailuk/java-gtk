package ch.bailu.gtk;


import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import ch.bailu.gtk.builder.AliasBuilder;
import ch.bailu.gtk.builder.BuilderInterface;
import ch.bailu.gtk.builder.ModelBuilder;

public class App {

    public static void main(String[] args) {
        try {
            Configuration.init(args);

            System.out.println("generate sources");

            parse(new AliasBuilder());
            parse(new ModelBuilder());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void parse(BuilderInterface builder) throws IOException, XmlPullParserException {
        for (String[] girFile : Configuration.GIR_FILES) {
            new Parser(new File(Configuration.instance().getGirBaseDir(),girFile[1]), builder);
        }
    }

}
