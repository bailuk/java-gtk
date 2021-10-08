package ch.bailu.gtk;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.xmlpull.v1.XmlPullParserException;

import ch.bailu.gtk.builder.AliasBuilder;
import ch.bailu.gtk.builder.BuilderInterface;
import ch.bailu.gtk.builder.ModelBuilder;
import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.NamespaceModel;
import ch.bailu.gtk.table.StructureTable;
import ch.bailu.gtk.writer.CWriter;
import ch.bailu.gtk.writer.IO;

public class App {

    public static void main(String[] args) {
        try {
            Configuration.init(args);

            System.out.println("generate sources");

            parse(new AliasBuilder());
            logTables();
            parse(new ModelBuilder());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logTables() throws IOException {
        Writer out = null;
        try {
            out = new BufferedWriter(new FileWriter(Configuration.LOG_CLASSES));
            StructureTable.INSTANCE.log(out);
        } finally {
            IO.close(out);
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
