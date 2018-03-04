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
	 * @param map the map that route finding will be carried out on
	 */
	public AStar(Map map) {
		this.map = map;
	}

	/**@param currentPosition
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
		int length = 0;

		// checks that both points are within the map
		if (!tempMap.withinMapBounds(currentPosition) || !tempMap.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		// checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition) || !tempMap.isPassable(targetPosition)) {
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		
		TempRouteInfo ti = routeFindingAlgo(currentPosition, targetPosition, tempMap);
		// moves final coordinates to queue and finalises the length of the route
		coordinates.addAll(ti.getCoords());
		length = ti.getTimeInterval();
		BlockingQueue<Action> directions = generateDirectionsQueue(ti.getDirs());
		return new Route(coordinates, directions, length, startingPose,
				differenceInPose(poseToInt(startingPose), ti.getDirs().get(0), true));
	}

	/*The actual route finding code*/
	private TempRouteInfo routeFindingAlgo(Point startPosition, Point targetPosition, Map tempMap) {
		Point testPoint = startPosition.clone();
		ArrayList<Point> visitedCoords = new ArrayList<Point>();
		ArrayList<Point> traversedCoords = new ArrayList<Point>();
		ConcurrentMap<Point, RouteCoordInfo> visitedPoints = new ConcurrentHashMap<>();
		boolean atDestination = startPosition.equals(targetPosition);
		
		visitedPoints.put(startPosition ,new RouteCoordInfo(startPosition, startPosition, startPosition.distance(targetPosition), 0));
		traversedCoords.add(startPosition);
		visitedCoords.add(startPosition);
				
		// loops until coordinates are at the target coordinates
		while (!atDestination) {
			double nextDist = Double.POSITIVE_INFINITY;
			Point[] testDirs = new Point[] { new Point(testPoint.x - 1, testPoint.y), // left
					new Point(testPoint.x, testPoint.y - 1), // up
					new Point(testPoint.x + 1, testPoint.y), // right
					new Point(testPoint.x, testPoint.y + 1) }; // down

			// indexes: [0]:left, [1]:right, [2]:up, [3]:down
			for (int allDirs = 0; allDirs < 4; allDirs++) {
				// for each point within the bounds of the map
				if (tempMap.withinMapBounds(testDirs[allDirs])) {
					// if test point is passable and makes the route shorter than the
					if (tempMap.isPassable(testDirs[allDirs])){
						Point p = testDirs[allDirs];
						if (!visitedCoords.contains(p)) {
							visitedPoints.putIfAbsent(p, new RouteCoordInfo(p, testPoint, p.distance(targetPosition), visitedPoints.get(testPoint).getDistFromStart()+1));
							visitedCoords.add(p);
						}
					}
				}
			}
			
			for (int i = 0; i<visitedCoords.size(); i++) {
				Point p = visitedCoords.get(i);
				RouteCoordInfo pInfo = visitedPoints.get(p);
				if (!traversedCoords.contains(p)) {
					if (pInfo.getTotalPointDist()<nextDist) {
						nextDist = pInfo.getTotalPointDist();
						testPoint = p;
						traversedCoords.add(p);
					}
				}
			}
			logger.info(testPoint);
			logger.info(visitedPoints.get(testPoint).getTotalPointDist());
			atDestination = testPoint.equals(targetPosition);
		}
		
		
		boolean atStart = startPosition.equals(testPoint);
		ArrayList<Point> tempCoords = new ArrayList<Point>(visitedPoints.get(targetPosition).getDistFromStart());
		ArrayList<Integer> tempDirs = new ArrayList<Integer>(visitedPoints.get(targetPosition).getDistFromStart());
		for (int i = 0; i<visitedPoints.get(targetPosition).getDistFromStart(); i++) {
			tempCoords.add(new Point(-1,-1));
			tempDirs.add(-1);
		}
		while (!atStart){
			RouteCoordInfo pInfo = visitedPoints.get(testPoint);
			tempCoords.set(pInfo.getDistFromStart()-1, testPoint);
			testPoint = pInfo.getOriginPoint();
			atStart = startPosition.equals(testPoint);
			tempDirs.set(pInfo.getDistFromStart()-1, getDirection(pInfo));
		}
		
		int i = 0;
		while (i<tempCoords.size()) {
			if (otherRobotAtCoord(tempCoords.get(i), i)) {
				tempCoords.add(i, tempCoords.get(i));
				tempDirs.add(i, STILL);
			}
			i++;
		}
		return new TempRouteInfo(tempCoords.size(), tempCoords, tempDirs);
	}
	
	/*determines which direction has to be travelled between the coordinate and its parent*/
	private int getDirection(RouteCoordInfo pInfo) {
		int direction;
		Point difference = pInfo.getThisPoint().subtract(pInfo.getOriginPoint());
		if (difference.x!=0d) {
			if (difference.x==1d) {
				direction = POS_X;
			}
			else {
				direction = NEG_X;
			}
		}
		else {
			if(difference.y==1d) {
				direction = POS_Y;
			}
			else {
				direction = NEG_Y;
			}
		}
		return direction;
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

	/*if another robot is at the coordinate specified at the time specified then true is returned*/
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
				default:{
					break;
				}
				}
			}
		}
		return directions;
	}
}