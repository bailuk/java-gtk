package ch.bailu.gtk.parser

import ch.bailu.gtk.Directories
import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.builder.BuilderInterface
import ch.bailu.gtk.parser.tag.DocumentTag
import ch.bailu.gtk.parser.tag.Tag
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader

class Parser(directories: Directories, namespaceConfig: NamespaceConfig, builder: BuilderInterface) {

    init {
        directories.getGirReader(namespaceConfig.girFile).use {
            parse(getParser(it), DocumentTag(builder, namespaceConfig))
        }
    }

    @Throws(XmlPullParserException::class)
    private fun getParser(reader: Reader): XmlPullParser {
        val result = getFactory().newPullParser()
        result.setInput(reader)
        return result
    }

    @Throws(XmlPullParserException::class)
    private fun getFactory(): XmlPullParserFactory {
        val result = XmlPullParserFactory.newInstance()
        result.isNamespaceAware = true
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parse(parser: XmlPullParser, rootTag: Tag) {
        var tag = rootTag
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                tag = tag.getChild(toPrefixed(parser.name, parser.prefix))
                for (i in 0 until parser.attributeCount) {
                    tag.setAttribute(toPrefixed(parser.getAttributeName(i), parser.getAttributePrefix(i)), parser.getAttributeValue(i))
                }
                tag.started()
            } else if (parser.eventType == XmlPullParser.END_TAG) {
                tag.end()
                tag = tag.getParent()
            } else if (parser.eventType == XmlPullParser.TEXT) {
                tag.setText(parser.text)
            }
            parser.next()
        }
    }


    private fun toPrefixed(name: String, prefix: String?) : String {
        if (prefix == null) {
            return name
        }
        return "$prefix:$name"
    }
}
