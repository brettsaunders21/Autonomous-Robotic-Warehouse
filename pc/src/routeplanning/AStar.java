package routeplanning;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import interfaces.Action;
import interfaces.Pose;
import lejos.geom.Point;
import routeplanning.astarhelpers.*;
import sun.security.x509.IssuingDistributionPointExtension;

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
	private final static int HOLD = 5;

	private final Map map;

	/**
	 * @param map
	 *            the map that route finding will be carried out on
	 */
	public AStar(Map map) {
		this.map = map;
	}

	
	
	public Route adjustForCollisions(Route r, Route[] rs, int startTime, int numOfInstructionsSent) {
		if (r.getDirections().size()==0) {
			return r;
		}
		int diffFromStart = r.getCoordinatesArray().length-r.getCoordinates().size();
		Action[] directions = new Action[r.getDirections().size()];
		directions = r.getDirections().toArray(directions);
		Point[] coordinates = new Point[r.getCoordinates().size()];
		coordinates = r.getCoordinates().toArray(coordinates);
		int endPointIndex = directions.length-1;
		for (int i = 0; i<directions.length; i++) {
			logger.info(directions[i]);
			if (directions[i].equals(Action.PICKUP)||directions[i].equals(Action.DROPOFF)||directions[i].equals(Action.HOLD)) {
				endPointIndex = i;
				break;
			}
		}
		logger.info(endPointIndex);
		BlockingQueue<Action> dirQ = r.getDirections();
		BlockingQueue<Point> coordQ = r.getCoordinates();
		for (int i = 0; i< endPointIndex; i++) {
			logger.info(dirQ.poll());
			logger.info(coordQ.poll());
		}
		logger.warn(r.getCoordinatesArray()[diffFromStart-1]);
		logger.warn(r.getCoordinatesArray()[diffFromStart]);
		Point coord;
		if (dirQ.peek().equals(Action.HOLD)||dirQ.peek().equals(Action.PICKUP)||dirQ.peek().equals(Action.DROPOFF)) {
			coord = coordQ.peek();
		}
		else {
			coord = coordQ.poll();
			dirQ.poll();
		}
		Route nextStep = generateRoute(r.getCoordinatesArray()[diffFromStart-1], coord, r.getPoseAt(diffFromStart-1), rs, startTime);
		logger.warn(coordQ.peek());
		if (r.getCoordinates().size()==0) {	
			r = nextStep;
		}
		else {
			logger.warn(r.getCoordinates().peek());
			logger.warn(r.getDirections().peek());
			r = new Route(nextStep, r);				
		}
		return r;
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
		logger.fatal("map " + map.isPassable(currentPosition));
		logger.fatal(tempMap.isPassable(currentPosition));
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			logger.fatal(currentPosition);
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		//
		if (currentPosition.equals(targetPosition)) {
			throw new UnsupportedOperationException("Cannot generate route that starts and ends at the same location");
		}

		// finds the shortest route between the two points
		BacktrackNeededException[] blockages = new BacktrackNeededException[0];
		TempRouteInfo ti = null;
		try {
			ti = routeFindingAlgo(currentPosition, targetPosition, tempMap, routes, myStartTime,
				currentPosition, blockages);
		}
		catch (StackOverflowError e) {
			logger.debug(currentPosition +" "+targetPosition + " " + myStartTime);
			System.exit(-1);
		}
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
			int myStartTime, Point myStartCoord, BacktrackNeededException[] blockages) {
		
		while (true) {
			// finds the shortest route through the map
			ConcurrentMap<Point, RouteCoordInfo> visitedPoints = findShortestRoute(tempMap, startPosition,
					targetPosition, blockages);

			// produces the route information in the correct order
			TempRouteInfo tempInfo = produceInOrderRouteInfo(startPosition, targetPosition, visitedPoints);

			logger.fatal("");
			logger.fatal(tempMap.isPassable(new Point(5,0)));
			
			try {
				return addRobotAvoidInstructions(tempInfo, routes, myStartTime, myStartCoord, tempMap);
			} catch (BacktrackNeededException e) {
				ArrayList<BacktrackNeededException> blockedInfo = new ArrayList<BacktrackNeededException>();
				for (BacktrackNeededException b : blockages) {
					blockedInfo.add(b);
				}
				Map updatedMap;
				if (!e.isIndefinitelyBlocked()) {
					blockedInfo.add(e);
					updatedMap = tempMap;
				}
				else {
					updatedMap = tempMap.clone(e.getBlockedPoint());
				}
				blockages = blockedInfo.toArray(blockages);
				return routeFindingAlgo(startPosition, targetPosition, updatedMap, routes, myStartTime, myStartCoord,
						blockages);
			}
		}
	}

	/*
	 * returns the ConcurrentMap which holds information on the points which can be
	 * used to obtain the shortest route
	 */
	private ConcurrentMap<Point, RouteCoordInfo> findShortestRoute(Map tempMap, Point startPosition,
			Point targetPosition, BacktrackNeededException[] blockages) {

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
		int routeTime = 0;
		// loops until the test coordinate is at the target coordinates
		while (!atDestination) {
			// returns a list of all points directly adjacent to the current point that are
			// valid points to move to
			Point[] points = getValidSurroundingPoints(tempMap, testPoint, blockages, routeTime);
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

			routeTime++;

			logger.info(testPoint);
			logger.info(visitedPoints.get(testPoint).getTotalPointDist());
		}
		return visitedPoints;
	}

	/*
	 * returns a list of all points directly adjacent to the current point that are
	 * valid points to move to
	 */
	private Point[] getValidSurroundingPoints(Map tempMap, Point testPoint, BacktrackNeededException[] blockages,
			int routeTime) {
		ArrayList<Point> points = new ArrayList<Point>(4);
		Point[] testDirs = new Point[] { new Point(testPoint.x - 1, testPoint.y), // left
				new Point(testPoint.x, testPoint.y - 1), // up
				new Point(testPoint.x + 1, testPoint.y), // right
				new Point(testPoint.x, testPoint.y + 1) }; // down

		// indexes: [0]:left, [1]:right, [2]:up, [3]:down
		for (int allDirs = 0; allDirs < 4; allDirs++) {
			// for each point within the bounds of the map
			logger.warn(testPoint);
			logger.warn(tempMap);
			logger.warn(testDirs);
			if (tempMap.withinMapBounds(testDirs[allDirs])) {
				// if test point is passable and makes the route shorter than the
				if (tempMap.isPassable(testDirs[allDirs])) {
					boolean blocked = false;
					for (BacktrackNeededException blockage : blockages) {
						if (blockage.getBlockedPoint().equals(testDirs[allDirs])
								&& blockage.getRelTime() == routeTime) {
							blocked = true;
							break;
						}
					}
					if (!blocked) {
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
	private TempRouteInfo addRobotAvoidInstructions(TempRouteInfo tempInfo, Route[] routes, int myStartTime,
			Point myStartCoord, Map tempMap) throws BacktrackNeededException {
		ArrayList<Point> tempCoords = tempInfo.getCoords();
		ArrayList<Integer> tempDirs = tempInfo.getDirs();
		ArrayList<Point> holdCoords = new ArrayList<Point>();
		boolean[] heldAlreadyFound = new boolean[routes.length];
		for (int i = 0; i<heldAlreadyFound.length; i++) {
			heldAlreadyFound[i] = false;
		}
		for (int routeIndex = 0; routeIndex<routes.length; routeIndex++) {
			if (!heldAlreadyFound[routeIndex]) {
			Action[] directions = routes[routeIndex].getDirectionArray();
			Point[] coords = routes[routeIndex].getCoordinatesArray();
			for (int i = 0; i<directions.length; i++) {
				if (directions[i].equals(Action.PICKUP)||directions[i].equals(Action.DROPOFF)||directions[i].equals(Action.HOLD)) {
					holdCoords.add(coords[i]);
					heldAlreadyFound[routeIndex] = true;
					break;
				}
			}
			}
			else {
				boolean noFurtherChecks = true;
				for (int i = 0; i<heldAlreadyFound.length; i++) {
					if (!heldAlreadyFound[i]) {
						noFurtherChecks = false;
					}
				}
				if (noFurtherChecks) {
					break;
				}
			}
		}
		boolean holdAdded = false;
		int i = 0;
		while (i < tempCoords.size()) {
			for (Point p : holdCoords) {
				if (p.equals(tempCoords.get(i))) {
					if (i+1==tempCoords.size()) {
						if (i > 0) {
							tempCoords.add(i, tempCoords.get(i - 1));
						} else {
							tempCoords.add(i, myStartCoord);
						}
						tempDirs.add(i, HOLD);
						holdAdded = true;
						break;
					}
					else if (tempMap.isPassable(p)){
						logger.fatal(p);
						throw new BacktrackNeededException(p, -1);
					}
				}
			}
			if (holdAdded) {
				break;
			}
			for (Route route : routes) {
				int startTime = route.getStartTime();
				int endTime = route.getStartTime() + route.getLength();
				int currentTime = myStartTime + i;
				if (currentTime >= startTime && currentTime < endTime) {
					int relativeTime = currentTime - startTime;
					Point theirCoord = route.getCoordinatesArray()[relativeTime];
					Point myCoord = tempCoords.get(i);
					if (theirCoord.equals(myCoord)) {
						if (i > 0) {
							tempCoords.add(i, tempCoords.get(i - 1));
						} else {
							tempCoords.add(i, myStartCoord);
						}
						tempDirs.add(i, STILL);
					}
					if (i > 0) {
						headOnCollisionCheck(tempCoords.get(i - 1), myCoord, relativeTime, i, route);
					} else {
						headOnCollisionCheck(myStartCoord, myCoord, relativeTime, i, route);
					}
				}
			}
			i++;
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}

	private void headOnCollisionCheck(Point thisPoint, Point nextPoint, int theirTime, int myTime, Route route)
			throws BacktrackNeededException {
		Point[] theirPoints = route.getCoordinatesArray();
		Point theirNextPoint = theirPoints[theirTime];
		Point theirPointNow;
		if (theirTime > 0) {
			theirPointNow = theirPoints[theirTime - 1];
		} else {
			theirPointNow = route.getStartPoint();
		}
		if (theirPointNow.equals(nextPoint) && theirNextPoint.equals(thisPoint)) {
			throw new BacktrackNeededException(nextPoint, myTime);
		}
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
			}
			else if (tempDirs.get(i).equals(HOLD)) {
				directions.add(Action.HOLD);
			}
			else {
				logger.debug(startPose);
				Action action = generateDirectionInstruction(startPose, tempDirs.get(i));
				directions.add(action);
				logger.debug(action);
				switch (action) {
				case RIGHT: {
					startPose = startPose - 1;
					if (startPose < 0) {
						startPose = startPose + 4;
					}
					break;
				}
				case LEFT: {
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
			dir = Action.RIGHT;
			break;
		}
		case 2: {
			dir = Action.BACKWARD;
			break;
		}
		case 3: {
			dir = Action.LEFT;
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