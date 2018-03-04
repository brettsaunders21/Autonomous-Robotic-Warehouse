package routeplanning;

import lejos.geom.Point;

public class RouteCoordInfo{
	private final Point thisPoint;
	private final Point originPoint;
	private final double distanceToDest;
	
	public RouteCoordInfo(Point thisPoint, Point originPoint, double distanceToDest) {
		this.thisPoint = thisPoint;
		this.originPoint = originPoint;
		this.distanceToDest = distanceToDest;
	}
}