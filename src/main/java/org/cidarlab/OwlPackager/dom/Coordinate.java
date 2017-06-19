package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class Coordinate {

	@Getter private String partInstance;
	@Getter private int partPosition;
	@Getter private int startX;
	@Getter private int endX;
	
	public Coordinate(String partInstance, int partPosition, int startX, int endX) {
		this.partInstance = partInstance;
		this.partPosition = partPosition;
		this.startX = startX;
		this.endX = endX;
	}
	
}
