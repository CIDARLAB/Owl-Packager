package org.cidarlab.EugeneParser.tests;

import java.util.ArrayList;
import java.util.List;

import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.adaptors.DeviceSplitter;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartProperty;
import org.cidarlab.OwlPackager.dom.PartType;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParserTest {

	@Test
	public void testEugeneParser(){
		String eugeneFile = Utilities.readEugeneOutput();
		
		String[] dSections = DeviceSplitter.splitArrayIntoDevices(eugeneFile);
		
		for(String s: dSections){
			// first element contains device name; second element is device contents
			String[] mySplit = s.trim().split("\\(", 2);
			System.out.println(mySplit[0] + "\n" + mySplit[1] + "\n===============");
			
			//send individual devices to DeviceFactory and receive a new instance of a Device with that name.

			// Send deviceContents to ComponentFactory

			PartProperty pp = new PartProperty("pBla", "AAATTT");
			Part part = new Part("p1", PartType.PROMOTER, false, pp);
			
			PartProperty pp2 = new PartProperty("pBla", "GGGCCC");
			Part part2 = new Part("cds1", PartType.CDS, false, pp2);
			
			List<Part> pl = new ArrayList<>();
			pl.add(part);
			pl.add(part2);
			
			GeneticConstruct gc = new GeneticConstruct("Monocistronic_prgt_1", pl);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(gc));
			
			System.out.println(gson.fromJson(gson.toJson(gc), GeneticConstruct.class));
			
		}
	}
	
}
