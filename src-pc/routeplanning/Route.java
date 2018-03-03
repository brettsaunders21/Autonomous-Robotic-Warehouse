package routeplanning;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import lejos.geom.Point;

public class Route{
	final static Logger logger = Logger.getLogger(Route.class);
	private BlockingQueue<Point> coordinates;
	private BlockingQueue directions;
	private int routeLength;
	
	/**@param coordinates coordinates in order of traversal
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @throws IllegalArgumentException route length is negative*/
	public Route(BlockingQueue<Point> coordinates, BlockingQueue directions, int length) {
		this.coordinates = coordinates;
		this.directions = directions;
		if (length<0) {
			throw new IllegalArgumentException("Negative route length");
		}
		this.routeLength = length;
	}
	
	/**Creates a new route from an existing one by appending the new directions to the end of the existing route
	 * @param currentRoute pre-existing route
	 * @param coordinates coordinates in order of traversal 
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @throws IllegalArgumentException route length is negative*/
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue directions, int length) {
		this.coordinates = currentRoute.getRouteCoordinates();
		this.coordinates.addAll(coordinates);
		this.directions = currentRoute.getDirections();
		this.directions.addAll(directions);
		this.routeLength = currentRoute.getRouteLength()+length;		
	}
	
	/**Creates a new route from an existing one by appending the existing route to the new directions
	 * @param currentRoute pre-existing route
	 * @param coordinates coordinates in order of traversal 
	 * @param directions directions in order of execution
	 * @param length the total length of the route
	 * @throws IllegalArgumentException route length is negative*/
	public Route(BlockingQueue<Point> coordinates, BlockingQueue directions, int length, Route currentRoute) {
		this.coordinates = coordinates;
		this.coordinates.addAll(currentRoute.getRouteCoordinates());
		this.directions = directions;
		this.directions.addAll(currentRoute.getDirections());
		this.routeLength = currentRoute.getRouteLength()+length;		
	}
	
	/**@return queue of coordinates in order of traversal*/
	public BlockingQueue<Point> getRouteCoordinates(){
		return coordinates;
	}
	
	/**@return queue of directions in order of execution*/
	public BlockingQueue getDirections(){
		return directions;
	}
	
	/**@return the total length of the route*/
	public int getRouteLength() {
		return routeLength;
	}
}