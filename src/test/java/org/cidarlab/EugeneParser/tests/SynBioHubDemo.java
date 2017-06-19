package org.cidarlab.EugeneParser.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.adaptors.DeviceSplitter;
import org.cidarlab.OwlPackager.adaptors.GeneticConstructFactory;
import org.cidarlab.OwlPackager.adaptors.RegExpDevice;
import org.cidarlab.OwlPackager.adaptors.SbolExporter;
import org.cidarlab.OwlPackager.adaptors.SynBioHubAdaptor;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidate;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SBOLWriter;
import org.synbiohub.frontend.SynBioHubException;

public class SynBioHubDemo {
	
	private SynBioHubAdaptor instance;
	
	public static List<Part> uniqueParts = new ArrayList<>();
	
	@BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SynBioHubAdaptor("https://synbiohub.programmingbiology.org/");
        try {
			instance.login("yvi1@bu.edu", "cidarlab");
		} catch (SynBioHubException e) {
			e.printStackTrace();
		}
    }
    
    @After
    public void tearDown() {
    	instance.logout();
    }
    
    
    @Test
    public void testSynBioHubDemo() throws SBOLValidationException, URISyntaxException, SynBioHubException, IOException, SBOLConversionException{
    	    	   	
    	System.out.println("OWL reads Eugene results...");
    	
    	//File file = new File(Utilities.getResourcesFilepath()+"Fang_Monocistronic.out");
    	File file = new File(Utilities.getResourcesFilepath()+"Gate.out");
    	if(file.exists()){
    		System.out.println(file.getAbsolutePath());
    	}
    	
    	String eugeneFile = Utilities.readEugeneOutput(file);
    	
    	//get number of the assembled devices from the Eugene output Array[] file.
        long deviceNumber = 0;
        deviceNumber = RegExpDevice.getDeviceNumber(eugeneFile);
        int limit = (int)deviceNumber;
    	
        System.out.println("\nOwl assembled "+ limit + " gates.");
        
        String[] dSections = DeviceSplitter.splitArrayIntoDevices(eugeneFile);
        
        String[] newSections = Arrays.copyOf(dSections, 100);
        
        SBOLDocument sdoc = new SBOLDocument();
		sdoc.setDefaultURIprefix("https://www.cidarlab.org/owl/Synbiohub_Demo/");
		sdoc.setComplete(true);
		sdoc.setCreateDefaults(true);
        
		List<GeneticConstruct> constructList = new ArrayList<>();
		for(String s: newSections){
			
			// the first element contains device name; the second element contains device contents (all information about the parts and properties)
			String[] mySplit = s.trim().split("\\(", 2);
			
			// sends deviceContents to GeneticConstructFactory and receives assembled GeneticConstruct
			GeneticConstruct gc = GeneticConstructFactory.assembleGeneticConstruct(mySplit[0], mySplit[1]);
			
			// add union of parts into a collection
			for(Part part: gc.getPartList()){
				if(!SbolExporter.partExists(uniqueParts, part.getPartProperties().getName())){
					System.out.println("adding part "+ part.getPartProperties().getName() + " to unique collection.");
					uniqueParts.add(part);
				}
			}
			constructList.add(gc);	
		}
		
		for(Part p : uniqueParts){
			System.out.println(p.getPartProperties().getName());
		}
		sdoc = SbolExporter.createComponentDefinitionForParts(uniqueParts, sdoc);
		
		
		// submit constructs to SBOL factory
		for(GeneticConstruct construct : constructList){
			sdoc = SbolExporter.geneticConstructToComponentDefinition(construct, sdoc, uniqueParts);
		}
		
		SBOLValidate.validateSBOL(sdoc, true, true, true);
		if(SBOLValidate.getNumErrors() > 0){
			for(String error : SBOLValidate.getErrors()){
				System.out.println(error);
			}
		} else {
			System.out.println("\n=====================\nGenerated SBOL file is valid.");
		}
		
		//SBOLWriter.write(sdoc, Utilities.getResourcesFilepath()+"failedToUploadSBOL.xml");
		
		// SUBMIT to SynBioHub
		instance.submit("Owl_SynbiohubDemo2", "1.0", "Owl_SynbiohubDemo2", "Cello gates designed by Owl", "27034378", "Synbiohub_Owl", "1", sdoc);
    }
}
