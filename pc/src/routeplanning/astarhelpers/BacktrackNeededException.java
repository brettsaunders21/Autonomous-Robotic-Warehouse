package routeplanning.astarhelpers;

import lejos.geom.Point;

public class BacktrackNeededException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000579165666660215L;
	private final Point blockedPoint;
	
	/**@param the point which cannot be traversed for this route*/
	public BacktrackNeededException(Point blockedPoint) {
		this.blockedPoint = blockedPoint;
	}
	
	/**@return the point which cannot be traversed for this route*/
	public Point getBlockedPoint() {
		return blockedPoint;
	}
}