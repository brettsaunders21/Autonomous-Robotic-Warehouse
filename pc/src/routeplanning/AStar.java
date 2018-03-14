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

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 */

public class AStar {
	final static Logger logger = Logger.getLogger(AStar.class);

	/*
	 * Directions for route planning, for global directions see {@link
	 * interfaces.Direction}
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
	 * @param startingPose
	 *            the axis along which the robot is facing before starting the route
	 * @param routes
	 *            an array containing all the routes of the other robots active
	 *            within the map
	 * @param myStartTime
	 *            the time at which the currently generated route will begin
	 *            executing
	 * @return shortest route between the two points avoiding obstacles
	 * @throws IllegalArgumentException
	 *             one or both coordinates are not within the map area
	 * @throws IllegalStateException
	 *             one or both coordinates are in an inaccessible area
	 * @throws UnsupportedOperationException
	 *             cannot generate a route that starts and ends at the same location
	 */
	public Route generateRoute(Point currentPosition, Point targetPosition, Pose startingPose, Route[] routes,
			int myStartTime) {
		// creates a clone of the existing map to prevent accidental overwriting of the
		// map
		Map tempMap = map.clone();
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();

		// checks that both points are within the map
		if (!tempMap.withinMapBounds(currentPosition) || !tempMap.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		//
		if (currentPosition.equals(targetPosition)) {
			throw new UnsupportedOperationException("Cannot generate route that starts and ends at the same location");
		}


		// holds information about each point that is currently accessible to route find
		// on
		ConcurrentMap<Point, RouteCoordInfo> visitedPoints = new ConcurrentHashMap<Point, RouteCoordInfo>();
		// finds the shortest route between the two points
		TempRouteInfo ti = routeFindingAlgo(currentPosition, targetPosition, tempMap, routes, myStartTime, visitedPoints, new ArrayList<Obstruction>());

		// moves final coordinates to queue and finalises the length of the route
		coordinates.addAll(ti.getCoords());

		// uses the list of coordinates to produce a queue of directions
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		directions.add(generateDirectionInstruction(poseToInt(startingPose), ti.getDirs().get(0)));
		BlockingQueue<Action> temp = generateDirectionsQueue(ti.getDirs());
		temp.remove();
		directions.addAll(temp);
		return new Route(coordinates, directions, startingPose, myStartTime, currentPosition);
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
	private TempRouteInfo routeFindingAlgo(Point startPosition, Point targetPosition, Map tempMap, Route[] routes,
			int myStartTime, ConcurrentMap<Point, RouteCoordInfo> visitedPoints, ArrayList<Obstruction> obstructions) {
		// finds the shortest route through the map
		visitedPoints = findShortestRoute(tempMap, startPosition, targetPosition, visitedPoints, obstructions);

		// produces the route information in the correct order
		TempRouteInfo tempInfo = produceInOrderRouteInfo(startPosition, targetPosition, visitedPoints);

		TempRouteInfo finalInfo;
		try {
			finalInfo = addRobotAvoidInstructions(tempInfo, routes, myStartTime, startPosition);
		}
		catch (BacktrackNeededException e) {
			logger.warn(e.getBlockedPoint());
			visitedPoints = removeAllChildren(e.getPreviousPoint(), visitedPoints);
			Obstruction obstruction = new Obstruction(e.getTime(), e.getBlockedPoint());
			obstructions.add(obstruction);
			finalInfo = routeFindingAlgo(e.getPreviousPoint(), targetPosition, tempMap.clone(), routes, myStartTime, visitedPoints, obstructions);
		}
		return finalInfo;
	}

	
	
	/*
	 * returns the ConcurrentMap which holds information on the points which can be
	 * used to obtain the shortest route
	 */
	private ConcurrentMap<Point, RouteCoordInfo> findShortestRoute(Map tempMap, Point startPosition,
			Point targetPosition, ConcurrentMap<Point, RouteCoordInfo> visitedPoints, ArrayList<Obstruction> obstructions) {

		// creates arrays to hold all points that are accessible so far and points that
		// have already been traversed
		ArrayList<Point> traversedCoords = new ArrayList<Point>();
		ArrayList<Point> visitedCoords = new ArrayList<Point>();

		// adds start coordinate to the lists
		visitedPoints.put(startPosition,
				new RouteCoordInfo(startPosition, startPosition, startPosition.distance(targetPosition), 0, 0));
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
			Point[] points = getValidSurroundingPoints(tempMap, testPoint, obstructions, visitedPoints.get(testPoint).getTimeOnRoute()+1);
			for (int i = 0; i < points.length; i++) {
				Point p = points[i];
				// adds the information about each point to corresponding arrays if it is not
				// already in the arrays
				if (!visitedCoords.contains(p)) {
					visitedPoints.putIfAbsent(p, new RouteCoordInfo(p, testPoint, p.distance(targetPosition),
							visitedPoints.get(testPoint).getDistFromStart() + 1, visitedPoints.get(testPoint).getTimeOnRoute()+1));
					Point[] children = visitedPoints.get(visitedPoints.get(p).getOriginPoint()).getChildren();
					boolean alreadyPresent = false;
					for (int child = 0; child< children.length; child++) {
						if (p.equals(children[child])) {
							alreadyPresent = true;
							break;
						}
					}
					if (!alreadyPresent) {
						Point[] newChildren = new Point[children.length +1];
						for (int adder = 0; adder<children.length; adder++) {
							newChildren[adder] = children[adder];
						}
						newChildren[newChildren.length-1] = p;
					}
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
	private Point[] getValidSurroundingPoints(Map tempMap, Point testPoint, ArrayList<Obstruction> obstructions, int relativeTime) {
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
					boolean tempBlocked = false;
					for (int i = 0; i< obstructions.size(); i++) {
						if (relativeTime==obstructions.get(i).getTime()) {
							if (obstructions.get(i).getCoordinate().equals(testDirs[allDirs])) {
								tempBlocked = true;
							}
						}
					}
					if (!tempBlocked) {
						points.add(testDirs[allDirs]);
					}
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
			tempDirs.set(pInfo.getDistFromStart() - 1, getDirection(pInfo.getOriginPoint(), pInfo.getThisPoint()));
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}

	
	
	/*
	 * determines which direction has to be travelled between the coordinate and its
	 * parent
	 */
	public static int getDirection(Point firstPoint, Point secondPoint) {
		int direction;
		Point difference = secondPoint.subtract(firstPoint);
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
	private TempRouteInfo addRobotAvoidInstructions(TempRouteInfo tempInfo, Route[] routes, int myStartTime, Point myStartCoord) throws BacktrackNeededException {
		ArrayList<Point> tempCoords = tempInfo.getCoords();
		ArrayList<Integer> tempDirs = tempInfo.getDirs();
		int i = 0;
		while (i < tempCoords.size()) {
			logger.debug("length "+routes.length);
			for (int robot = 0; robot < routes.length; robot++) {
				logger.debug("robot " +robot);
				int otherRobotTime = (myStartTime + i) - routes[robot].getStartTime();
				if ((otherRobotTime >= 0)&& (otherRobotTime<routes[robot].getLength())) {
					logger.debug("otherTime "+otherRobotTime);
					logger.debug("this "+tempCoords.get(i) + " other "+routes[robot].getCoordinatesArray()[otherRobotTime]);
					if (otherRobotAtCoord(tempCoords, i, myStartCoord, routes[robot].getCoordinatesArray(), otherRobotTime, routes[robot].getStartPoint())) {
						if (i>0) {
							tempCoords.add(i, tempCoords.get(i-1));
						}
						else {
							tempCoords.add(i, myStartCoord);
						}
						tempDirs.add(i, STILL);
					}
				}
			}
			i++;
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}

	
	
	/*
	 * if another robot is at the coordinate specified at the time specified then
	 * true is returned
	 */
	private boolean otherRobotAtCoord(ArrayList<Point> myCoords, int myTime, Point myStart, Point[] theirCoords, int theirTime, Point theirStart) throws BacktrackNeededException{
		boolean inValidMove = false;
		if (myCoords.get(myTime).equals(theirCoords[theirTime])){
			inValidMove = true;
		}
		logger.debug("m "+myTime + " t "+theirTime);
		if(myTime>0) {
			if (theirTime>0) {
				if (myCoords.get(myTime).equals(theirCoords[theirTime-1])&&(myCoords.get(myTime-1).equals(theirCoords[theirTime]))) {
					throw new BacktrackNeededException("NO-ERROR", myCoords.get(myTime), myCoords.get(myTime-1), myTime);
				}
			}
			else {
				if (myCoords.get(myTime).equals(theirStart)&&(myCoords.get(myTime-1).equals(theirCoords[theirTime]))) {
					throw new BacktrackNeededException("NO-ERROR", myCoords.get(myTime), myCoords.get(myTime-1), myTime);
				}
			}
		}
		else {
			if (theirTime>0) {
				if (myCoords.get(myTime).equals(theirCoords[theirTime-1])&&(myStart.equals(theirCoords[theirTime]))) {
					throw new BacktrackNeededException("NO-ERROR", myCoords.get(myTime), myStart, myTime);
				}
			}
			else {
				if (myCoords.get(myTime).equals(theirStart)&&(myStart.equals(theirCoords[theirTime]))) {
					throw new BacktrackNeededException("NO-ERROR", myCoords.get(myTime), myStart, myTime);
				}
			}
		}
		return inValidMove;
	}

	
	
	private ConcurrentMap<Point, RouteCoordInfo> removeAllChildren(Point p, ConcurrentMap<Point, RouteCoordInfo> visitedPoints){
		Point[] children = visitedPoints.get(p).getChildren();
		for (int child = 0; child<children.length; child++) {
			visitedPoints = removeAllChildren(children[child], visitedPoints);
		}
		visitedPoints.remove(p);
		return visitedPoints;
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
				Action action = generateDirectionInstruction(startPose, tempDirs.get(i));
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

	
	
	/*
	 * generates the correct direction instruction to match the difference in the
	 * orientation of the robot relative to its target direction
	 */
	public static Action generateDirectionInstruction(int currentPose, int targetDir) {
		Action dir = Action.WAIT;
		int initialDir = findRotation(currentPose, targetDir);
		logger.trace(" cp " + currentPose + " td " + targetDir + " id " + initialDir);
		switch (initialDir) {
		case 0: {
			dir = Action.FORWARD;
			break;
		}
		case 1: {
			dir = Action.LEFT;
			break;
		}
		case 2: {
			dir = Action.BACKWARD;
			break;
		}
		case 3: {
			dir = Action.RIGHT;
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
	private static int findRotation(int pose, int initialMoveDir) {
		int initialDir = pose - initialMoveDir;
		logger.trace("p " + pose + " imd " + initialMoveDir + " id " + initialDir);
		if (initialDir < 0) {
			initialDir = initialDir + 4;
		}
		return initialDir;
	}

	
	
	/* Converts from pose to int */
	public static int poseToInt(Pose p) {
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

	
	
}