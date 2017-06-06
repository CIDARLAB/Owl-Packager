package org.cidarlab.OwlPackager.adaptors;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SequenceOntology;
import org.synbiohub.frontend.IdentifiedMetadata;
import org.synbiohub.frontend.SynBioHubException;
import org.synbiohub.frontend.SynBioHubFrontend;

public class SynBioHubAdaptor {
	
	SynBioHubFrontend frontend;
	
	public SynBioHubAdaptor(String synBioHubURL) {
        frontend = new SynBioHubFrontend(synBioHubURL);
    }
    
    public SynBioHubAdaptor(String synBioHubURL, String prefix) {
        frontend = new SynBioHubFrontend(synBioHubURL, prefix);
    }
    
    //login
    public void login(String email, String password) throws SynBioHubException{
    	frontend.login(email, password);
    }
    
    //logout
    public void logout(){
    	frontend.logout();
    }
    
    //submit to Synbiohub
    public void submit(String id, String version, String name, String description, String citations, String collections, String owerwrite_merge, SBOLDocument document) throws SynBioHubException{
    	frontend.submit(id, version, name, description, citations, collections, owerwrite_merge, document);
    }
    
    //remove SBOL
    public void removeSBOL(URI topLevelUri) throws SynBioHubException{
    	frontend.removeSBOL(topLevelUri);
    }
    
    public SBOLDocument getSBOL(String uri) throws SynBioHubException, URISyntaxException {
        return frontend.getSBOL(new URI(uri));
    }
    
    public List<IdentifiedMetadata> getCollectionMetadata() throws SynBioHubException, URISyntaxException {
        return frontend.getRootCollectionMetadata();
    }
    
    public List<String> getCollectionURIs() throws SynBioHubException, URISyntaxException {
        List<String> URIs = new ArrayList<String>();
        for (IdentifiedMetadata part : getCollectionMetadata()) {
            URIs.add(part.getUri());
        }
        return URIs;
    }
    
    public List<IdentifiedMetadata> getAllComponentDefinitionMetadata() throws SynBioHubException, URISyntaxException {
        return frontend.getMatchingComponentDefinitionMetadata(null, null, null, null, 0, 10000);
    }
    
    public List<String> getAllComponentDefinitionURIs() throws SynBioHubException, URISyntaxException {
        List<String> URIs = new ArrayList<String>();
        for (IdentifiedMetadata part : getAllComponentDefinitionMetadata()) {
            URIs.add(part.getUri());
        }
        return URIs;
    }
    
    public List<IdentifiedMetadata> getPromoterMetadata() throws SynBioHubException, URISyntaxException {
        return getPartMetadataUsingRole(SequenceOntology.PROMOTER);
    }
    
    public List<String> getPromoterURIs() throws SynBioHubException, URISyntaxException {
        return getPartURIsUsingRole(SequenceOntology.PROMOTER);
    }
    
