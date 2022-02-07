package com.messanger.engine.uc.utils;

import java.io.Writer;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class LocalXppDriver extends XppDriver {
	public HierarchicalStreamWriter createWriter(Writer out) {
		return new LocalPrettyPrintWriter(out);
	}

}
