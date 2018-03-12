package routeplanning;

import lejos.geom.Point;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */


/**Map objects can only be constructed using the static methods generateRealWarehouseMap() and createTestMap()*/
public class Map {
	final static Logger logger = Logger.getLogger(Map.class);

	private boolean[][] passable; // false for walls or other obstructions in the map-space
	private final int height; // height of the map
	private final int width; // width of the map

	/**Creates the map that will be used on the robot warehouse assignment
	 * @return the warehouse assignment map in the format required by AStar class*/
	public static Map generateRealWarehouseMap() {
		GridMap map = MapUtils.createRealWarehouse();
		int width = map.getXSize();
		int height = map.getYSize();
		ArrayList<Point> obstructions = new ArrayList<Point>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map.isObstructed(x, y)) {
					obstructions.add(new Point(x, y));
				}
			}
		}
		Point[] outArray = new Point[obstructions.size()];
		outArray = obstructions.toArray(outArray);
		return new Map(width, height, outArray);
	}
	
	/**Creates a map based upon the parameters given. Should only be used for testing purposes
	 * @param width The map width
	 * @param height The map height
	 * @param obstructions The coordinates of points on the grid that are not
	 * possible to traverse. Must be integer coordinates stored as float
	 * @throws IllegalArgumentException width or height is less than 0
	 * @throws IllegalArgumentException more obstruction coordinates than map
	 * coordinates
	 * @throws IllegalArgumentException obstruction coordinate not within bounds of
	 * map*/
	public static Map createTestMap(int width, int height, Point[] array) {
		return new Map(width, height, array);
	}

	/*
	 * The map will start at coordinate (0,0) and extend to coordinate(width,
	 * height)
	 * 
	 * @param width The map width
	 * 
	 * @param height The map height
	 * 
	 * @param obstructions The coordinates of points on the grid that are not
	 * possible to traverse. Must be integer coordinates stored as float
	 * 
	 * @throws IllegalArgumentException width or height is less than 0
	 * 
	 * @throws IllegalArgumentException more obstruction coordinates than map
	 * coordinates
	 * 
	 * @throws IllegalArgumentException obstruction coordinate not within bounds of
	 * map
	 */
	private Map(int width, int height, Point[] obstructions) {
		this.height = height;
		this.width = width;

		// throws an exception if the width or height parameter is not greater than 0
		if (width <= 0 || height <= 0) {
			logger.debug("w " + width + " h " + height);
			throw new IllegalArgumentException("Negative or zero dimension given");
		}
		// throws an exception if there are more coordinates for obstructions than there
		// are coordinates
		if (obstructions.length >= ((width + 1) * (height + 1))) {
			logger.debug("Num on map " + width * height + " num given " + obstructions.length);
			throw new IllegalArgumentException("More points than grid size");
		}

		this.passable = new boolean[width + 1][height + 1];
		// initialises all values to true, ie no obstructions
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				passable[x][y] = true;
			}
		}

		// cycles through every coordinate
		for (int i = 0; i < obstructions.length; i++) {
			// casts to int as coordinates are expected in int format but point holds float
			int x = (int) obstructions[i].x;
			int y = (int) obstructions[i].y;
			// throws an exception if coordinate is outside bounds of the map
			if (!withinMapBounds(obstructions[i])) {
				logger.debug("w " + width + " h " + height + " x " + x + " y " + y);
				throw new IllegalArgumentException("Point not within bounds of map");
			}
			passable[x][y] = false;
		}
	}

	/*
	 * Generates a map based upon the passable flag states of the 2d array
	 * 
	 * @param passable 2d array containing passable flags for each coordinate
	 * 
	 * @throws IllegalArgumentException one or more dimensions of array are of size
	 * 0
	 */
	private Map(boolean[][] passable) {
		if (passable.length < 1) {
			throw new IllegalArgumentException("Array has no width");
		}
		this.width = passable.length;
		if (passable[0].length < 1) {
			throw new IllegalArgumentException("Array has no height");
		}
		this.height = passable[0].length;
		this.passable = passable;
	}

	/**
	 * @param p
	 *            the coordinate being checked
	 * @return true if the given point is within the bounds of the map, false if it
	 *         is not
	 */
	public boolean withinMapBounds(Point p) {
		float x = p.x;
		float y = p.y;
		return ((x >= 0 && x < width) && (y >= 0 && y < height));
	}

	/**
	 * @param p
	 *            the coordinate of on the grid being checked
	 * @return true if the point is able to be traversed, false if it is obstructed
	 * @throws IllegalArgumentException
	 *             the coordinate is out of the bounds of the map
	 */
	public boolean isPassable(Point p) {
		if (!withinMapBounds(p)) {
			logger.debug("w " + width + " h " + height + " x " + p.x + " y " + p.y);
			throw new IllegalArgumentException("Point not within bounds of map");
		}
		return passable[(int) p.x][(int) p.y];
	}

	/** @return a clone of the current object */
	@Override
	public Map clone() {
		return new Map(passable);
	}

	/** @return the map width */
	public int getWidth() {
		return width;
	}

	/** @return the map height */
	public int getHeight() {
		return height;
	}

	/**@return the array of passable flags*/
	public boolean[][] obstructions(){
		return passable;
	}
}