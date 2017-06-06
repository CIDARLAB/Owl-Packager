package org.cidarlab.OwlPackager.adaptors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartProperty;
import org.cidarlab.OwlPackager.dom.PartType;

public class GeneticConstructFactory {	
	/**
	 * This method takes String representation of the device contents from Eugene Array[] output
	 * and returns assembled GeneticConstruct.
	 * 
	 * @param deviceName ... String name of the device.
	 * @param deviceContents ... String representation of device contents in the Array[].
	*/
	public static final GeneticConstruct assembleGeneticConstruct(String deviceName, String deviceContents){
		
		List<Part> partList = new ArrayList<>();
		
		//split device contents, from Eugene Array text file, by component per line.
		String[] componentsSplit = deviceContents.trim().split("(?=[\\+-])");
		for(String s: componentsSplit){
			
			Pattern pattern = Pattern.compile("^\\s*[\\+-]?(Promoter|RBS|Ribozyme|CDS|Terminator)\\s{1}(\\w+)\\s{1}\\(\\.SEQUENCE\\(\\\"(\\w+)\\\"\\),\\.name\\(\\\"(.+)\\\"\\),\\.PIGEON.+");
			Matcher matcher = pattern.matcher(s);
			
			while (matcher.find()) {
				PartProperty properties = new PartProperty(matcher.group(4), matcher.group(3));
				Part part = new Part(matcher.group(2), findPartType(matcher.group(1)), findOrientation(s), properties);
				partList.add(part);
			}
			
		}
		// instantiates and returns the GeneticConstruct using device name and list of parts.
		return new GeneticConstruct(deviceName, partList);
	}
	
	/**
	* This private method checks if a String, containing Eugene part, starts with "+" or "-"
	* and returns false for reverse part and true for forward part.
	* 
	* @param lineWithPart ... file line containing a Part, e.g., "+Promoter p1 (.SEQUENCE("ATGC")..."
	*/
	private static final boolean findOrientation(String lineWithPart) {
		if(lineWithPart.startsWith("+")){
			return false;
		} else if (lineWithPart.startsWith("-")){
			return true;
		} else {
			throw new IllegalArgumentException("Unknown part orientation: " +lineWithPart);
		}
	}
	
	/**
	* This private method checks if a provided partType String, is either Promoter, RBS, Ribozyme, CDS
	* or Terminator and returns corresponding enum PartType.
	* 
	* @param partTypeString ... parsed String from this line (example): "+Promoter p1 (.SEQUENCE("ATGC")..."
	*/
	private static final PartType findPartType(String partTypeString) {
			
			if(partTypeString.contains("Promoter")){
				return PartType.PROMOTER;
			} else if (partTypeString.contains("RBS")){
				return PartType.RBS;
			} else if (partTypeString.contains("Ribozyme")){
				return PartType.RIBOZYME;
			} else if (partTypeString.contains("CDS")){
				return PartType.CDS;
			} else if (partTypeString.contains("Terminator")){
				return PartType.TERMINATOR;
			} else {
				throw new IllegalArgumentException("Unknown part type detected: "+ partTypeString);
			}
	}
		
}
