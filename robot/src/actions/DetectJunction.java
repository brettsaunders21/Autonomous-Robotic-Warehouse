package actions;

import lejos.robotics.subsumption.ArbitratorEx;
import lejos.util.Delay;
/**
 * Detect Junctions Class
 * 
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */
public class DetectJunction extends AbstractBehaviour {
	
	private ArbitratorEx arby;

	/**
	 * Constructor.
	 * @param arby 
	 * @param _config The WheeledRobotConfiguration being used by the robot
	 * @param route 
	 */
	public DetectJunction(int _MID_BOUND, ArbitratorEx _arby) {
		super(_MID_BOUND);
		arby = _arby;
	}

	@Override
	public boolean takeControl() {
		//If the right sensor detects a line then takeControl
		Delay.msDelay(25);
		return isLeftOnLine() && isRightOnLine();
	}

	@Override
	public void action() {
		arby.stop();
	}
}
