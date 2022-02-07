package com.messanger.engine.uc.utils;

import java.io.File;
import java.io.FilenameFilter;

public class PrefixFilenameFilter implements FilenameFilter {

	private String prefix = "";
	
	public PrefixFilenameFilter(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public boolean accept(File dir, String name) {
		if(name.startsWith(prefix)) {
			return true;
		}
		return false;
	}
}
