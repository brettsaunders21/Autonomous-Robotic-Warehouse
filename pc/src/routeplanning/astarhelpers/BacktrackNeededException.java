package routeplanning.astarhelpers;

import lejos.geom.Point;

public class BacktrackNeededException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2551145307258452266L;
	private final Point blockedPoint;
	private final int relTime;
	
	public BacktrackNeededException(Point blockedPoint, int relTime) {
		this.blockedPoint = blockedPoint;
		this.relTime = relTime;
	}
	
	public Point getBlockedPoint() {
		return blockedPoint;
	}
	
	public int getRelTime() {
		return relTime;
	}
}