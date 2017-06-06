package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class Coordinate {

	@Getter private String partInstance;
	@Getter private int startX;
	@Getter private int endX;
	
	public Coordinate(String partInstance, int startX, int endX) {
		this.partInstance = partInstance;
		this.startX = startX;
		this.endX = endX;
	}
	
}
