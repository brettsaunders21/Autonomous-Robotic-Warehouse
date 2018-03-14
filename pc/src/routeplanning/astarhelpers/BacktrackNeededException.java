package routeplanning.astarhelpers;

import lejos.geom.Point;

public class BacktrackNeededException extends RuntimeException{

	private static final long serialVersionUID = 4277318767619487661L;
	private final Point blockedPoint;
	private final Point previousPoint;
	private final int time;
	
	public BacktrackNeededException(String message, Point blockedPoint, Point previousPoint, int time) {
		super(message);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
		this.time = time;
	}
	
	public BacktrackNeededException(String message, Throwable error, Point blockedPoint, Point previousPoint, int time) {
		super(message, error);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
		this.time = time;
	}
	
	public BacktrackNeededException(Throwable error, Point blockedPoint, Point previousPoint, int time) {
		super(error);
		this.blockedPoint = blockedPoint;
		this.previousPoint = previousPoint;
		this.time = time;
	}
	
	public Point getBlockedPoint() {
		return blockedPoint;
	}
	
	public Point getPreviousPoint() {
		return previousPoint;
	}
	
	public int getTime() {
		return time;
	}
}