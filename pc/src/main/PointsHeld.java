package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lejos.geom.Point;

public class PointsHeld {

	private List<Point> pointsHeld = Collections.synchronizedList(new ArrayList<Point>());

	public PointsHeld() {
	}

	// keeps the robot at point p
	public synchronized void holdAt(Point p, Point q) {
		if (pointsHeld.contains(q)) {
			pointsHeld.remove(q);
		}
		if (!pointsHeld.contains(p)) {
			pointsHeld.add(p);
		}
	}

	// checks to see if the robot is still held at point p
	public boolean isStillHeld(Point p) {
		return pointsHeld.contains(p);
	}
}