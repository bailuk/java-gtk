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
        for (String girFile : Configuration.GIR_FILES) {
            File file = getExistingFile(girFile);
            new Parser(file, builder);
        }
    }

    private static File getExistingFile(String girFile) throws IOException {
        File result = new File(Configuration.GIR_DIR_CUSTOM,girFile);
        String type = "custom";

        if (!result.exists()) {
            result = new File(Configuration.instance().getGirBaseDir(),girFile);
            type = "system";
            if (!result.exists()) {
                result = new File(Configuration.GIR_DIR_LOCAL, girFile);
                type = "local";
                if (!result.exists()) {
                    throw new IOException("File does not exist " + result);
                }
            }
        }

        System.out.println(type + ": " + result);
        return result;
    }

}
