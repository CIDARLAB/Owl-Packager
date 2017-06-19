package org.cidarlab.OwlPackager.adaptors;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartType;
import org.sbolstandard.core2.AccessType;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SequenceAnnotation;
import org.sbolstandard.core2.SequenceOntology;

public class SbolExporter {
	
	public static SBOLDocument partToComponentDefinition(Part part, SBOLDocument sbol) throws SBOLValidationException, MalformedURLException, URISyntaxException{
		
		final URI ribozymeSeqOntology = new URL("http://identifiers.org/so/SO:0000374").toURI();
		String version = "1.0";
		
		sbol.createSequence(part.getPartProperties().getName()+"_seq", version, part.getPartProperties().getSequence(), Sequence.IUPAC_DNA);
		
		if(part.getPartType() == PartType.PROMOTER){
			sbol.createComponentDefinition(part.getPartProperties().getName(), version, ComponentDefinition.DNA).addRole(SequenceOntology.PROMOTER);
		}
		if(part.getPartType() == PartType.RBS){
			sbol.createComponentDefinition(part.getPartProperties().getName(), version, ComponentDefinition.DNA).addRole(SequenceOntology.RIBOSOME_ENTRY_SITE);
		}
		if(part.getPartType() == PartType.CDS){
			sbol.createComponentDefinition(part.getPartProperties().getName(), version, ComponentDefinition.DNA).addRole(SequenceOntology.CDS);
		}
		if(part.getPartType() == PartType.RIBOZYME){
			sbol.createComponentDefinition(part.getPartProperties().getName(), version, ComponentDefinition.DNA).addRole(ribozymeSeqOntology);
		}
		if(part.getPartType() == PartType.TERMINATOR){
			sbol.createComponentDefinition(part.getPartProperties().getName(), version, ComponentDefinition.DNA).addRole(SequenceOntology.TERMINATOR);
		}
		
		return sbol;
	}
	
	
	public static SBOLDocument createComponentDefinitionForParts(List<Part> uniquePartCollection, SBOLDocument sbol) throws SBOLValidationException, MalformedURLException, URISyntaxException{
		String version = "1.0";
		
		for(Part p: uniquePartCollection) {
			ComponentDefinition cd = sbol.createComponentDefinition(p.getPartProperties().getName(), version, ComponentDefinition.DNA);
			cd.addRole(getType(p.getPartType()));
			Sequence seq = sbol.createSequence(p.getPartProperties().getName()+"_seq", version, p.getPartProperties().getSequence(), Sequence.IUPAC_DNA);
			cd.addSequence(seq);
		}
		return sbol;
	}
	
	public static SBOLDocument geneticConstructToComponentDefinition(GeneticConstruct construct, SBOLDocument sbol, List<Part> partUnion) throws SBOLValidationException, MalformedURLException, URISyntaxException{
		String version = "1.0";
		
		ComponentDefinition device = sbol.createComponentDefinition(construct.getName(), version, ComponentDefinition.DNA);
		device.addRole(SequenceOntology.ENGINEERED_REGION);
		Sequence deviceSeq = sbol.createSequence(construct.getName()+"_seq", construct.getSequence(), Sequence.IUPAC_DNA);
		device.addSequence(deviceSeq);
		device.createAnnotation(new QName(sbol.getDefaultURIprefix(), "owl_experience", "owl"), getWorksOrFails());
		device.createAnnotation(new QName(sbol.getDefaultURIprefix(), "wasGeneratedBy", "prov"), "Owl");
	
		for(Part part: construct.getPartList()){
			int partPosition = construct.getPartList().indexOf(part)+1;
			Component component = device.createComponent(construct.getName()+"_"+part.getPartProperties().getName()+"_"+partPosition, AccessType.PRIVATE, sbol.getComponentDefinition(part.getPartProperties().getName(), version).getIdentity());
			SequenceAnnotation sa = device.createSequenceAnnotation(construct.getName()+"_"+part.getPartProperties().getName()+"_"+partPosition+"_sa", "position_"+partPosition, construct.getPartStartX(construct.getPartList().indexOf(part)+1), construct.getPartEndX(construct.getPartList().indexOf(part)+1), OrientationType.INLINE);
			sa.setComponent(component.getIdentity());
		}
		return sbol;
	}
	
	public static boolean partExists(Collection<Part> c, String partName){
		for(Part part: c){
			if(part != null && part.getPartProperties().getName().equalsIgnoreCase(partName)){
				return true;
			}
		}
		return false;
	}
	
	private static URI getType(PartType type) throws MalformedURLException, URISyntaxException{
		
		URI uri = null;
		
		if(type == PartType.CDS){
			uri = SequenceOntology.CDS; 
		}
		if(type == PartType.RBS){
			uri = SequenceOntology.RIBOSOME_ENTRY_SITE; 
		}
		if(type == PartType.RIBOZYME){
			uri = new URL("http://identifiers.org/so/SO:0000374").toURI(); 
		}
		if(type == PartType.PROMOTER){
			uri = SequenceOntology.PROMOTER; 
		}
		if(type == PartType.TERMINATOR){
			uri = SequenceOntology.TERMINATOR; 
		}
		
		return uri;
	}
	
	private static String getWorksOrFails(){
		if(1 + (int)(Math.random() * ((2-1)+1)) == 1){
			return "works";
		}
		return "fails";
	}
}
