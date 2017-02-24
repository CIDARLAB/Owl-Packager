package org.cidarlab.OwlPackager.dom;

import java.io.File;
import java.util.Map;

import org.cidarlab.OwlPackager.Util.Utilities;

import lombok.Getter;

public class Datasheet {
	
	@Getter private String project;
    @Getter private Map<String,String> pigeonFilepath;
    @Getter private String outputFolder;
    @Getter private File pathToTexFile;
    @Getter private Map<String, String> deviceCompositions;
    @Getter private Map<String, String> gcContents;
    @Getter private Map<String, Integer> deviceLengths;
    
	public Datasheet(String project, Map<String, String> pigeonFilepath, String outputFolder,
			Map<String, String> deviceCompositions, Map<String, String> gcContents,
			Map<String, Integer> deviceLengths) {
		this.project = project;
		this.pigeonFilepath = pigeonFilepath;
		this.outputFolder = outputFolder;
		this.pathToTexFile = new File(outputFolder + Utilities.getFileDivider()+project+".tex");
		this.deviceCompositions = deviceCompositions;
		this.gcContents = gcContents;
		this.deviceLengths = deviceLengths;
	}
	
}
