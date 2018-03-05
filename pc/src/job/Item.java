package job;

import lejos.geom.Point;

/**
 * @author Brett Saunders <bjs730@cs.bham.ac.uk>
 *
 */
public class Item {

	private final String ID;
	private final float WEIGHT;
	private final float REWARD;
	private final Point POSITION;
	private final int QUANTITY;
	
	public Item(String _ID, int _QUANTITY) {
		this.ID = _ID;
		this.QUANTITY = _QUANTITY;
		WEIGHT = 0;
		POSITION = new Point(1,1);
		REWARD = 0;
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
}
