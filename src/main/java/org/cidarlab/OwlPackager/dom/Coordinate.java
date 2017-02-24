package org.cidarlab.OwlPackager.dom;

import lombok.Getter;

public class Coordinate {

	@Getter private int startX;
	@Getter private int endX;
	
	public Coordinate(int startX, int endX) {
		this.startX = startX;
		this.endX = endX;
	}
	
}
