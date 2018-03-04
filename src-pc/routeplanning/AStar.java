package routeplanning;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import interfaces.Direction;
import lejos.geom.Point;

public class AStar {
	final static Logger logger = Logger.getLogger(AStar.class);
	
	/**Directions for route planning, for global directions see {@link interfaces.Direction} */
	private final static int NEG_X = 0;
	private final static int NEG_Y = 1;
	private final static int POS_X = 2;
	private final static int POS_Y = 3;
	private final static int STILL = 4;
	
	private final Map map;
	
	/**@param m the map that route finding will be carried out on*/
	public AStar(Map map) {
		this.map = map;
	}
	
	/**@param currentPosition the starting coordinate of the route
	 * @param targetPosition the target coordinate of the route
	 * @return shortest route between the two points avoiding obstacles
	 * @throws IllegalArgumentException one or both coordinates are not within the map area
	 * @throws IllegalStateException one or both coordinates are in an inaccessible area*/
	public Route generateRoute(Point currentPosition, Point targetPosition, Direction.Pose startingPose, Route[] routes) {
		Map tempMap = map.clone();
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		ArrayList<Point> tempCoords = new ArrayList<Point>();
		ArrayList<Integer> tempDirs = new ArrayList<Integer>();
		int length = 0;		
		int timeInterval = 0;
		Point testPoint = currentPosition.clone();
		boolean atDestination = currentPosition.equals(targetPosition);
		
		//checks that both points are within the map
		if(!tempMap.withinMapBounds(currentPosition)||!tempMap.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		//checks that both points are at potentially reachable positions
		if (!tempMap.isPassable(currentPosition)||!tempMap.isPassable(targetPosition)) {
			throw new IllegalStateException("One or both coordinates are not in the traversable area");
		}
		
		//loops until coordinates are at the target coordinates
		while (!atDestination) {
			//double absoluteDistance = testPoint.distance(targetPosition);
			double nextDist = Double.POSITIVE_INFINITY;
			int shortestDir=0;
			//Point nextDir;
			Point[] testDirs = new Point[] {
					new Point(testPoint.x-1,testPoint.y),	//left
					new Point(testPoint.x,testPoint.y-1),	//up
					new Point(testPoint.x+1,testPoint.y),	//right
					new Point(testPoint.x,testPoint.y+1)};	//down
			
			//indexes: [0]:left, [1]:right, [2]:up, [3]:down
			for (int allDirs = 0; allDirs<4; allDirs++) {
				//for each point within the bounds of the map
				if (tempMap.withinMapBounds(testDirs[allDirs])) {
					//if test point is passable and makes the route shorter than the 
					if (tempMap.isPassable(testDirs[allDirs])&&(testDirs[allDirs].distance(targetPosition)<nextDist)) {
						nextDist = testDirs[allDirs].distance(targetPosition);
						shortestDir = allDirs;
					}
				}
			}

			//if another robot is already going to be at the coordinates
			if (!otherRobotAtCoord(testDirs[shortestDir], timeInterval)) {
				tempCoords.add(testDirs[shortestDir]);
				tempDirs.add(shortestDir);
				testPoint = testDirs[shortestDir];
				timeInterval++;
			}
			else {
				tempCoords.add(testPoint);
				tempDirs.add(STILL);
				timeInterval++;
			}
			atDestination = testPoint.equals(targetPosition);
		}
		//moves final coordinates to queue and finalises the length of the route
		coordinates.addAll(tempCoords);
		length = timeInterval;
		BlockingQueue<Direction.Directions> directions = generateDirectionsQueue(tempDirs, startingPose);
		return new Route(coordinates, directions, length, startingPose, findInitialRotation(startingPose, tempDirs.get(0)));
	}
	
	/**@param coordinate the coordinate being checked
	 * @param timeInterval the time interval for which the coordinate is being checked
	 * @return if another robot is at the coordinate specified at the time specified*/
	private boolean otherRobotAtCoord(Point coordinate, int timeInterval) {
		return false;
	}
	
	/*returns the direction that the robot needs to travel in next*/
	private Direction.Directions differenceInPose(Direction.Pose currentPose, int targetDir) {
		Direction.Directions dir = Direction.Directions.WAIT;
		int initialDir=findRotation(currentPose, targetDir);
		switch (initialDir) {
		case 0:{
			dir = Direction.Directions.FORWARD;
		}
		case 1:{
			dir = Direction.Directions.LEFT;
		}
		case 2:{	//this case should not be possible to reach in practice
			dir = Direction.Directions.WAIT;
		}
		case 3:{
			dir = Direction.Directions.RIGHT;
		}
		}
		return dir;
	}
	
	/*Returns the rotation needed to make the robot start facing the correct direction*/
	private Direction.Rotation findInitialRotation(Direction.Pose p, int initialMoveDir){
		Direction.Rotation rot = Direction.Rotation.NONE;
		int initialDir=findRotation(p, initialMoveDir);
		switch (initialDir) {
		case 0:{
			rot = Direction.Rotation.NONE;
		}
		case 1:{
			rot = Direction.Rotation.TURN_LEFT;
		}
		case 2:{
			rot = Direction.Rotation.TURN_180;
		}
		case 3:{
			rot = Direction.Rotation.TURN_RIGHT;
		}
		}
		return rot;
	}
	
	/*produces an integer representation of the difference in direction of the pose and direction specified*/
	private int findRotation(Direction.Pose p, int initialMoveDir) {
		int initialDir = 0;
		switch (p) {
		case POS_X:{
			initialDir = POS_X-initialMoveDir;
		}
		case NEG_X:{
			initialDir = NEG_X-initialMoveDir;
		}
		case POS_Y:{
			initialDir = POS_Y-initialMoveDir;
		}
		case NEG_Y:{
			initialDir = NEG_Y-initialMoveDir;
		}
		}
		if (initialDir<0) {
			initialDir = initialDir+4;
		}
		return initialDir;
	}
	
	/*returns a queue of directions that the robot needs to take*/
	private BlockingQueue<Direction.Directions> generateDirectionsQueue(ArrayList<Integer> tempDirs, Direction.Pose startPose) {
		BlockingQueue<Direction.Directions> directions = new LinkedBlockingQueue<Direction.Directions>();
		Direction.Pose currentPose = startPose;
		for (int i=0; i<tempDirs.size(); i++) {
			if (tempDirs.get(i).equals(STILL)) {
				directions.add(Direction.Directions.WAIT);
			}
			else {
				directions.add(differenceInPose(currentPose, tempDirs.get(i)));
			}
		}
		return directions;
	}
}