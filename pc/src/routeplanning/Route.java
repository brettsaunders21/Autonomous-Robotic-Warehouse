package routeplanning;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import lejos.geom.Point;
import interfaces.Action;
import interfaces.Pose;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 */

public class Route {
	final static Logger logger = Logger.getLogger(Route.class);
	private final BlockingQueue<Point> coordinates;
	private final BlockingQueue<Action> directions;
	private final int routeLength;
	private final Pose startPose;
	private final Action orientationAdjust;
	private final Point[] coordsArray;

	/**@param coordinates
	 *            coordinates in order of traversal
	 * @param directions
	 *            directions in order of execution
	 * @param startPose
	 *            the pose the robot is in when the first direction is executed
	 * @param orientationAdj
	 *            the initial adjustment needed before execution of directions
	 * @param myStartTime the time at which the currentRoute is started
	 * @throws IllegalArgumentException
	 *             route length is negative
	 */
	public Route(BlockingQueue<Point> coordinates, BlockingQueue<Action> directions, Pose startPose,
			Action orientationAdj, int myStartTime) {
		this.routeLength = coordinates.size();
		if (routeLength < 0) {
			throw new IllegalArgumentException("Negative route length");
		}
		this.coordinates = coordinates;
		this.directions = directions;
		this.startPose = startPose;
		this.orientationAdjust = orientationAdj;
		this.coordsArray = (Point[]) coordinates.toArray();
	}

