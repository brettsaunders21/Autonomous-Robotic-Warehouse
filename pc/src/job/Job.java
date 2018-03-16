package job;

import java.util.ArrayList;

import lejos.geom.Point;
import routeplanning.Route;

/**
 * @author Brett Saunders <bjs730@cs.bham.ac.uk>
 *
 */
public class Job {
	private final int ID;
	private ArrayList<Item> ITEMS;
	private final float WEIGHT;
	private final float REWARD;
	private Point dropLocation;
	private Route currentroute;
	private boolean canceled;

	public Job(int _ID, ArrayList<Item> _ITEMS) {
		this.ID = _ID;
		this.ITEMS = _ITEMS;
		this.WEIGHT = calculateWeight();
		this.REWARD = calculateReward();
		dropLocation = null;
		currentroute = null;
		setCanceled(false);
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
	
	public float calculateWeight() {
		float weight = 0.0f;
		for (Item i : ITEMS) {
			weight += i.getTOTAL_WEIGHT();
		}
		return weight;
	}
	
	public float calculateReward() {
		float reward = 0.0f;
		for (Item i : ITEMS) {
			reward += i.getTOTAL_REWARD();
		}
		return reward;
	}

	/**
	 * @return the wEIGHT
	 */
	public float getWEIGHT() {
		return WEIGHT;
	}

	/**
	 * @return the rEWARD
	 */
	public float getREWARD() {
		return REWARD;
	}

	public Route getCurrentroute() {
		return currentroute;
	}

	public void assignCurrentroute(Route currentroute) {
		this.currentroute = currentroute;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	public void setItems(ArrayList<Item> i ) {
		this.ITEMS = i;
	}
}
