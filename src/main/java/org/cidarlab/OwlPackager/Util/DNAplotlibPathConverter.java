package org.cidarlab.OwlPackager.Util;

import java.io.File;

import com.beust.jcommander.IStringConverter;

public class DNAplotlibPathConverter implements IStringConverter<File>{

		@Override
		public File convert(String value) {
			
			File f = null;
			f = new File(value+Utilities.getFileDivider()+"quick.py");
			
			if(!f.exists()){
				System.err.println("ERROR. Incorrect path to the DNAplotlib directory containing quick.py script. No quick.py was found in: "+ value);
			}
			
			return f;
		}

}

