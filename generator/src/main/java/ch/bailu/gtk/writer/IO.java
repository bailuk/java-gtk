package ch.bailu.gtk.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.NamespaceModel;

public class IO {

    public static Writer getJavaWriter(String className, NamespaceModel nameSpaceModel) throws IOException {
        return new BufferedWriter(new FileWriter(getJavaFile(className, nameSpaceModel)));
    }

    private static File getJavaFile(String className, NamespaceModel nameSpaceModel) throws IOException {
        return new File(createDirectory(nameSpaceModel.getJavaSourceDirectory()), className + ".java");
    }

    private static File createDirectory(File directory) throws IOException {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Could not create directory:" + directory.getAbsolutePath());
        }
        return directory;
    }

    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static Reader getReader(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    public static Writer getCWriter(ClassModel model, NamespaceModel namespace) throws IOException {
        return new BufferedWriter(new FileWriter(getCFile(model.getImpName(), namespace)));
    }

    private static File getCFile(String name, NamespaceModel namespace) throws IOException {
        StringBuilder fn = new StringBuilder();
        fn.append(Configuration.HEADER_FILE_BASE).append(namespace.getNamespace()).append("_").append(name).append(".c");

        return new File(createDirectory(namespace.getCSourceDirectory()), fn.toString());
    }

    public static Writer getJavaImpWriter(ClassModel classModel, NamespaceModel namespace) throws IOException {
        return new BufferedWriter(new FileWriter(getJavaFile(classModel.getImpName(), namespace)));
    }
}

