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
	private BlockingQueue<Point> coordinates;
	private BlockingQueue<Action> directions;
	private int routeLength;
	private Pose startPose;
	private Action[] dirsArray;
	private Point[] coordsArray;
	private int myStartTime;
	private Point startPoint;

	/**
	 * @param coordinates
	 *            coordinates in order of traversal
	 * @param directions
	 *            directions in order of execution
	 * @param startPose
	 *            the pose the robot is in when the first direction is executed
	 * @param myStartTime
	 *            the time at which the currentRoute is started
	 * @param startPoint the starting coordinate of this route, before any movement occurs
	 * @throws IllegalArgumentException
	 *             coordinates and directions queues differ in length
	 */
	public Route(BlockingQueue<Point> coordinates, BlockingQueue<Action> directions, Pose startPose, int myStartTime, Point startPoint) {
		if (coordinates.size() != directions.size()) {
			throw new IllegalArgumentException("queues differ in length");
		}
		this.coordinates = coordinates;
		this.directions = directions;
		this.routeLength = coordinates.size();
		this.startPose = startPose;
		Action[] a = new Action[directions.size()];
		this.dirsArray = directions.toArray(a);
		Point[] p = new Point[coordinates.size()];
		this.coordsArray = coordinates.toArray(p);
		this.myStartTime = myStartTime;
		this.startPoint = startPoint;
	}

	/**
	 * @deprecated Creates a new route from an existing one by appending the new
	 *             directions to the end of the existing route
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
		this.coordinates = currentRoute.getCoordinates();
		this.coordinates.addAll(coordinates);
		this.directions = currentRoute.getDirections();
		this.directions.addAll(directions);
		this.routeLength = currentRoute.getLength() + coordinates.size();
		this.startPose = currentRoute.getStartPose();
		this.coordsArray = (Point[]) coordinates.toArray();
		this.dirsArray = (Action[]) directions.toArray();
		this.myStartTime = currentRoute.getStartTime();
		this.startPoint = currentRoute.getStartPoint();
	}

	/**
	 * @deprecated Creates a new route from an existing one by appending the
	 *             existing route to the new directions
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
	public Route(Route currentRoute, BlockingQueue<Point> coordinates, BlockingQueue<Action> directions, Pose startPose,
			Action orientationAdj, int myStartTime, Point startPoint) {
		this.coordinates = coordinates;
		this.coordinates.addAll(currentRoute.getCoordinates());
		this.directions = directions;
		this.directions.addAll(currentRoute.getDirections());
		this.routeLength = currentRoute.getLength() + coordinates.size();
		this.startPose = startPose;
		this.dirsArray = (Action[]) directions.toArray();
		this.coordsArray = (Point[]) coordinates.toArray();
		this.myStartTime = myStartTime;
		this.startPoint = startPoint;
	}

	/**
	 * @param firstRoute
	 *            the route which is to form the start of the new route
	 * @param secondRoute
	 *            the route which is to form the end of the new route
	 * @param middleAction
	 *            the action to be carried out between the two routes
	 * @throws IllegalArgumentException
	 *             middleAction can only be WAIT, PICKUP, DROPOFF, CANCEL, SHUTDOWN or null. If null is used then the no additional action will be added
	 * @throws IllegalArgumentException
	 *             one or both routes have no instructions
	 * @throws IllegalArgumentException
	 *             last coordinate of first route is not adjacent to first
	 *             coordinate of second route. Routes cannot be joined
	 */
	public Route(Route firstRoute, Route secondRoute, Action middleAction) {
		if (!nonMovementAction(middleAction)&&!(middleAction == null)) {
			throw new IllegalArgumentException("middleAction can only be WAIT, PICKUP, DROPOFF, CANCEL or SHUTDOWN");
		} else {
			concatenationConstructor(firstRoute, secondRoute, middleAction);
		}
	}

	/**
	 * @param firstRoute
	 *            the route which is to form the start of the new route
	 * @param secondRoute
	 *            the route which is to form the end of the new route
	 * @throws IllegalArgumentException
	 *             one or both routes have no instructions
	 * @throws IllegalArgumentException
	 *             last coordinate of first route is not adjacent to first
	 *             coordinate of second route. Routes cannot be joined
	 */
	public Route(Route firstRoute, Route secondRoute) {
		concatenationConstructor(firstRoute, secondRoute, null);
	}

	/**
	 * @param routes
	 *            list of routes to be combined, in the order they are to be
	 *            executed
	 * @throws IllegalArgumentException
	 *             routes contains no elements
	 * @throws IllegalArgumentException
	 *             one or more routes have no instructions
	 * @throws IllegalArgumentException
	 *             last coordinate of one route is not adjacent to first
	 *             coordinate of next route. Routes cannot be joined
	 */
	public Route(ArrayList<Route> routes) {
		Route currentRoute;
		if (routes.size() < 1) {
			throw new IllegalArgumentException("routes contains no elements");
		} else {
			currentRoute = routes.get(0);
		}
		for (int i = 1; i < routes.size(); i++) {
			currentRoute = new Route(currentRoute, routes.get(i));
		}
		this.coordinates = currentRoute.getCoordinates();
		this.directions = currentRoute.getDirections();
		this.routeLength = currentRoute.getLength();
		this.startPose = currentRoute.getStartPose();
		Point[] p = new Point[coordinates.size()];
		this.coordsArray = coordinates.toArray(p);
		Action[] a = new Action[directions.size()];
		this.dirsArray = directions.toArray(a);
		this.myStartTime = routes.get(0).getStartTime();
		this.startPoint = routes.get(0).getStartPoint();
	}

	/**
	 * @param routes
	 *            list of routes to be combined, in the order they are to be
	 *            executed
	 * @param actions
	 *            list of actions in the order they are to be carried out in. One
	 *            action between each route
	 * @throws IllegalArgumentException
	 *             routes is not exactly one greater in size than actions
	 * @throws IllegalArgumentException
	 *             routes contains only one route or less
	 * @throws IllegalArgumentException
	 *             one or more routes have no instructions
	 * @throws IllegalArgumentException
	 *             last coordinate of one route is not adjacent to first
	 *             coordinate of next route. Routes cannot be joined
	 */
	public Route(ArrayList<Route> routes, ArrayList<Action> actions) {
		if (routes.size() != actions.size() + 1) {
			throw new IllegalArgumentException("routes is not exactly one greater in size than actions");
		}
		Route currentRoute;
		if (routes.size() < 2) {
			throw new IllegalArgumentException("routes contains only one route or less");
		} else {
			currentRoute = routes.get(0);
		}
		for (int i = 1; i < routes.size(); i++) {
			currentRoute = new Route(currentRoute, routes.get(i), actions.get(i - 1));
		}
		this.coordinates = currentRoute.getCoordinates();
		this.directions = currentRoute.getDirections();
		this.routeLength = currentRoute.getLength();
		this.startPose = currentRoute.getStartPose();
		Point[] p = new Point[coordinates.size()];
		this.coordsArray = coordinates.toArray(p);
		Action[] a = new Action[directions.size()];
		this.dirsArray = directions.toArray(a);
		this.myStartTime = routes.get(0).getStartTime();
		this.startPoint = routes.get(0).getStartPoint();
	}

	/**
	 * @param firstRoute
	 *            the route which is to form the start of the new route
	 * @param action
	 *            the action to be carried out between the two routes
	 * @throws IllegalArgumentException
	 *             action can only be WAIT, PICKUP, DROPOFF, CANCEL or SHUTDOWN
	 */
	public Route(Route route, Action action) {
		if (!nonMovementAction(action)) {
			throw new IllegalArgumentException("action was not a valid non-movement action");
		}
		this.coordinates = route.getCoordinates();
		this.coordinates.add(route.getCoordinatesArray()[route.getLength()-1]);
		this.directions = route.getDirections();
		directions.add(action);
		this.routeLength = route.getLength()+1;
		this.startPose = route.getStartPose();
		Action[] a = new Action[directions.size()];
		this.dirsArray = directions.toArray(a);
		Point[] p = new Point[coordinates.size()];
		this.coordsArray = coordinates.toArray(p);
		this.myStartTime = route.getStartTime();
		this.startPoint = route.getStartPoint();
	}
	
	/* constructor method shared by all route merging constructors */
	private void concatenationConstructor(Route firstRoute, Route secondRoute, Action middleAction) {
		// checks both routes have elements
		if ((firstRoute.getCoordinatesArray().length < 1) || (secondRoute.getCoordinatesArray().length < 1)) {
			throw new IllegalArgumentException("One or both routes have no instructions");
		}
		// checks that the end of the first route is only one grid space away from the
		// start of the second
		else if (!adjacentCoords(firstRoute.getCoordinatesArray()[firstRoute.getCoordinatesArray().length - 1],
				secondRoute.getCoordinatesArray()[0])) {
			throw new IllegalArgumentException(
					"last coordinate of first route is not adjacent to first coordinate of second route");
		} else {
			this.coordinates = firstRoute.getCoordinates();
			this.startPose = firstRoute.getStartPose();
			this.myStartTime = firstRoute.getStartTime();
			this.directions = firstRoute.getDirections();
			this.startPoint = firstRoute.getStartPoint();

			int tempRouteLength = firstRoute.getLength() + secondRoute.getLength();

			// the middleAction can only be null when called by the constructor which only
			// takes in two routes
			if (nonMovementAction(middleAction)) {
				this.coordinates.add(firstRoute.getCoordinatesArray()[firstRoute.getLength()-1]);
				this.directions.add(middleAction);
				tempRouteLength = tempRouteLength + 1;
			}
			this.routeLength = tempRouteLength;

			BlockingQueue<Action> temp = secondRoute.getDirections();
			// the first instructon of the second route has to be changed to change the
			// direction the robot is travelling in
			temp.remove();
			Point[] ps = firstRoute.getCoordinatesArray();
			Point p1 = ps[ps.length - 1];
			Point p2 = secondRoute.getCoordinatesArray()[0];

			// adds correct direction instruction
			this.directions.add(generateRotation(p1, p2, firstRoute.getFinalPose()));
			this.directions.addAll(temp);

			this.coordinates.addAll(secondRoute.getCoordinates());
			Point[] p = new Point[coordinates.size()];
			this.coordsArray = coordinates.toArray(p);
			Action[] a = new Action[directions.size()];
			this.dirsArray = directions.toArray(a);
		}
	}

	/* checks if two points are at adjacent grid positions to each other*/
	private boolean adjacentCoords(Point p1, Point p2) {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != y && x != -y) {
					if (p1.equals(new Point(p2.x + x, p2.y + y))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*checks that only non move instructions are added to the route from outside an existing route object*/
	private boolean nonMovementAction(Action a) {
		if (a == null) {
			return false;
		}
		switch (a) {
		case WAIT:{
			return true;
		}
		case PICKUP:{
			return true;			
		}
		case DROPOFF:{
			return true;			
		}
		case CANCEL:{
			return true;			
		}
		case SHUTDOWN:{
			return true;			
		}
		default:{
			return false;
		}
		}
	}
	
	/**
	 * @return an array of all directions in the original route
	 */
	public Action[] getDirectionArray() {
		return dirsArray;
	}

	/** @return the orientation of the robot before any motion occurs */
	public Pose getStartPose() {
		return startPose;
	}

	/** @return queue of coordinates in order of traversal */
	public BlockingQueue<Point> getCoordinates() {
		return coordinates;
	}

	/** @return Array of type Point of all coordinates in original route */
	public Point[] getCoordinatesArray() {
		return coordsArray;
	}

	/** @return queue of directions in order of execution */
	public BlockingQueue<Action> getDirections() {
		return directions;
	}

	/** @return the total length of the route */
	public int getLength() {
		return routeLength;
	}

	/**
	 * @param relativeTime
	 *            the time relative to the robot starting the route
	 * @return the pose of the robot at the specified time
	 * @throws IllegalArgumentException
	 *             the time given is not within the length of the route
	 */
	public Pose getPoseAt(int relativeTime) {
		Pose p;
		if (relativeTime > 0 && relativeTime<routeLength) {
			int direction = AStar.getDirection(coordsArray[relativeTime - 1], coordsArray[relativeTime]);
			switch (direction) {
			case 0: {
				p = Pose.NEG_X;
				break;
			}
			case 1: {
				p = Pose.NEG_Y;
				break;
			}
			case 2: {
				p = Pose.POS_X;
				break;
			}
			case 3: {
				p = Pose.POS_Y;
				break;
			}
			default: { // should not be possible to reach
				p = Pose.POS_X;
				break;
			}
			}
		} else if (relativeTime == 0) {
			p = this.getStartPose();
		} else {
			throw new IllegalArgumentException("relativeTime cannot be negative or longer than length of route");
		}
		return p;
	}

	/** @return the pose of the robot upon completion of the entire route */
	public Pose getFinalPose() {
		return getPoseAt(routeLength - 1);
	}

	/** @return the time at which this route was started */
	public int getStartTime() {
		return myStartTime;
	}

	public Point getStartPoint() {
		return startPoint;
	}
	
	/*returns the new instruction that must be carried out to move from the end of one route to the start of the next*/
	private Action generateRotation(Point firstPoint, Point secondPoint, Pose poseAtFirstPoint) {
		int direction = AStar.getDirection(firstPoint, secondPoint);
		logger.debug(direction);
		logger.debug(firstPoint);
		logger.debug(secondPoint);
		int startPose = AStar.poseToInt(poseAtFirstPoint);
		return AStar.generateDirectionInstruction(startPose, direction);
	}
}