package org.cidarlab.OwlPackager.adaptors;

import java.util.Arrays;
import java.util.regex.Pattern;

public class DeviceSplitter {
	
	/**
	* This method takes String representation of the Eugene output Array[] and splits this string
	* into String[] sections.
	* 
	* @param eugeneOutputArray ... Eugene output Array[] as a String
	*/
	public final static String[] splitArrayIntoDevices(String eugeneOutputArray){
	String regExp = "Device ";
	
	Pattern pattern = Pattern.compile(regExp, Pattern.LITERAL);
	
	String[] dSections = pattern.split(eugeneOutputArray);
	
	return Arrays.copyOfRange(dSections, 1, dSections.length);
	}
	
}
