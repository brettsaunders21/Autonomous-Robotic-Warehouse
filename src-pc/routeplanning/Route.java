package routeplanning;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import lejos.geom.Point;
import interfaces.Direction;

public class Route{
	final static Logger logger = Logger.getLogger(Route.class);
	private BlockingQueue<Point> coordinates;
	private BlockingQueue<Direction.Directions> directions;
	private int routeLength;
	private final Direction.Pose startPose;
	private final Direction.Rotation orientationAdjust;
	
	/**@param coordinates coordinates in order of traversal
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @param startPose the pose the robot is in when the first direction is executed
	 * @param orientationAdj the initial adjustment needed before execution of directions
	 * @throws IllegalArgumentException route length is negative*/
	public Route(BlockingQueue<Point> coordinates, BlockingQueue<Direction.Directions> directions, int length, Direction.Pose startPose, Direction.Rotation orientationAdj) {
		this.coordinates = coordinates;
		this.directions = directions;
		if (length<0) {
			throw new IllegalArgumentException("Negative route length");
		}
		this.startPose = startPose;
		this.orientationAdjust = orientationAdj;
	}
	
	/**Creates a new route from an existing one by appending the new directions to the end of the existing route
	 * @param currentRoute pre-existing route
	 * @param coordinates coordinates in order of traversal 
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @throws IllegalArgumentException route length is negative*/
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue<Direction.Directions> directions, int length) {
		this.coordinates = currentRoute.getRouteCoordinates();
		this.coordinates.addAll(coordinates);
		this.directions = currentRoute.getDirections();
		this.directions.addAll(directions);
		this.routeLength = currentRoute.getRouteLength()+length;
		this.startPose = currentRoute.getStartPose();
		this.orientationAdjust = currentRoute.getOrientationAdjust();
	}
	
	/**Creates a new route from an existing one by appending the existing route to the new directions
	 * @param currentRoute pre-existing route
	 * @param coordinates coordinates in order of traversal 
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @param startPose the pose the robot is in when the first direction is executed
	 * @param orientationAdj the initial adjustment needed before execution of directions
	 * @throws IllegalArgumentException route length is negative*/
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue<Direction.Directions> directions, int length, Direction.Pose startPose, Direction.Rotation orientationAdj) {
		this.coordinates = coordinates;
		this.coordinates.addAll(currentRoute.getRouteCoordinates());
		this.directions = directions;
		this.directions.addAll(currentRoute.getDirections());
		this.routeLength = currentRoute.getRouteLength()+length;
		this.startPose = startPose;
		this.orientationAdjust = orientationAdj;
	}
	
	/**@return the rotation the robot needs to make before executing any direction instructions*/
	public Direction.Rotation getOrientationAdjust(){
		return orientationAdjust;
	}
	
	/**@return the orientation of the robot before any motion occurs*/
	public Direction.Pose getStartPose(){
		return startPose;
	}
	
	/**@return queue of coordinates in order of traversal*/
	public BlockingQueue<Point> getRouteCoordinates(){
		return coordinates;
	}
	
	/**@return queue of directions in order of execution*/
	public BlockingQueue<Direction.Directions> getDirections(){
		return directions;
	}
	
	/**@return the total length of the route*/
	public int getRouteLength() {
		return routeLength;
	}
}