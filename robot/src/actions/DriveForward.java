package actions;

import rp.util.Rate;

/**
 * Drive forward class
 * 
 * Default behaviour of the robot - drive forward
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */
public class DriveForward extends AbstractBehaviour {

	/**
	 * Constructor.
	 * 
	 * @param _config The WheeledRobotConfiguration being used by the robot
	 */
	public DriveForward(int _MID_BOUND) {
		super(_MID_BOUND);
	}

	/**
	 * Take control of the robot when chosen by arbitrator
	 */
	@Override
	public boolean takeControl() {
		// Always return true as default behaviour
		return true;
	}

	/**
	 * Perform an action if takeControl returns true
	 */
	@Override
	public void action() {
		PILOT.forward();
		Rate r = new Rate(20);
		while (!m_suppressed) {
			r.sleep();
		}
		PILOT.stop();
		m_suppressed = false;
	}

}
