package routeplanning.astarhelpers;

import lejos.geom.Point;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */

public class RouteCoordInfo{
	private static final int MAX_NUM_OF_CHILDREN = 4;
	
	private final Point thisPoint;
	private final Point originPoint;
	private Point[] children;
	private final double distanceToDest;
	private final int distFromStart;
	private final int timeOnRoute;
	
	/**@param thisPoint the coordinates of the point this object relates to
	 * @param originPoint the coordinates of the point that was traversed through to reach this point
	 * @param distanceToTest the absolute distance to the target position
	 * @param distFromStart the shortest distance in grid units from the start coordinate*/
	public RouteCoordInfo(Point thisPoint, Point originPoint ,double distanceToDest, int distFromStart, int timeOnRoute) {
		if (distanceToDest<0) {
			throw new IllegalArgumentException("distance to destination must be positive or 0");
		}
		if (distFromStart<0) {
			throw new IllegalArgumentException("distance from start must be positive or 0");
		}
		this.thisPoint = thisPoint;
		this.originPoint = originPoint;
		this.distanceToDest = distanceToDest;
		this.distFromStart = distFromStart;
		this.children = new Point[] {};
		this.timeOnRoute = timeOnRoute;
	}
	
	/**@return the coordinates of the point this object relates to*/
	public Point getThisPoint() {
		return thisPoint;
	}
	
	/**@return the coordinates of the point that is traversed just before this point*/
	public Point getOriginPoint() {
		return originPoint;
	}
	
	/**@return an array of all points which have this point as their origin*/
	public Point[] getChildren() {
		return children;
	}
	
	/**@param children all points which have this point as their origin*/
	public void setChildren(Point[] children) {
		if (children.length>MAX_NUM_OF_CHILDREN) {
			throw new IllegalArgumentException("maximum number of children exceeded");
		}
		this.children = children;
	}
	
	/**@return the absolute distance to the target coordinate*/
	public double getDistToGoal() {
		return distanceToDest;
	}
	
	/**@return the shortest distance in grid units from the start coordinate*/
	public int getDistFromStart() {
		return distFromStart;
	}
	
	/**@return the sum of the distance from the start in grid units and the absolute distance to the target coordinate from this coordinate*/
	public double getTotalPointDist() {
		return distanceToDest + distFromStart;
	}
	
	public int getTimeOnRoute() {
		return timeOnRoute;
	}
}