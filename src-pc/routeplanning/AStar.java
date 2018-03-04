package routeplanning;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import interfaces.Action;
import interfaces.Pose;
import lejos.geom.Point;

public class AStar {
	final static Logger logger = Logger.getLogger(AStar.class);

	/**
	 * Directions for route planning, for global directions see
	 * {@link interfaces.Direction}
	 */
	private final static int NEG_X = 0;
	private final static int NEG_Y = 1;
	private final static int POS_X = 2;
	private final static int POS_Y = 3;
	private final static int STILL = 4;

	private final Map map;

	/**
	 * @param m
	 *            the map that route finding will be carried out on
	 */
	public AStar(Map map) {
		this.map = map;
	}

	/**If more than one direction has the same absolute distance then the priority of directions is NEG_X, NEG_Y, POS_X, POS_Y
	 * @param currentPosition
	 *            the starting coordinate of the route
	 * @param targetPosition
	 *            the target coordinate of the route
	 * @return shortest route between the two points avoiding obstacles
	 * @throws IllegalArgumentException
	 *             one or both coordinates are not within the map area
	 * @throws IllegalStateException
	 *             one or both coordinates are in an inaccessible area
	 */
	public Route generateRoute(Point currentPosition, Point targetPosition, Pose startingPose, Route[] routes) {
		Map tempMap = map.clone();
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		ArrayList<Point> tempCoords = new ArrayList<Point>();
		ArrayList<Integer> tempDirs = new ArrayList<Integer>();
		int length = 0;
		int timeInterval = 0;
		Point testPoint = currentPosition.clone();
		boolean atDestination = currentPosition.equals(targetPosition);

		// checks that both points are within the map
		if (!tempMap.withinMapBounds(currentPosition) || !tempMap.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		
		// loops until coordinates are at the target coordinates
		while (!atDestination) {
			// double absoluteDistance = testPoint.distance(targetPosition);
			double nextDist = Double.POSITIVE_INFINITY;
			int shortestDir = 0;
			// Point nextDir;
			Point[] testDirs = new Point[] { new Point(testPoint.x - 1, testPoint.y), // left
					new Point(testPoint.x, testPoint.y - 1), // up
					new Point(testPoint.x + 1, testPoint.y), // right
					new Point(testPoint.x, testPoint.y + 1) }; // down

			// indexes: [0]:left, [1]:right, [2]:up, [3]:down
			for (int allDirs = 0; allDirs < 4; allDirs++) {
				// for each point within the bounds of the map
				if (tempMap.withinMapBounds(testDirs[allDirs])) {
					// if test point is passable and makes the route shorter than the
					if (tempMap.isPassable(testDirs[allDirs])
							&& (testDirs[allDirs].distance(targetPosition) < nextDist)) {
						nextDist = testDirs[allDirs].distance(targetPosition);
						shortestDir = allDirs;
					}
				}
			}

			// if another robot is already going to be at the coordinates
			if (!otherRobotAtCoord(testDirs[shortestDir], timeInterval)) {
				tempCoords.add(testDirs[shortestDir]);
				tempDirs.add(shortestDir);
				testPoint = testDirs[shortestDir];
				timeInterval++;
			} else {
				tempCoords.add(testPoint);
				tempDirs.add(STILL);
				timeInterval++;
			}
			atDestination = testPoint.equals(targetPosition);
		}
		// moves final coordinates to queue and finalises the length of the route
		coordinates.addAll(tempCoords);
		length = timeInterval;
		BlockingQueue<Action> directions = generateDirectionsQueue(tempDirs);
		return new Route(coordinates, directions, length, startingPose,
				differenceInPose(poseToInt(startingPose), tempDirs.get(0), true));
	}

	/*Converts from pose to int*/
	private int poseToInt(Pose p) {
		switch (p) {
		case POS_X:{
			return POS_X;
		}
		case POS_Y:{
			return POS_Y;
		}
		case NEG_X:{
			return NEG_X;
		}
		case NEG_Y:{
			return NEG_Y;
		}
		default:{	//should never pe possible to reach this branch
			return 0;
		}
		}
	}

	/**
	 * @param coordinate
	 *            the coordinate being checked
	 * @param timeInterval
	 *            the time interval for which the coordinate is being checked
	 * @return if another robot is at the coordinate specified at the time specified
	 */
	private boolean otherRobotAtCoord(Point coordinate, int timeInterval) {
		return false;
	}

	/* returns the direction that the robot needs to travel in next */
	private Action differenceInPose(int currentPose, int targetDir, boolean rotationOnly) {
		Action dir = Action.WAIT;
		int initialDir = findRotation(currentPose, targetDir);
		logger.trace("r " + rotationOnly + " cp " + currentPose + " td " + targetDir + " id " + initialDir);
		if (rotationOnly) {
			initialDir = initialDir + 4;
		}
		switch (initialDir) {
		case 0: {
			dir = Action.FORWARD;
			break;
		}
		case 1: {
			dir = Action.LEFT;
			break;
		}
		case 2: { // this case should not be possible to reach in practice
			dir = Action.WAIT;
			break;
		}
		case 3: {
			dir = Action.RIGHT;
			break;
		}
		case 4: {
			dir = Action.WAIT;
			break;
		}
		case 5: {
			dir = Action.TURN_LEFT;
			break;
		}
		case 6: {
			dir = Action.TURN_180;
			break;
		}
		case 7: {
			dir = Action.TURN_RIGHT;
			break;
		}
		}
		logger.trace(dir);
		return dir;
	}


	/*
	 * produces an integer representation of the difference in direction of the pose
	 * and direction specified
	 */
	private int findRotation(int p, int initialMoveDir) {
		int initialDir = p-initialMoveDir;
		logger.trace("p " + p + " imd " + initialMoveDir + " id " + initialDir);
		if (initialDir < 0) {
			initialDir = initialDir + 4;
		}
		return initialDir;
	}

	/* returns a queue of directions that the robot needs to take */
	private BlockingQueue<Action> generateDirectionsQueue(ArrayList<Integer> tempDirs) {
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		int startPose=0;
		if (tempDirs.size() > 0) {
			startPose = tempDirs.get(0);
		}
		for (int i = 0; i < tempDirs.size(); i++) {
			if (tempDirs.get(i).equals(STILL)) {
				directions.add(Action.WAIT);
			} else {
				logger.debug(startPose);
				Action action = differenceInPose(startPose, tempDirs.get(i), false);
				directions.add(action);
				logger.debug(action);
				switch (action) {
				case LEFT:{
					startPose = startPose-1;
					if (startPose<0) {
						startPose = startPose+4;
					}
					break;
				}
				case RIGHT:{
					startPose = startPose+1;
					if (startPose>3) {
						startPose = startPose-4;
					}
					break;
				}
				}
			}
		}
		return directions;
	}
}