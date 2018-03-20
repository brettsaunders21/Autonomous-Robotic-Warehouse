package routeplanning.astarhelpers;

import lejos.geom.Point;

public class BacktrackNeededException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000579165666660215L;
	private final Point blockedPoint;
	
	public BacktrackNeededException(Point blockedPoint) {
		this.blockedPoint = blockedPoint;
	}
	
	public Point getBlockedPoint() {
		return blockedPoint;
	}
}