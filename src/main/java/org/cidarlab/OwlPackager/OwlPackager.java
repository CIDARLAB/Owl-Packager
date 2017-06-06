package org.cidarlab.OwlPackager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.biojava3.core.sequence.DNASequence;
import org.cidarlab.OwlPackager.Util.CmdLineParser;
import org.cidarlab.OwlPackager.Util.ShellExec;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.Util.ZipFolder;
import org.cidarlab.OwlPackager.adaptors.DNAplotlibClient;
import org.cidarlab.OwlPackager.adaptors.DeviceSplitter;
import org.cidarlab.OwlPackager.adaptors.FastaFileFactory;
import org.cidarlab.OwlPackager.adaptors.GenBankExporter;
import org.cidarlab.OwlPackager.adaptors.GeneticConstructFactory;
import org.cidarlab.OwlPackager.adaptors.LatexAdaptor;
import org.cidarlab.OwlPackager.adaptors.PigeonClient;
import org.cidarlab.OwlPackager.adaptors.RegExpDevice;
import org.cidarlab.OwlPackager.dom.Datasheet;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;

import com.beust.jcommander.JCommander;

/**
 * @author Yury V. Ivanov, Ph.D. (Broad Institute and Boston University)
*/
public class OwlPackager {

	public static void main(String[] args) throws Exception {
		
		//configuration for errors
		PrintStream console = System.err;
        System.setErr(console);
		
        File errors = null;
        for(int i = 0; i < args.length; i++){
        	if(args[i].matches("^-e$")){
        		errors = new File(args[i+1]);
        	}
        }
        
        if(errors != null){
        	PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(errors)), true);
        	System.setErr(ps);
        }
        
        // parsing command line arguments
    	CmdLineParser cmd = new CmdLineParser();
    	JCommander jc = null;
    	try{	
    		jc = new JCommander(cmd, args);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	jc.setProgramName("OwlPackager");
    	
    	// check for -help argument
    	if(cmd.isHelp()){
    		System.out.println("\n===========================================");
        	jc.usage();
        	System.out.println("===========================================");
        	System.exit(0);
    	}
    	
    	// check for input
    	if(cmd.getEugeneFile() == null){
    		System.err.println("\nERROR:\nNo input file was provided");
    		System.out.println("\n===========================================");
    		jc.usage();
    		System.out.println("\n===========================================");
    		System.exit(1);
    	}
    	
    	
		// opens a file and extracts filename into String project, if -project argument was not provided.
        String project = null;
    	if(cmd.getProjectName() == null){
    		String fname = cmd.getEugeneFile().getName();
    		int pos = fname.lastIndexOf(".");
    		if (pos > 0) {
    		    fname = fname.substring(0, pos);
    		}
    		project = fname;
    	} else {
    		project = cmd.getProjectName();
    	}
    		
		String eugeneFile = Utilities.readEugeneOutput(cmd.getEugeneFile());
		
		if(!eugeneFile.startsWith("The total number")){
			System.err.println("ERROR: Packager received a non-compatible file: " + cmd.getEugeneFile().getCanonicalPath() + ".");
			System.exit(1);
		}
		
		File outputDir = null;
		if(cmd.getOutput() != null){
    		outputDir = new File(cmd.getOutput().getPath()+Utilities.getFileDivider()+project);
    		outputDir.mkdir();
    	} else {
    		outputDir = new File(System.getProperty("user.dir") + Utilities.getFileDivider() + project);
    		outputDir.mkdir();	
    	}
		
		Map<String,String> pigeonMap = new LinkedHashMap<>();
		Map<String, String> gcContentMap = new HashMap<>();
        Map<String, Integer> deviceLengths = new HashMap<>();	        
        Map<String, String> deviceCompositions = new HashMap<>();
        
        
        //get number of the assembled devices from the Eugene output Array[] file.
        long deviceNumber = 0;
        boolean deviceLimit = false;
		deviceNumber = RegExpDevice.getDeviceNumber(eugeneFile);
        int limit = (int)deviceNumber;
		if(deviceNumber > 50){
			deviceLimit = true;
			limit = 50;
		}
		
		if(deviceLimit){
			System.err.println("WARNING: The total number of assembled Genetic Constructs: " + deviceNumber + ". Only first " + limit +" constructs have been processed."
								+ "Please reduce the number of parts that you supply");
		}
		
		String[] dSections = DeviceSplitter.splitArrayIntoDevices(eugeneFile);
		
		String[] newSections = Arrays.copyOf(dSections, limit);

		for(String s: newSections){
			
			// the first element contains device name; the second element contains device contents (all information about the parts and properties)
			String[] mySplit = s.trim().split("\\(", 2);
			
			// sends deviceContents to GeneticConstructFactory and receives assembled GeneticConstruct
			GeneticConstruct gc = GeneticConstructFactory.assembleGeneticConstruct(mySplit[0], mySplit[1]);
			//System.out.println(gc.toString());
			
			if(cmd.isWithPigeon()){
				// generate Pigeon Specification; send POST requests to Pigeon service
				pigeonMap.put(gc.getName(), PigeonClient.generatePigeonScript(gc));
			} else {
				// generate DNAplotlib specification
				pigeonMap.put(gc.getName(), DNAplotlibClient.generateDNAplotlibParams(gc));
			}
			
			// generate FASTA files
	     	FastaFileFactory.createDeviceFastaFile(gc, outputDir);
	     	
	     	// generate GenBank files
	     	String genBankContents = GenBankExporter.writeGenBank(gc, project);
	     	Utilities.writeToFile(outputDir.getPath()+ Utilities.getFileDivider()+gc.getName()+".gbk", genBankContents);
			
	        // calculate GC-content:
            gcContentMap.put(gc.getName(), getGcPercent(gc));
	     	
            // device length
            deviceLengths.put(gc.getName(), gc.getSequence().length());
            
            //get device components for PDF datasheet
            deviceCompositions.put(gc.getName(), LatexAdaptor.getDeviceComponents(gc));
			
		}
		
		
		
		Map<String,String> images = new LinkedHashMap<String, String>();
		if(cmd.isWithPigeon()){
			// upload Pigeon images and save them in the project folder
			//System.out.println("withPigeon is "+cmd.isWithPigeon());
			images = PigeonClient.generateFile(pigeonMap, outputDir, limit);
		} else {
			//generate DNAplotlib images
			for(String key: pigeonMap.keySet()){
				ShellExec shell = new ShellExec(true, false);
				try {
					int exitCode = shell.execute("python", outputDir.getAbsolutePath(), true, cmd.getDnaplotlib().getAbsolutePath(), "-input", pigeonMap.get(key), "-output", key+".png");
					if(exitCode == 0){
		            	// read png file and save its path to the map Device_name : Image_path.
						File f = new File(outputDir.getAbsolutePath()+Utilities.getFileDivider()+key+".png");
						images.put(key, f.getAbsolutePath());
					} else {
						System.err.println("PDF-LaTeX returned exit code = " + exitCode);
						System.exit(1);
					}
				} catch (IOException e) {
	            System.err.println("ERROR. DNAplotlib has failed. Reason: " + e.getMessage());
				}
			}
		}
		
		// Generate PDF Datasheet
		Datasheet datasheet = new Datasheet(project, images, outputDir.getPath(), deviceCompositions, gcContentMap, deviceLengths);
		/*String texFile = */LatexAdaptor.makeTexFile(datasheet);

		ShellExec exec = new ShellExec(true, false);
        try {
            int exitCode = exec.execute(cmd.getLatex().getAbsolutePath(), outputDir.getAbsolutePath(), true, datasheet.getPathToTexFile().getAbsolutePath());
            if(exitCode == 0){
            	//System.out.println(project+".pdf file was successfully generated");
            } else {
            	System.err.println("PDF-LaTeX returned exit code = " + exitCode);
            	System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("ERROR. PDFlatex failed. Reason: " + e.getMessage());
        }
        /*System.out.println(exec.getOutput());*/
        
        Utilities.removeFile(datasheet.getOutputFolder()+Utilities.getFileDivider()+project+".aux");
        Utilities.removeFile(datasheet.getOutputFolder()+Utilities.getFileDivider()+project+".log");
        Utilities.removeFile(datasheet.getOutputFolder()+Utilities.getFileDivider()+project+".out");
        Utilities.removeFile(datasheet.getOutputFolder()+Utilities.getFileDivider()+project+".tex");
		
		// Package the project folder
	    File zipFile = new File(outputDir.getPath()+".zip");
		ZipFolder.zipDirectory(outputDir, zipFile);
		Utilities.deleteFolder(outputDir);
	}
	
	public static String getGcPercent(GeneticConstruct construct){
		DNASequence dna = new DNASequence(construct.getSequence());
        String gcContent = String.format("%.2f", dna.getGCCount()*100.00/dna.getLength());
        
		return gcContent; 
	}
}
