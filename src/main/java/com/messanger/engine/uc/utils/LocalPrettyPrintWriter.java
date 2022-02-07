package com.messanger.engine.uc.utils;

import java.io.Writer;
import java.util.regex.Pattern;

import com.messanger.engine.uc.Constants;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

public class LocalPrettyPrintWriter  extends PrettyPrintWriter {
    private static final char[] AMP = "&amp;".toCharArray();
	private static final char[] LT = "<".toCharArray();
	private static final char[] GT = ">".toCharArray();
	private static final char[] SLASH_R = " ".toCharArray();
	private static final char[] QUOT = "&quot;".toCharArray();
	private static final char[] APOS = "&apos;".toCharArray();

   
    public LocalPrettyPrintWriter (Writer writer) {
        super(writer);
    }

    protected void writeText(QuickWriter writer, String text) {       
       
        String CDATAPrefix = "<![CDATA[";
        String CDATASuffix = "]]>";

        if (!text.startsWith(CDATAPrefix) && !Pattern.matches("[^[0-9]]+", text)) {
        	text.replace(Constants.COL_DELIM, "");
        	text.replace(Constants.PROP_DELIM, "");
            text = CDATAPrefix+text+CDATASuffix;
        }
       
        int length = text.length();
        if (!text.startsWith(CDATAPrefix)) {
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                switch (c) {
                case '&':
                    writer.write(AMP);
                    break;
                case '<':
                    writer.write(LT);
                    break;
                case '>':
                    writer.write(GT);
                    break;
                case '"':
                    writer.write(QUOT);
                    break;
                case '\'':
                    writer.write(APOS);
                    break;
                case '\r':
                    writer.write(SLASH_R);
                    break;
                default:
                    writer.write(c);
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                writer.write(c);
            }
        }
    }
}

