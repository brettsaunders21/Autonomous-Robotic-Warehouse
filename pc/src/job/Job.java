package job;

import java.util.ArrayList;

import lejos.geom.Point;

/**
 * @author Brett Saunders <bjs730@cs.bham.ac.uk>
 *
 */
public class Job {
	private final int ID;
	private final ArrayList<Item> ITEMS;
	private final float WEIGHT;
	private final float SCORE;
	private Point dropLocation;

	public Job(int _ID, ArrayList<Item> _ITEMS) {
		this.ID = _ID;
		this.ITEMS = _ITEMS;
		this.WEIGHT = 0;
		this.SCORE = 0;
		dropLocation = null;
	}
	
	
	public Job(int _ID, ArrayList<Item> _ITEMS, Point _dropLocation) {
		this.ID = _ID;
		this.ITEMS = _ITEMS;
		this.WEIGHT = 0;
		this.SCORE = 0;
		dropLocation = _dropLocation;
	}

	/**
	 * @return the ID of job
	 */
	public final int getID() {
		return ID;
	}

	/**
	 * @return the items that make up job
	 */
	public final ArrayList<Item> getITEMS() {
		return ITEMS;
	}

	/**
	 * @return the total weight of the job
	 */
	public final float getWEIGHT() {
		return WEIGHT;
	}

	/**
	 * @return the total score of the job
	 */
	public final float getSCORE() {
		return SCORE;
	}

	/**
	 * @return the dropLocation
	 */
	public final Point getDropLocation() {
		return dropLocation;
	}

	/**
	 * @param dropLocation set the location of where items are dropped
	 */
	public final void setDropLocation(Point dropLocation) {
		this.dropLocation = dropLocation;
	}
}
