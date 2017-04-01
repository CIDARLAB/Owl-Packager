package org.cidarlab.EugeneParser.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cidarlab.OwlPackager.Util.ShellExec;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.adaptors.DNAplotlibClient;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Orientation;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartProperty;
import org.cidarlab.OwlPackager.dom.PartType;
import org.junit.Test;

public class DNAplotlibTest {
	
	@Test
	public void testDNAplotlib(){

			PartProperty pp = new PartProperty("pBla", "AAATTT");
			Part part = new Part("p1", PartType.PROMOTER, Orientation.FORWARD, pp);
			
			PartProperty pp2 = new PartProperty("p1", "GGGCCC");
			Part part2 = new Part("ri1", PartType.RIBOZYME, Orientation.FORWARD, pp2);
			
			PartProperty pp3 = new PartProperty("p2", "GGGCCC");
			Part part3= new Part("rbs1", PartType.RBS, Orientation.FORWARD, pp3);
			
			PartProperty pp4 = new PartProperty("p3", "GGGCCC");
			Part part4 = new Part("g1", PartType.CDS, Orientation.FORWARD, pp4);
			
			PartProperty pp5 = new PartProperty("p4", "GGGCCC");
			Part part5 = new Part("t1", PartType.TERMINATOR, Orientation.FORWARD, pp5);
			
			List<Part> pl = new ArrayList<>();
			pl.add(part);
			pl.add(part2);
			pl.add(part3);
			pl.add(part4);
			pl.add(part5);
			
			GeneticConstruct gc = new GeneticConstruct("DNAplotlibTest1", pl);
			
			String arguments = DNAplotlibClient.generateDNAplotlibParams(gc);
			System.out.println(arguments);
			
			Map<String,String> pigeonMap = new LinkedHashMap<>();
			pigeonMap.put(gc.getName(), arguments);
			
			Map<String,String> images = new LinkedHashMap<String, String>();
			ShellExec shell = new ShellExec(true, false);
			
			File dpl = new File("C:\\CIDAR\\dnaplotlib\\apps\\quick.py");
			
			try {
				int exitCode = shell.execute("python", Utilities.getOutputFilepath(), true, dpl.getAbsolutePath(), "-input", pigeonMap.get(gc.getName()), "-output", "DNAplotlibTest1.png");
				if(exitCode == 0){
	            	// read png file and save its path to the map Device_name : Image_path.
					File f = new File(Utilities.getOutputFilepath()+Utilities.getFileDivider()+"DNAplotlibTest1.png");
					images.put("DNAplotlibTest1", f.getAbsolutePath());
					System.out.println(f.getAbsolutePath());
				} else {
					System.err.println("PDF-LaTeX returned exit code = " + exitCode);
					System.exit(1);
				}
			} catch (IOException e) {
            System.err.println("ERROR. DNAplotlib has failed. Reason: " + e.getMessage());
			}
			
			
	}
}
