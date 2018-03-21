package actions;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import main.Configuration;

/**
 * Abstract Behaviour class
 * 
 * Sets up the basis for robot behaviours by implementing common methods that
 * will be used by all behaviour classes
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */
public abstract class AbstractBehaviour implements Behavior {

	// Declare variables
	protected boolean m_suppressed = false;
	protected final LightSensor LEFT_SENSOR;
	protected final LightSensor RIGHT_SENSOR;
	protected final DifferentialPilot PILOT;
	protected final int MID_BOUND;

	/**
	 * Constructor.
	 * 
	 * @param _config The WheeledRobotConfiguration being used by the robot
	 */
	public AbstractBehaviour(int _MID_BOUND) {
		MID_BOUND = _MID_BOUND;
		PILOT = new DifferentialPilot(56, 110.5, Motor.A, Motor.C);
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
	}

	public boolean isOnLine(int lightValue) {
		if (lightValue <= MID_BOUND) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRightOnLine() {
		return isOnLine(RIGHT_SENSOR.getNormalizedLightValue());
	}

	public boolean isLeftOnLine() {
		return isOnLine(LEFT_SENSOR.getNormalizedLightValue());
	}
	/**
	 * Suppresses the behaviour
	 */
	@Override
	public void suppress() {
		m_suppressed = true;
	}

}
