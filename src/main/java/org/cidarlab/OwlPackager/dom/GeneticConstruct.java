package org.cidarlab.OwlPackager.dom;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class GeneticConstruct {
	
	@Getter private final String name;
	@Getter private String sequence = "";
	@Getter private List<Part> partList;
	@Getter private List<Coordinate> coordinateList;
	
	public GeneticConstruct(String name, List<Part> partList) {
		List<Coordinate> coords = new ArrayList<Coordinate>();
		
		int counter = 0;
		this.name = name;
		for(Part part:partList){
			this.sequence += part.getPartProperties().getSequence();
			Coordinate coord = new Coordinate(part.getPartInstance(), partList.indexOf(part)+1, counter+1, counter+part.getPartProperties().getSequence().length());
			coords.add(coord);
			counter += part.getPartProperties().getSequence().length();
		}
		this.coordinateList = coords;
		this.partList = partList;		
		
	}
	
	public int getPartStartX(int partPosition){
		
		for(Coordinate coord: this.getCoordinateList()) {
			if(coord != null && coord.getPartPosition() == partPosition) {
				return coord.getStartX();
			}
		}
		return 0;
	}
	
	public int getPartEndX(int partPosition){
		
		for(Coordinate coord: this.getCoordinateList()) {
			if(coord != null && coord.getPartPosition() == partPosition) {
				return coord.getEndX();
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		
		return "GeneticConstruct [name=" + name + ", sequence=" + sequence + ", partList=" + partList + "]";
	}
	
	
}
