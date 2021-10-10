package ch.bailu.gtk.parser

import ch.bailu.gtk.builder.BuilderInterface
import ch.bailu.gtk.parser.tag.DocumentTag
import ch.bailu.gtk.parser.tag.Tag
import ch.bailu.gtk.config.NamespaceConfig
import ch.bailu.gtk.writer.getReader
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader

class Parser {

    constructor(namespaceConfig: NamespaceConfig, builder: BuilderInterface) {
        var reader: Reader? = null
        try {
            reader = getReader(namespaceConfig.getFile())
            parse(getParser(reader), DocumentTag(builder, namespaceConfig))
        } finally {
            reader?.close()
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
                tag = tag.getChild(parser.name, if (parser.prefix == null) "" else parser.prefix)
                for (i in 0 until parser.attributeCount) {
                    tag.setAttribute(parser.getAttributeName(i), parser.getAttributeValue(i))
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


}