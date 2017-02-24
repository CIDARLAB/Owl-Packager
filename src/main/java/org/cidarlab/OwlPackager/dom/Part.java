package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class Part {
	
	@Getter private String partInstance;
	@Getter private PartType partType;
	@Getter private Orientation orientation;
	@Getter private PartProperty partProperties;
	
	public Part(String partInstance, PartType partType, Orientation orientation, PartProperty partProperties) {
		this.partInstance = partInstance;
		this.partType = partType;
		this.orientation = orientation;
		this.partProperties = partProperties;
	}

	@Override
	public String toString() {
		
		return "Part [partInstance=" + partInstance + ", partType=" + partType + ", orientation=" + orientation + ", partProperties=" + partProperties + "]";
	}
	
	
}