    public List<IdentifiedMetadata> getPromotersMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartMetadata(SequenceOntology.PROMOTER, collectionURI);
    }
    
    public List<String> getPromoterURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartURIs(SequenceOntology.PROMOTER, collectionURI);
    }
    
    public List<IdentifiedMetadata> getRBSMetadata() throws SynBioHubException, URISyntaxException {
        return getPartMetadataUsingRole(SequenceOntology.RIBOSOME_ENTRY_SITE);
    }
    
    public List<String> getRBSURIs() throws SynBioHubException, URISyntaxException {
        return getPartURIsUsingRole(SequenceOntology.RIBOSOME_ENTRY_SITE);
    }
    
    public List<IdentifiedMetadata> getRBSMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartMetadata(SequenceOntology.RIBOSOME_ENTRY_SITE, collectionURI);
    }
    
    public List<String> getRBSURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartURIs(SequenceOntology.RIBOSOME_ENTRY_SITE, collectionURI);
    }
    
    public List<IdentifiedMetadata> getCDSMetadata() throws SynBioHubException, URISyntaxException {
        return getPartMetadataUsingRole(SequenceOntology.CDS);
    }
    
    public List<String> getCDSURIs() throws SynBioHubException, URISyntaxException {
        return getPartURIsUsingRole(SequenceOntology.CDS);
    }
    
    public List<IdentifiedMetadata> getCDSMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartMetadata(SequenceOntology.CDS, collectionURI);
    }
    
    public List<String> getCDSURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartURIs(SequenceOntology.CDS, collectionURI);
    }
    
    public List<IdentifiedMetadata> getTerminatorMetadata() throws SynBioHubException, URISyntaxException {
        return getPartMetadataUsingRole(SequenceOntology.TERMINATOR);
    }
    
    public List<String> getTerminatorURIs() throws SynBioHubException, URISyntaxException {
        return getPartURIsUsingRole(SequenceOntology.TERMINATOR);
    }
    
    public List<IdentifiedMetadata> getTerminatorMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartMetadata(SequenceOntology.TERMINATOR, collectionURI);
    }
    
    public List<String> getTerminatorURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        return getPartURIs(SequenceOntology.TERMINATOR, collectionURI);
    }
    
    public List<IdentifiedMetadata> getRibozymeMetadata() throws SynBioHubException, URISyntaxException, MalformedURLException {
        return getPartMetadataUsingRole(new URL("http://identifiers.org/so/SO:0000374").toURI());
    }
    
    public List<String> getRibozymeURIs() throws SynBioHubException, URISyntaxException, MalformedURLException {
        return getPartURIsUsingRole(new URL("http://identifiers.org/so/SO:0000374").toURI());
    }
    
    public List<IdentifiedMetadata> getRibozymeMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException, MalformedURLException {
        return getPartMetadata(new URL("http://identifiers.org/so/SO:0000374").toURI(), collectionURI);
    }
    
    public List<String> getRibozymeURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException, MalformedURLException {
        return getPartURIs(new URL("http://identifiers.org/so/SO:0000374").toURI(), collectionURI);
    }
        
    private List<IdentifiedMetadata> getPartMetadataUsingRole(URI role) throws SynBioHubException, URISyntaxException {
        Set<URI> roles = new HashSet<URI>();
        roles.add(role);
        return frontend.getMatchingComponentDefinitionMetadata(null, roles, null, null, 0, 10000);
    }
    
    public List<IdentifiedMetadata> getPartMetadataFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        Set<URI> collections = new HashSet<URI>();
        collections.add(new URI(collectionURI));
        return frontend.getMatchingComponentDefinitionMetadata(null, null, null, collections, 0, 10000);
    }
    
    private List<IdentifiedMetadata> getPartMetadata(URI role, String collectionURI) throws SynBioHubException, URISyntaxException {
        Set<URI> roles = new HashSet<URI>();
        roles.add(role);
        Set<URI> collections = new HashSet<URI>();
        collections.add(new URI(collectionURI));
        return frontend.getMatchingComponentDefinitionMetadata(null, roles, null, collections, 0, 10000);
    }
    
    private List<String> getPartURIsUsingRole(URI role) throws SynBioHubException, URISyntaxException {
        List<String> URIs = new ArrayList<String>();
        for (IdentifiedMetadata part : getPartMetadataUsingRole(role)) {
            URIs.add(part.getUri());
        }
        return URIs;
    }
    
    public List<String> getPartURIsFromCollection(String collectionURI) throws SynBioHubException, URISyntaxException {
        List<String> URIs = new ArrayList<String>();
        for (IdentifiedMetadata part : getPartMetadataFromCollection(collectionURI)) {
            URIs.add(part.getUri());
        }
        return URIs;
    }
    
    private List<String> getPartURIs(URI role, String collectionURI) throws SynBioHubException, URISyntaxException {
        List<String> URIs = new ArrayList<String>();
        for (IdentifiedMetadata part : getPartMetadata(role, collectionURI)) {
            URIs.add(part.getUri());
        }
        return URIs;
    }
	
}
