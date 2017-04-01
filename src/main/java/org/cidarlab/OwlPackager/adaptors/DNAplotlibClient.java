package org.cidarlab.OwlPackager.adaptors;

import java.util.HashMap;
import java.util.Map;

import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartType;

import lombok.Getter;

public class DNAplotlibClient {
	
	@Getter private static Map<String, Integer> colors = new HashMap<>();
	
	public static String generateDNAplotlibParams(GeneticConstruct device) {
        String script = "\"";
        
        for(Part part: device.getPartList()){
            	
                if (part.getPartType()==PartType.PROMOTER) {
                    script += "p." + getColorWord(getColor(part.getPartProperties().getName())) + " ";
                } else if (part.getPartType()==PartType.RBS) {
                    script += "r." + getColorWord(getColor(part.getPartProperties().getName())) + " ";
                } else if (part.getPartType()==PartType.RIBOZYME) {
                    script += "i." + getColorWord(getColor(part.getPartProperties().getName())) + " ";
                } else if (part.getPartType()==PartType.CDS) {
                    script += "c." + getColorWord(getColor(part.getPartProperties().getName())) + " ";
                } else if (part.getPartType()==PartType.TERMINATOR) {
                    script += "t." + getColorWord(getColor(part.getPartProperties().getName())) + " ";
                } else {
                    System.err.println("Urecognized part type: " + part);
                    System.exit(1);
                }
                
            }
            script = script.trim();
        	script += "\"";
        
		return script;
	}
	
	 /**
		 * This private method gives a new color for a new Part or a corresponding color or the existing Part.
		 * 
		 * @param s  ... the name of a Part
		 * @return   int color ... the color code of the Part
		 */
		private static int getColor(String s) {
			if(colors.containsKey(s)) {
				int color = colors.get(s);
				if(color <= 1) {
					return 1;
				} else if(color >= 14) {
					return 14;
				}
				return color;
			}

			/*
			 * otherwise, we put the name into the coloring map
			 */
			int color = getRandomColor();
			colors.put(s, color);
			return color;
		}
		
		private static final int COLOR_MIN = 1;
		private static final int COLOR_MAX = 14;
		
		private static int getRandomColor() {
			return COLOR_MIN + (int)(Math.random() * ((COLOR_MAX - COLOR_MIN) + 1));
		}
	
		private static String getColorWord(Integer colorFormMap){
			String word = "";
			
		    final Map<Integer, String> intToColor = new HashMap<>();
		    intToColor.put(1, "black");
		    intToColor.put(2, "grey");
		    intToColor.put(3, "red");
		    intToColor.put(4, "orange");
		    intToColor.put(5, "yellow");
		    intToColor.put(6, "green");
		    intToColor.put(7, "blue");
		    intToColor.put(8, "purple");
		    intToColor.put(9, "lightred");
		    intToColor.put(10, "lightorange");
		    intToColor.put(11, "lightyellow");
		    intToColor.put(12, "lightgreen");
		    intToColor.put(13, "lightblue");
		    intToColor.put(14, "lightpurple");
		    
		    word = intToColor.get(colorFormMap);
		    
		    return word;
		}
}
