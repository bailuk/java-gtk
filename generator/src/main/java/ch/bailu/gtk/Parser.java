package ch.bailu.gtk;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import ch.bailu.gtk.builder.BuilderInterface;
import ch.bailu.gtk.tag.DocumentTag;
import ch.bailu.gtk.tag.Tag;
import ch.bailu.gtk.writer.IO;

public class Parser {

    public Parser(File file, BuilderInterface builder) throws IOException, XmlPullParserException {
        Reader reader = null;

        try {
            reader = IO.getReader(file);
            parse(getParser(reader), new DocumentTag(builder));

        } finally {
            IO.close(reader);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private XmlPullParser getParser(Reader reader) throws XmlPullParserException {
        XmlPullParser result = getFactory().newPullParser();
        result.setInput(reader);
        return result;
    }

    private XmlPullParserFactory getFactory() throws XmlPullParserException {
        XmlPullParserFactory result = XmlPullParserFactory.newInstance();
        result.setNamespaceAware(true);
        return result;
    }

    private void parse(XmlPullParser parser, Tag tag) throws XmlPullParserException, IOException {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                tag = tag.getChild(parser.getName(), parser.getPrefix());

                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    tag.setAttribute(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
                tag.started();

            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                tag.end();
                tag = tag.getParent();

            } else if (parser.getEventType() == XmlPullParser.TEXT) {
                tag.setText(parser.getText());
            }


            parser.next();
        }
    }
}