package org.cidarlab.OwlPackager.Util;

import java.io.File;

import com.beust.jcommander.IStringConverter;

public class LatexPathConverter implements IStringConverter<File>{

	@Override
	public File convert(String value) {
		
		File f = null;
		if(Utilities.isWindows()){
			f = new File(value+Utilities.getFileDivider()+"pdflatex.exe");	
		} else {
			f = new File(value+Utilities.getFileDivider()+"pdflatex");	
		}
		
		if(!f.exists()){
			System.err.println("ERROR. Incorrect path to the latex directory. No pdflatex was found in: "+ value);
		}
		
		return f;
	}

}
