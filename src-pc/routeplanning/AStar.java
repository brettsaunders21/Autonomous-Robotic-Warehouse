package routeplanning;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import lejos.geom.Point;

public class AStar {
	final static Logger logger = Logger.getLogger(AStar.class);
	private final Map map;
	
	/**@param m the map that route finding will be carried out on*/
	public AStar(Map map) {
		this.map = map;
	}
	
	/**@param currentPosition the starting coordinate of the route
	 * @param targetPosition the target coordinate of the route
	 * @return shortest route between the two points avoiding obstacles
	 * @throws IllegalArgumentException one or both coordinates are not within the map area*/
	public Route generateRoute(Point currentPosition, Point targetPosition) {
		if(!map.withinMapBounds(currentPosition)||!map.withinMapBounds(targetPosition)) {
			throw new IllegalArgumentException("Coordinate not within map area");
		}
		
		BlockingQueue directions = new LinkedBlockingQueue();
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		int length = 0;
		
		boolean atDestination = currentPosition.equals(targetPosition);
		while (!atDestination) {
			
		}
		return new Route(coordinates, directions, length);
	}
	
}