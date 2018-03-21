package main;

import java.util.ArrayList;
import lejos.geom.Point;

public class PointsHeld {

	private ArrayList<Point> pointsHeld = new ArrayList<Point>();

	public PointsHeld() {
	}

	// keeps the robot at point p
	public void holdAt(Point p) {
		if (!pointsHeld.contains(p)) {
			pointsHeld.add(p);
		}
	}

	// checks to see if the robot is still held at point p
	public boolean isStillHeld(Point p) {
		return pointsHeld.contains(p);
	}

	// frees up point p for robot to move into
	public void freeUp(Point p) {
		if (pointsHeld.contains(p)) {
			pointsHeld.remove(p);
		}
	}
}