package routeplanning.astarhelpers;

import java.util.ArrayList;
import lejos.geom.Point;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */

public class TempRouteInfo{
	private final ArrayList<Point> coords;
	private final ArrayList<Integer> dirs;
	
	/**@param coords the list of coordinates on the route in the order they should be reached
	 * @param dirs the list of directions that needs to be carried out to traverse the route*/
	public TempRouteInfo(ArrayList<Point> coords, ArrayList<Integer> dirs) {
		if (coords.size()!=dirs.size()) {
			throw new IllegalArgumentException("lists are of different sizes");
		}
		if (coords.size()<1) {
			throw new IllegalArgumentException("lists have no elements");
		}
		this.coords = coords;
		this.dirs = dirs;
	}

	/**@return the list of coordinates on the route in the order they should be reached*/
	public ArrayList<Point> getCoords(){
		return coords;
	}
	
	/**@return the list of directions that needs to be carried out to traverse the route*/
	public ArrayList<Integer> getDirs(){
		return dirs;
	}
}