package org.cidarlab.OwlPackager.adaptors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpDevice {

	public static long getDeviceNumber(String eugeneFile){
		long devices = 0;
		String regExp = "(The total number of devices: )(\\d+)(.*)";
		Pattern pattern = Pattern.compile(regExp);
		
		Matcher m = pattern.matcher(eugeneFile);
		
		if(m.find()){
			devices = Long.parseLong(m.group(2));
		} else {
			System.err.println("No information about how many constructs produced. Please check Eugene-output file");
		}
		
		return devices;
	}
	
}
