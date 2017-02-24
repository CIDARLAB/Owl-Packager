package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class PartProperty {
	
	@Getter private String name;
	@Getter private String sequence;
	
	public PartProperty(String name, String sequence) {
		this.name = name;
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		
		return "PartProperty [name=" + name + ", sequence=" + sequence + "]";
	}
	
	
	
}