	/**@deprecated
	 * Creates a new route from an existing one by appending the new directions to
	 * the end of the existing route
	 * 
	 * @param currentRoute
	 *            pre-existing route
	 * @param coordinates
	 *            coordinates in order of traversal
	 * @param directions
	 *            directions in order of execution
	 * @param length
	 *            the total length of the route
	 * @throws IllegalArgumentException
	 *             route length is negative
	 */
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue<Action> directions) {
		this.coordinates = currentRoute.getRouteCoordinates();
		this.coordinates.addAll(coordinates);
		this.directions = currentRoute.getDirections();
		this.directions.addAll(directions);
		this.routeLength = currentRoute.getRouteLength() + coordinates.size();
		this.startPose = currentRoute.getStartPose();
		this.orientationAdjust = currentRoute.getOrientationAdjust();
		this.coordsArray = (Point[])coordinates.toArray();
	}

	/**@deprecated
	 * Creates a new route from an existing one by appending the existing route to
	 * the new directions
	 * 
	 * @param currentRoute
	 *            pre-existing route
	 * @param coordinates
	 *            coordinates in order of traversal
	 * @param directions
	 *            directions in order of execution
	 * @param length
	 *            the total length of the route
	 * @param startPose
	 *            the pose the robot is in when the first direction is executed
	 * @param orientationAdj
	 *            the initial adjustment needed before execution of directions
	 * @throws IllegalArgumentException
	 *             route length is negative
	 */
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue<Action> directions,
			Pose startPose, Action orientationAdj) {
		this.coordinates = coordinates;
		this.coordinates.addAll(currentRoute.getRouteCoordinates());
		this.directions = directions;
		this.directions.addAll(currentRoute.getDirections());
		this.routeLength = currentRoute.getRouteLength() + coordinates.size();
		this.startPose = startPose;
		this.orientationAdjust = orientationAdj;
		this.coordsArray = (Point[])coordinates.toArray();
	}


	/**@param firstRoute the route which is to form the start of the new route
	 * @param secondRoute the route which is to form the end of the new route
	 * @param middleAction the action to be carried out between the two routes*/
	public Route(Route firstRoute, Route secondRoute, Action middleAction) {
		this.coordinates = firstRoute.getRouteCoordinates();
		this.directions = firstRoute.getDirections();
		this.routeLength = firstRoute.getRouteLength();
		this.startPose = firstRoute.getStartPose();
		this.orientationAdjust = firstRoute.getOrientationAdjust();

		//code to make transition smooth here
		
		this.coordinates.addAll(secondRoute.getRouteCoordinates());
		this.directions.addAll(secondRoute.getDirections());
		this.coordsArray = (Point[]) coordinates.toArray();
	}
	
	/**@param firstRoute the route which is to form the start of the new route
	 * @param secondRoute the route which is to form the end of the new route*/
	public Route(Route firstRoute, Route secondRoute) {
		this.coordinates = firstRoute.getRouteCoordinates();
		this.directions = firstRoute.getDirections();
		this.routeLength = firstRoute.getRouteLength();
		this.startPose = firstRoute.getStartPose();
		this.orientationAdjust = firstRoute.getOrientationAdjust();

		//code to make transition smooth here
		
		this.coordinates.addAll(secondRoute.getRouteCoordinates());
		this.directions.addAll(secondRoute.getDirections());
		this.coordsArray = (Point[]) coordinates.toArray();
	}

	/**@param routes list of routes to be combined, in the order they are to be executed
	 * @throws IllegalArgumentException routes contains no elements*/
	public Route(ArrayList<Route> routes) {
		Route currentRoute;
		if (routes.size()<1) {
			throw new IllegalArgumentException();
		}	
		else {
			currentRoute = routes.get(0);
		}
		for (int i = 1; i<routes.size(); i++) {
			currentRoute = new Route(currentRoute, routes.get(i));
		}
		this.coordinates = currentRoute.getRouteCoordinates();
		this.directions = currentRoute.getDirections();
		this.routeLength = currentRoute.getRouteLength();
		this.startPose = currentRoute.getStartPose();
		this.orientationAdjust = currentRoute.getOrientationAdjust();
		this.coordsArray = (Point[])coordinates.toArray();
	}
	
	/**@param routes list of routes to be combined, in the order they are to be executed
	 * @param actions list of actions in the order they are to be carried out in. One action between each route
	 * @throws IllegalArgumentException routes is not exactly one greater in size than actions
	 * @throws IllegalArgumentException routes contains only one route or less*/
	public Route(ArrayList<Route> routes, ArrayList<Action> actions) {
		if (routes.size() != actions.size()-1) {
			throw new IllegalArgumentException();
		}
		Route currentRoute;
		if (routes.size()<2) {
			throw new IllegalArgumentException();
		}	
		else {
			currentRoute = routes.get(0);
		}
		for (int i = 1; i<routes.size(); i++) {
			currentRoute = new Route(currentRoute, routes.get(i), actions.get(i-1));
		}
		this.coordinates = currentRoute.getRouteCoordinates();
		this.directions = currentRoute.getDirections();
		this.routeLength = currentRoute.getRouteLength();
		this.startPose = currentRoute.getStartPose();
		this.orientationAdjust = currentRoute.getOrientationAdjust();
		this.coordsArray = (Point[])coordinates.toArray();
	}
	
	/**@return the rotation the robot needs to make before executing any direction
	 *         instructions
	 */
	public Action getOrientationAdjust() {
		return orientationAdjust;
	}

	/** @return the orientation of the robot before any motion occurs */
	public Pose getStartPose() {
		return startPose;
	}

	/** @return queue of coordinates in order of traversal */
	public BlockingQueue<Point> getRouteCoordinates() {
		return coordinates;
	}

	/** @return Array of type Point of all coordinates in original route */
	public Point[] getRouteCoordinatesArray() {
		return coordsArray;
	}

	/** @return queue of directions in order of execution */
	public BlockingQueue<Action> getDirections() {
		return directions;
	}

	/** @return the total length of the route */
	public int getRouteLength() {
		return routeLength;
	}

	/**@param relativeTime the time relative to the robot starting the route
	 * @return the pose of the robot at the specified time
	 * @throws IllegalArgumentException the time given is not within the length of the route*/
	public Pose getPoseAt(int relativeTime) {
		return Pose.POS_X;
	}
	
	/**@return the pose of the robot upon completion of the entire route*/
	public Pose getFinalPose() {
		return getPoseAt(routeLength-1);
	}
}