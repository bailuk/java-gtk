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
import ch.bailu.gtk.table.AliasTable;
import ch.bailu.gtk.log.Logable;
import ch.bailu.gtk.table.CallbackTable;
import ch.bailu.gtk.table.StructureTable;

public class App {

    public static void main(String[] args) {
        try {
            Configuration.init(args);
            System.out.println("1. fill tables");
            parse(new AliasBuilder());

            System.out.println("2. log tables");
            logTables();

            System.out.println("3. build model and write code files");
            parse(new ModelBuilder());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logTables() throws IOException {
        logTable(StructureTable.INSTANCE, Configuration.LOG_STRUCTURE_TABLE_FILE);
        logTable(AliasTable.INSTANCE, Configuration.LOG_ALIAS_TABLE_FILE);
        logTable(CallbackTable.INSTANCE, Configuration.LOG_CALLBACK_TABLE_FILE);
    }

    private static void logTable(Logable logable, String file) throws IOException {
        System.out.println(file);
        Writer out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            logable.log(out);
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
