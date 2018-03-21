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

	/**regenerates the route between the current coordinate and the next hold/pickup/dropoff point
	 * @param r the route to be regenerated
	 * @param rs the other existing routes currently being executed
	 * @param startTime the time at which the next route is to start being executed
	 * @return the updated route which avoids collisions with other robots*/
	public Route adjustForCollisions(Route r, Route[] rs, int startTime) {
		//if no directions remain a new job needs to be assigned
		if (r.getDirections().size() == 0) {
			return r;
		}
		//how many instructions have been executed since last route generation
		int diffFromStart = r.getCoordinatesArray().length - r.getCoordinates().size();
		//remaining directions
		Action[] directions = new Action[r.getDirections().size()];
		directions = r.getDirections().toArray(directions);
		//remaining coordinates
		Point[] coordinates = new Point[r.getCoordinates().size()];
		coordinates = r.getCoordinates().toArray(coordinates);
		
		//find the next hold/pickup/dropoff coordinate
		int endPointIndex = directions.length - 1;
		for (int i = 0; i < directions.length; i++) {
			if (directions[i].equals(Action.PICKUP) || directions[i].equals(Action.DROPOFF)
					|| directions[i].equals(Action.HOLD)) {
				endPointIndex = i;
				break;
			}
		}
		
		//remove all directions and coordinates up to next hold/pickup/dropoff coordinate
		BlockingQueue<Action> dirQ = r.getDirections();
		BlockingQueue<Point> coordQ = r.getCoordinates();
		for (int i = 0; i < endPointIndex; i++) {
			dirQ.poll();
			coordQ.poll();
		}
		
		//checks if the end of the route is reached or if a hold/pickup/dropoff coordinate is reached
		Point coord;
		if (dirQ.peek().equals(Action.HOLD) || dirQ.peek().equals(Action.PICKUP)
				|| dirQ.peek().equals(Action.DROPOFF)) {
			coord = coordQ.peek();
			Level currentLevel = logger.getLevel();
			logger.setLevel(Level.DEBUG);
			logger.debug(coordQ.peek());
			logger.setLevel(currentLevel);
		} else {
			coord = coordQ.poll();
			Level currentLevel = logger.getLevel();
			logger.setLevel(Level.DEBUG);
			logger.debug(dirQ.poll(););
			logger.setLevel(currentLevel);
		}
		
		//regenerates the next section of the route
		Route nextStep = generateRoute(r.getCoordinatesArray()[diffFromStart - 1], coord,
				r.getPoseAt(diffFromStart - 1), rs, startTime);
		
		//if the remaining route has no coordinates then the route just generated is returned, otherwise the routes are concatenated

		if (r.getCoordinates().size() == 0) {
			r = nextStep;
		} else {
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
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			logger.fatal(currentPosition + " " + tempMap.isPassable(currentPosition));
			logger.fatal(targetPosition + " " + tempMap.isPassable(targetPosition));
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		//
		if (currentPosition.equals(targetPosition)) {
			throw new UnsupportedOperationException("Cannot generate route that starts and ends at the same location");
		}

		// finds the shortest route between the two points
		TempRouteInfo ti = null;
		ti = routeFindingAlgo(currentPosition, targetPosition, tempMap, routes, myStartTime, currentPosition, startingPose);
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
			int myStartTime, Point myStartCoord, Pose startPose) {

		while (true) {
			// finds the shortest route through the map
			ConcurrentMap<Point, RouteCoordInfo> visitedPoints = findShortestRoute(tempMap, startPosition,
					targetPosition, routes, myStartTime);

			// produces the route information in the correct order
			TempRouteInfo tempInfo = produceInOrderRouteInfo(startPosition, targetPosition, visitedPoints, startPose);

			try {
				return addRobotAvoidInstructions(tempInfo, routes, myStartTime, myStartCoord, tempMap);
			} catch (BacktrackNeededException e) {
				Map updatedMap = tempMap.clone(e.getBlockedPoint());
				return routeFindingAlgo(startPosition, targetPosition, updatedMap, routes, myStartTime, myStartCoord, startPose);
			}
		}
	}

	/*
	 * returns the ConcurrentMap which holds information on the points which can be
	 * used to obtain the shortest route
	 */
	private ConcurrentMap<Point, RouteCoordInfo> findShortestRoute(Map tempMap, Point startPosition,
			Point targetPosition, Route[] routes, int myStartTime) {

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
			Point[] points = getValidSurroundingPoints(tempMap, testPoint, routes, routeTime, myStartTime);
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

			logger.trace(testPoint);
			logger.trace(visitedPoints.get(testPoint).getTotalPointDist());
		}
		return visitedPoints;
	}

	/*
	 * returns a list of all points directly adjacent to the current point that are
	 * valid points to move to
	 */
	private Point[] getValidSurroundingPoints(Map tempMap, Point testPoint, Route[] routes, int routeTime,
			int myStartTime) {
		ArrayList<Point> points = new ArrayList<Point>(4);
		Point[] testDirs = new Point[] { new Point(testPoint.x - 1, testPoint.y), // left
				new Point(testPoint.x, testPoint.y - 1), // up
				new Point(testPoint.x + 1, testPoint.y), // right
				new Point(testPoint.x, testPoint.y + 1) }; // down

		logger.trace(testPoint);
		// indexes: [0]:left, [1]:right, [2]:up, [3]:down
		for (int allDirs = 0; allDirs < 4; allDirs++) {
			// for each point within the bounds of the map
			if (tempMap.withinMapBounds(testDirs[allDirs])) {
				// if test point is passable and makes the route shorter than the
				if (tempMap.isPassable(testDirs[allDirs])) {
					boolean blocked = false;
					for (Route route : routes) {
						//finds the relative time in each other robots route
						int relativeTime = (route.getStartTime() - myStartTime) + routeTime;
						int endTime = route.getLength();

						if (relativeTime >= 0 && relativeTime < endTime) {
							//if a head on collision occurs then this point is not valid to move to from this point
							if (headOnCollisionCheck(testPoint, testDirs[allDirs], relativeTime, routeTime, route)) {
								blocked = true;
								break;
							}
						}
					}
					if (!blocked) {
						logger.trace(testDirs[allDirs]);
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
		Point nextPoint = null;
		for (int i = 0; i < visitedCoords.size(); i++) {
			Point p = visitedCoords.get(i);
			logger.trace("v " + p);
			RouteCoordInfo pInfo = visitedPoints.get(p);
			//if the current point has not already been traversed then it is considered
			if (!traversedCoords.contains(p)) {
				logger.trace("t " + p);
				//if the current point is the closest point to the destination then it is marked as the next point
				if (pInfo.getTotalPointDist() < nextDist) {
					nextPoint = p;
					nextDist = pInfo.getTotalPointDist();
				}
			}
		}
		traversedCoords.add(nextPoint);
		return nextPoint;
	}

	/*
	 * returns a TempRouteInfo object which contains two ArrayLists holding the
	 * coordinates and directions in the correct order
	 */
	private TempRouteInfo produceInOrderRouteInfo(Point startPosition, Point targetPosition,
			ConcurrentMap<Point, RouteCoordInfo> visitedPoints, Pose startPose) {
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
			if (!pInfo.getOriginPoint().equals(pInfo.getThisPoint())) {
				tempDirs.set(pInfo.getDistFromStart() - 1, getDirection(pInfo.getOriginPoint(), pInfo.getThisPoint()));
			}
			else {
				tempDirs.set(pInfo.getDistFromStart()-1, poseToInt(startPose));
			}
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
		int i = 0;
		while (i < tempCoords.size()) {
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
				}
			}
			i++;
		}
		return new TempRouteInfo(tempCoords, tempDirs);
	}

	private boolean headOnCollisionCheck(Point thisPoint, Point nextPoint, int theirTime, int myTime, Route route) {
		Point[] theirPoints = route.getCoordinatesArray();
		Point theirNextPoint = theirPoints[theirTime];
		Point theirPointNow;
		if (theirTime > 0) {
			theirPointNow = theirPoints[theirTime - 1];
		} else {
			theirPointNow = route.getStartPoint();
		}
		boolean collision = false;
		if (theirPointNow.equals(nextPoint) && theirNextPoint.equals(thisPoint)) {
			collision = true;
		}
		return collision;
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
				logger.trace(startPose);
				Action action = generateDirectionInstruction(startPose, tempDirs.get(i));
				directions.add(action);
				logger.trace(action);
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