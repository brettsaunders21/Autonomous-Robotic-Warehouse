package actions;

import lejos.nxt.Motor;
import lejos.util.Delay;

/**
 * Left of Line Class
 * 
 * Follows line to the left if no right line detected
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */

public class LeftOfLine extends AbstractBehaviour {

	public LeftOfLine(int _MID_BOUND) {
		super(_MID_BOUND);
	}
	
	@Override
	public boolean takeControl() {
		//If the right sensor detects a line then takeControl
		Delay.msDelay(10);
		return isLeftOnLine() && !isRightOnLine();
	}

	@Override
	public void action() {
		PILOT.forward();
		Motor.A.setSpeed(Motor.A.getSpeed() * 1.3f);
	}
	

}
