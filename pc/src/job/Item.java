package job;

import lejos.geom.Point;

/**
 * @author Brett Saunders <bjs730@cs.bham.ac.uk>
 *
 */
public class Item {

	private final String ID;
	private final float WEIGHT;
	private final float TOTAL_WEIGHT;
	private final float REWARD;
	private final float TOTAL_REWARD;
	private final Point POSITION;
	private final int QUANTITY;
	
	public Item(String _ID, int _QUANTITY, float _WEIGHT, Point _POS, float _REWARD) {
		this.ID = _ID;
		this.QUANTITY = _QUANTITY;
		this.WEIGHT = _WEIGHT;
		this.POSITION = _POS;
		this.REWARD = _REWARD;
		this.TOTAL_WEIGHT = WEIGHT * QUANTITY;
		this.TOTAL_REWARD = REWARD * QUANTITY;
	}

	/**
	 * @return the ID of the item
	 */
	public final String getID() {
		return ID;
	}

	/**
	 * @return the weight of the item
	 */
	public final float getWEIGHT() {
		return WEIGHT;
	}

	/**
	 * @return the reward of the item
	 */
	public final float getREWARD() {
		return REWARD;
	}

	/**
	 * @return the position of where to get the item
	 */
	public final Point getPOSITION() {
		return POSITION;
	}

	/**
	 * @return the quantity of item to pick up
	 */
	public final int getQUANTITY() {
		return QUANTITY;
	}

	/**
	 * @return the tOTAL_WEIGHT
	 */
	public float getTOTAL_WEIGHT() {
		return TOTAL_WEIGHT;
	}

	/**
	 * @return the tOTAL_REWARD
	 */
	public float getTOTAL_REWARD() {
		return TOTAL_REWARD;
	}
}
