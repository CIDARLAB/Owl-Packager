package org.cidarlab.EugeneParser.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.adaptors.SbolExporter;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartProperty;
import org.cidarlab.OwlPackager.dom.PartType;
import org.junit.Test;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidate;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SBOLWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SbolExporterTest {
	
	@Test
	public void testSbolExport() throws SBOLValidationException, URISyntaxException, SBOLConversionException, IOException{
		
		PartProperty pp = new PartProperty("pBla", "AAATTT");
		Part part = new Part("p1", PartType.PROMOTER, false, pp);
		
		PartProperty pp2 = new PartProperty("abc1", "ATGCTAGCTAGCTAATCGATCTAA");
		Part part2 = new Part("cds1", PartType.CDS, false, pp2);
		
		PartProperty pp3 = new PartProperty("T1", "GGGCCC");
		Part part3 = new Part("t1", PartType.TERMINATOR, false, pp3);
		
		List<Part> pl = new ArrayList<>();
		pl.add(part);
		pl.add(part2);
		pl.add(part3);
		
		GeneticConstruct gc = new GeneticConstruct("Monocistronic_prgt_1", pl);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(gc));
		
		
		// Export SBOL file
		SBOLDocument sdoc = new SBOLDocument();
		sdoc.setDefaultURIprefix("http://cidarlab.org/owl/");
		sdoc.setComplete(true);
		sdoc.setCreateDefaults(true);
		String version = "1.0";
		
		for(Part p: pl){
			sdoc = SbolExporter.partToComponentDefinition(p, sdoc);
		}
		
		sdoc.createModuleDefinition(gc.getName(), version);
		
		SBOLValidate.validateSBOL(sdoc, true, true, true);
		if(SBOLValidate.getNumErrors() > 0){
			for(String error: SBOLValidate.getErrors()) {
				System.out.println(error);
			}
			return;
		}
		
		SBOLWriter.write(sdoc, (System.out));
		SBOLWriter.write(sdoc, Utilities.getResourcesFilepath()+"exportedSBOL.xml");
		
		
		
	}
}
