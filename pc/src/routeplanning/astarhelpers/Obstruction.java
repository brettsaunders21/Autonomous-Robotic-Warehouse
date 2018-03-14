package routeplanning.astarhelpers;

import lejos.geom.Point;

public class Obstruction {
	private final int time;
	private final Point coordinate;
	
	public Obstruction(int time, Point coordinate) {
		this.time = time;
		this.coordinate = coordinate;
	}
	
	public int getTime() {
		return time;
	}
	
	public Point getCoordinate() {
		return coordinate;
	}
}