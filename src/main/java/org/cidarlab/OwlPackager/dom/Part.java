package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class Part {
	
	@Getter private String partInstance;
	@Getter private PartType partType;
	@Getter private Orientation orientation;
	@Getter private PartProperty partProperties;
	
	public Part(String partInstance, PartType partType, boolean isReversePart, PartProperty partProperties) {
		this.partInstance = partInstance;
		this.partType = partType;
		if(isReversePart){
			this.orientation = Orientation.REVERSE;
		} else {
			this.orientation = Orientation.FORWARD;
		}
		this.partProperties = partProperties;
	}

	@Override
	public String toString() {
		
		return "Part [partInstance=" + partInstance + ", partType=" + partType + ", orientation=" + orientation + ", partProperties=" + partProperties + "]";
	}
	
	
}
