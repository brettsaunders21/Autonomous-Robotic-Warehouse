package actions;

import lejos.nxt.Motor;
import lejos.util.Delay;

/**
 * Right of Line Class
 * 
 * Follows line to the right if no left line detected
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */

public class RightOfLine extends AbstractBehaviour {
	
	public RightOfLine(int _MID_BOUND) {
		super(_MID_BOUND);
	}

	@Override
	public boolean takeControl() {
		//If the right sensor detects a line then takeControl
		Delay.msDelay(25);
		return !isLeftOnLine() && isRightOnLine();
	}

	@Override
	public void action() {
		Motor.A.setSpeed(Motor.A.getSpeed() * 1.3f);
		Delay.msDelay(200);
	}

}
