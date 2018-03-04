package routeplanning;

import java.util.ArrayList;

import lejos.geom.Point;

public class TempRouteInfo{
	private final int timeInterval;
	private final ArrayList<Point> coords;
	private final ArrayList<Integer> dirs;
	
	/**@param time the number of directions the robot has to execute  to carry out this route
	 * @param coords the list of coordinates on the route in the order they should be reached
	 * @param dirs the list of directions that needs to be carried out to traverse the route*/
	public TempRouteInfo(int time, ArrayList<Point> coords, ArrayList<Integer> dirs) {
		this.timeInterval = time;
		this.coords = coords;
		this.dirs = dirs;
	}
	
	/**@return the number of directions the robot has to execute  to carry out this route7*/
	public int getTimeInterval() {
		return timeInterval;
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