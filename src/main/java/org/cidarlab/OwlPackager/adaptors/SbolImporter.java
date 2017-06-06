package org.cidarlab.OwlPackager.adaptors;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartProperty;
import org.cidarlab.OwlPackager.dom.PartType;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceOntology;

public class SbolImporter {
	
	public static SBOLDocument doImport(File sbol) throws SBOLValidationException, IOException, SBOLConversionException{
		
		SBOLDocument sd = new SBOLDocument();
		sd.read(sbol);
		
		return sd;
	}
	
	
	public static GeneticConstruct sbolToConstruct(SBOLDocument sbol) throws SBOLValidationException, MalformedURLException, URISyntaxException{
		
		final URI ribozymeSeqOntology = new URL("http://identifiers.org/so/SO:0000374").toURI();
		
		String name = null;
		List<Part> parts = new ArrayList<>();
		
		for(ComponentDefinition cd: sbol.getRootComponentDefinitions()){
			
			name = cd.getDisplayId();
			for(Component c: cd.getSortedComponents()){
				
				PartProperty pp = new PartProperty(c.getName(), c.getDefinition().getSequenceByEncoding(Sequence.IUPAC_DNA).toString());
				
				if(c.getDefinition().containsRole(SequenceOntology.PROMOTER)){
					//cd.getSequenceAnnotation(c).
					Part part = new Part("p"+cd.getSortedComponents().indexOf(c)+1, PartType.PROMOTER, false, pp);
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.GENE)){
					Part part = new Part("c"+cd.getSortedComponents().indexOf(c)+1, PartType.CDS, false, pp);
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.RIBOSOME_ENTRY_SITE)){
					Part part = new Part("rbs"+cd.getSortedComponents().indexOf(c)+1, PartType.RBS, false, pp);
					parts.add(part);
				}
				if(c.getDefinition().containsRole(SequenceOntology.TERMINATOR)){
					Part part = new Part("t"+cd.getSortedComponents().indexOf(c)+1, PartType.TERMINATOR, false, pp);
					parts.add(part);
				}
				if(c.getDefinition().containsRole(ribozymeSeqOntology)){
					Part part = new Part("r"+cd.getSortedComponents().indexOf(c)+1, PartType.RIBOZYME, false, pp);
					parts.add(part);
				}
			}
			
		}
		GeneticConstruct gc = new GeneticConstruct(name, parts);
		
		return gc;
	}
	
}
