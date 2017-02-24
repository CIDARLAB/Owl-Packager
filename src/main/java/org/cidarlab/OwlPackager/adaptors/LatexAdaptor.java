package org.cidarlab.OwlPackager.adaptors;

import java.util.regex.Matcher;

import org.apache.commons.lang3.text.WordUtils;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.dom.Datasheet;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;

public class LatexAdaptor {

	public static String generateLatexCode(Datasheet project){
        String latex = "";
        latex += "\\documentclass[12pt]{article}\n"
                + "\\usepackage{fullpage}\n"
                + "\\usepackage{hyperref}\n"
                + "\\usepackage{mathtools}\n"
                + "\\usepackage[usenames, dvipsnames]{color}\n"
                + "\\usepackage{adjustbox}\n"
                + "\\usepackage[section]{placeins}\n";
        String newProjectName = project.getProject().replaceAll("_", Matcher.quoteReplacement("\\_"));
        
        latex += "\\title{Datasheet summary - Project " + newProjectName + "}\n";
        
        latex += "\\begin{document}\n"
                + "\\maketitle\n"
                + "\\section{Devices}\n"
                + "\\begin{itemize}\n";

        for(String deviceName:project.getPigeonFilepath().keySet()){
            String newDeviceName = deviceName.replaceAll("_", Matcher.quoteReplacement("\\_"));
            latex += "\\item " + newDeviceName + "\\\\\n";
            latex += "Length of the device (bp): " + String.format("%,d", project.getDeviceLengths().get(deviceName)) + "\\\\\n";
            latex += "\\%GC = " + project.getGcContents().get(deviceName) + "\\\\\n";
            latex += "Composition (5' to 3'): " + project.getDeviceCompositions().get(deviceName) + "\\\\\n";
            latex += getImageMinibox(project.getPigeonFilepath().get(deviceName));
        }
        
        latex += "\\end{itemize}\n"
                + "\\end{document}";
        
        return latex;
    }
    
    private static String getImageMinibox(String filepath){
        String latex = "";
        latex += "\\begin{minipage}[t]{\\linewidth}\n"
                + "          \\adjustbox{valign=t}{\n";
        if(Utilities.isWindows()){
        	String newFilePath = filepath.replaceAll("\\\\","/");
        	latex += "           \\includegraphics[width=\\linewidth]{" + newFilePath + "}\n";
        }else {
        	latex += "           \\includegraphics[width=\\linewidth]{" + filepath + "}\n";
        }
        latex += "          }\n"
                + "          \\end{minipage}\n";
        return latex;
    }
    
    public static String getDeviceComponents(GeneticConstruct device){
    	String deviceComp = "";
        
    	for(Part part: device.getPartList()){
    		deviceComp += part.getPartProperties().getName() + " ; ";
    	}
    	
    	return WordUtils.wrap(deviceComp.replaceAll("_", "\\\\_"), 70, "\n", true);
    }
    
    public static String makeTexFile(Datasheet datasheet){
        Utilities.writeToFile(datasheet.getPathToTexFile().getAbsolutePath(), generateLatexCode(datasheet));
        return datasheet.getPathToTexFile().getAbsolutePath();
    }
	
}
