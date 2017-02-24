package org.cidarlab.OwlPackager.Util;

import java.io.File;

import com.beust.jcommander.IStringConverter;

public class DirConverter implements IStringConverter<File>{

	@Override
	public File convert(String value) {
		
		File file = new File(value);
		file.mkdir();
		
		return file;
	}

}
