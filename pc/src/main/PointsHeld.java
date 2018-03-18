package main;

import java.util.ArrayList;
import lejos.geom.Point;

public class PointsHeld {
	
	private ArrayList<Point> pointsHeld = new ArrayList<Point>();
	
	public PointsHeld() {
	}
	
	public void holdAt(Point p) {
		if (!pointsHeld.contains(p)) {
			pointsHeld.add(p);
		}
	}
	
	public boolean isStillHeld(Point p) {
		return pointsHeld.contains(p);
	}
	
	public void freeUp(Point p) {
		if (pointsHeld.contains(p)) {
			pointsHeld.remove(p);
		}
	}
}