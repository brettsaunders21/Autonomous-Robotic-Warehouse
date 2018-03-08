package routeplanning.astarhelpers;

import lejos.geom.Point;

public class BacktrackNeededException extends RuntimeException{

	private static final long serialVersionUID = 4277318767619487661L;
	private final Point blockedPoint;
	private final Point previousPoint;
	
	public BacktrackNeededException(String message, Point blockedPoint, Point previousPoint) {
		super(message);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
	}
	
	public BacktrackNeededException(String message, Throwable error, Point blockedPoint, Point previousPoint) {
		super(message, error);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
	}
	
	public BacktrackNeededException(Throwable error, Point blockedPoint, Point previousPoint) {
		super(error);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
	}
	
	public Point getBlockedPoint() {
		return blockedPoint;
	}
	
	public Point getPreviousPoint() {
		return previousPoint;
	}
}