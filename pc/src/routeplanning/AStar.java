package routeplanning;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import interfaces.Action;
import interfaces.Pose;
import lejos.geom.Point;
import routeplanning.astarhelpers.*;
import utilities.Utilities;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 */

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
	 * @param map
	 *            the map that route finding will be carried out on
	 */
	public AStar(Map map) {
		this.map = map;
	}

	/**
	 * @param currentPosition
	 *            the starting coordinate of the route
	 * @param targetPosition
	 *            the target coordinate of the route
	 * @param startingPose the axis along which the robot is facing before starting the route
	 * @param routes an array containing all the routes of the other robots active within the map
	 * @param myStartTime the time at which the currently generated route will begin executing
	 * @return shortest route between the two points avoiding obstacles
	 * @throws IllegalArgumentException
	 *             one or both coordinates are not within the map area
	 * @throws IllegalStateException
	 *             one or both coordinates are in an inaccessible area
	 */
	public Route generateRoute(Point currentPosition, Point targetPosition, Pose startingPose, Route[] routes, int myStartTime) {
		// creates a clone of the existing map to prevent accidental overwriting of the
		// map
		Map tempMap = map.clone();
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		int length = 0;

		// checks that both points are within the map
		if (!tempMap.withinMapBounds(currentPosition) || !tempMap.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}

		// finds the shortest route between the two points
		TempRouteInfo ti = routeFindingAlgo(currentPosition, targetPosition, tempMap);

		// moves final coordinates to queue and finalises the length of the route
		coordinates.addAll(ti.getCoords());

		// uses the list of coordinates to produce a queue of directions
		BlockingQueue<Action> directions = generateDirectionsQueue(ti.getDirs());
		return new Route(coordinates, directions, startingPose,
				differenceInPose(poseToInt(startingPose), ti.getDirs().get(0), true), myStartTime);
	}
	

	/*
	 * The actual route finding code
	 * 
	 * @param startPosition the starting point of the route
	 * 
	 * @param targetPosition the end point of the route
	 * 
	 * @param tempMap the map route finding is being carried out on
	 * 
	 * @return the information about the route that is used to produce a route
	 * object
	 */
	private TempRouteInfo routeFindingAlgo(Point startPosition, Point targetPosition, Map tempMap) {
		// finds the shortest route through the map
		ConcurrentMap<Point, RouteCoordInfo> visitedPoints = findShortestRoute(tempMap, startPosition, targetPosition);

		// produces the route information in the correct order
		TempRouteInfo tempInfo = produceInOrderRouteInfo(startPosition, targetPosition, visitedPoints);

		return addRobotAvoidInstructions(tempInfo);
	}
	
	/*
	 * returns the ConcurrentMap which holds information on the points which can be
	 * used to obtain the shortest route
	 */
	private ConcurrentMap<Point, RouteCoordInfo> findShortestRoute(Map tempMap, Point startPosition,
			Point targetPosition) {

		// holds information about each point that is currently accessible to route find
		// on
		ConcurrentMap<Point, RouteCoordInfo> visitedPoints = new ConcurrentHashMap<>();

		// creates arrays to hold all points that are accessible so far and points that
		// have already been traversed
		ArrayList<Point> traversedCoords = new ArrayList<Point>();
		ArrayList<Point> visitedCoords = new ArrayList<Point>();

		// adds start coordinate to the lists
		visitedPoints.put(startPosition,
				new RouteCoordInfo(startPosition, startPosition, startPosition.distance(targetPosition), 0));
		traversedCoords.add(startPosition);
		visitedCoords.add(startPosition);

		// the point currently being traversed
		Point testPoint = startPosition.clone();
		// loop exit condition
		boolean atDestination = startPosition.equals(targetPosition);
		// loops until the test coordinate is at the target coordinates
		while (!atDestination) {
			// returns a list of all points directly adjacent to the current point that are
			// valid points to move to
			Point[] points = getValidSurroundingPoints(tempMap, testPoint);
			for (int i = 0; i < points.length; i++) {
				Point p = points[i];
				// adds the information about each point to corresponding arrays if it is not
				// already in the arrays
				if (!visitedCoords.contains(p)) {
					visitedPoints.putIfAbsent(p, new RouteCoordInfo(p, testPoint, p.distance(targetPosition),
							visitedPoints.get(testPoint).getDistFromStart() + 1));
					visitedCoords.add(p);
				}
			}

			// finds the next closest untraversed point to the target
			testPoint = optimalPoint(visitedCoords, traversedCoords, visitedPoints);
			traversedCoords.add(testPoint);

			// checks if the target poisiton has been reached
			atDestination = testPoint.equals(targetPosition);

			logger.info(testPoint);
			logger.info(visitedPoints.get(testPoint).getTotalPointDist());
		}
		return visitedPoints;
	}

	/*
	 * returns a list of all points directly adjacent to the current point that are
	 * valid points to move to
	 */
	private Point[] getValidSurroundingPoints(Map tempMap, Point testPoint) {
		ArrayList<Point> points = new ArrayList<Point>(4);
		Point[] testDirs = new Point[] { new Point(testPoint.x - 1, testPoint.y), // left
				new Point(testPoint.x, testPoint.y - 1), // up
				new Point(testPoint.x + 1, testPoint.y), // right
				new Point(testPoint.x, testPoint.y + 1) }; // down

		// indexes: [0]:left, [1]:right, [2]:up, [3]:down
		for (int allDirs = 0; allDirs < 4; allDirs++) {
			// for each point within the bounds of the map
			if (tempMap.withinMapBounds(testDirs[allDirs])) {
				// if test point is passable and makes the route shorter than the
				if (tempMap.isPassable(testDirs[allDirs])) {
					points.add(testDirs[allDirs]);
				}
			}
		}
		return points.toArray(new Point[0]);
	}

	/*
	 * given the points that are available, returns the point closest to the target
	 * coordinates that has not yet been visited
	 */
	private Point optimalPoint(ArrayList<Point> visitedCoords, ArrayList<Point> traversedCoords,
			ConcurrentMap<Point, RouteCoordInfo> visitedPoints) {
		// the shortest route found so far
		double nextDist = Double.POSITIVE_INFINITY;
		// the point corresponding to the shortest route
		Point nextPoint = new Point(-1, -1);
		for (int i = 0; i < visitedCoords.size(); i++) {
			Point p = visitedCoords.get(i);
			RouteCoordInfo pInfo = visitedPoints.get(p);
			if (!traversedCoords.contains(p)) {
				if (pInfo.getTotalPointDist() < nextDist) {
					nextPoint = p;
					nextDist = pInfo.getTotalPointDist();
					traversedCoords.add(p);
				}
			}
		}
		return nextPoint;
	}

	/*
	 * returns a TempRouteInfo object which contains two ArrayLists holding the
	 * coordinates and directions in the correct order
	 */
	private TempRouteInfo produceInOrderRouteInfo(Point startPosition, Point targetPosition,
			ConcurrentMap<Point, RouteCoordInfo> visitedPoints) {
		// loop exit condition
		boolean atStart = startPosition.equals(targetPosition);
		Point testPoint = targetPosition;
		ArrayList<Point> tempCoords = new ArrayList<Point>(visitedPoints.get(targetPosition).getDistFromStart());
		ArrayList<Integer> tempDirs = new ArrayList<Integer>(visitedPoints.get(targetPosition).getDistFromStart());

		// initialises both arrays to be the correct size so that they can be inserted
		// into correctly
		for (int i = 0; i < visitedPoints.get(targetPosition).getDistFromStart(); i++) {
			tempCoords.add(new Point(-1, -1));
			tempDirs.add(-1);
		}

		// loops through the route from the destination until the start is reached,
		// producing a correctly ordered ArrayList
		while (!atStart) {
			RouteCoordInfo pInfo = visitedPoints.get(testPoint);
			tempCoords.set(pInfo.getDistFromStart() - 1, testPoint);
			testPoint = pInfo.getOriginPoint();
			atStart = startPosition.equals(testPoint);
			tempDirs.set(pInfo.getDistFromStart() - 1, getDirection(pInfo));
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}
	
	
/*determines which direction has to be travelled between the coordinate and its
	 * parent
	 */
	private int getDirection(RouteCoordInfo pInfo) {
		int direction;
		Point difference = pInfo.getThisPoint().subtract(pInfo.getOriginPoint());
		if (difference.x != 0d) {
			if (difference.x == 1d) {
				direction = POS_X;
			} else {
				direction = NEG_X;
			}
		} else {
			if (difference.y == 1d) {
				direction = POS_Y;
			} else {
				direction = NEG_Y;
			}
		}
		return direction;
	}


	/*
	 * adds additional coordinates and directions to allow the robot to execute a
	 * wait instruction to avoid a collision
	 */
	private TempRouteInfo addRobotAvoidInstructions(TempRouteInfo tempInfo) {
	ArrayList<Point> tempCoords = tempInfo.getCoords();
		ArrayList<Integer> tempDirs = tempInfo.getDirs();
		int i = 0;
		while (i < tempCoords.size()) {
			if (otherRobotAtCoord(tempCoords.get(i), i)) {
				tempCoords.add(i, tempCoords.get(i));
				tempDirs.add(i, STILL);
			}
			i++;
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}
	
	/*
	 * if another robot is at the coordinate specified at the time specified then
	 * true is returned
	 */
	private boolean otherRobotAtCoord(Point coordinate, int timeInterval) {
		return false;
	}

	/* returns a queue of directions that the robot needs to take */
	private BlockingQueue<Action> generateDirectionsQueue(ArrayList<Integer> tempDirs) {
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		int startPose = 0;
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
				case LEFT: {
					startPose = startPose - 1;
					if (startPose < 0) {
						startPose = startPose + 4;
					}
					break;
				}
				case RIGHT: {
					startPose = startPose + 1;
					if (startPose > 3) {
						startPose = startPose - 4;
					}
					break;
				}
				default: {
					break;
				}
				}
			}
		}
		return directions;
	}

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

	/* produces an integer representation of the difference in direction of the pose
	 * and direction specified
	 */
	private int findRotation(int p, int initialMoveDir) {
		int initialDir = p - initialMoveDir;
		logger.trace("p " + p + " imd " + initialMoveDir + " id " + initialDir);
		if (initialDir < 0) {
			initialDir = initialDir + 4;
		}
		return initialDir;
	}
	



	/* Converts from pose to int */

	private int poseToInt(Pose p) {
		switch (p) {
		case POS_X: {
			return POS_X;
		}
		case POS_Y: {
			return POS_Y;
		}
		case NEG_X: {
			return NEG_X;
		}
		case NEG_Y: {
			return NEG_Y;
		}
		default: { // should never be possible to reach this branch
			return 0;
		}
		}
	}
	

	/* returns the direction that the robot needs to travel in next */


	private int firstCommonRouteTime(int[] startTimes) {
		int earliestTime;
		if (startTimes.length > 1) {
			Integer[] temp = new Integer[startTimes.length];
			for (int i = 0; i < startTimes.length; i++) {
				temp[i] = Integer.valueOf(startTimes[i]);
			}
			Integer[] sortedTemp = Utilities.sort(temp, true);

			int[] sorted = new int[startTimes.length];
			for (int i = 0; i < startTimes.length; i++) {
				sorted[i] = Integer.valueOf(sortedTemp[i]);
			}
			earliestTime = startTimes[1];
		}
		else if (startTimes.length == 1){
			earliestTime = startTimes[0];
		}
		else {
			throw new IllegalStateException("Array has no length");
		}
		return earliestTime;
	}
}