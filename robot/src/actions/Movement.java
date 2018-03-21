package actions;

import interfaces.Action;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import main.Configuration;

public class Movement {
	private final int MID_BOUND;
	private final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	private RobotInterface rInterface;
	private final float DEGREETURN = 90f;
	private final float HALFLENGTH = 24.5f;

	public Movement(int _MID_BOUND, RobotInterface _rI) {
		PILOT = new DifferentialPilot(56, 110.5, Motor.A, Motor.C);
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		MID_BOUND = _MID_BOUND;
		PILOT.setTravelSpeed(PILOT.getMaxTravelSpeed());
		PILOT.setRotateSpeed(PILOT.getRotateMaxSpeed());
		rInterface = _rI;
	}

	public void nextAction(Action command, int pickAmount) {
		switch (command) {
		case HOLD:
			Delay.msDelay(100);
			return;
		case WAIT:
			Delay.msDelay(100);
			return;
		case FORWARD:
			break;
		case LEFT:
			PILOT.travel(HALFLENGTH);
			PILOT.rotate(-DEGREETURN);
			PILOT.forward();
			break;
		case RIGHT:
			PILOT.travel(HALFLENGTH);
			PILOT.rotate(DEGREETURN);
			PILOT.forward();
			break;
		case BACKWARD:
			PILOT.stop();
			PILOT.travel(-HALFLENGTH);
			PILOT.rotate(DEGREETURN * 2);
			PILOT.forward();
			break;
		case PICKUP:
			PILOT.stop();
			rInterface.loadItemsMessage(pickAmount);
			break;
		case DROPOFF:
			PILOT.stop();
			rInterface.unloadItemsMessage();
			break;
		case CANCEL:
			break;
		case SHUTDOWN:
			break;
		default:
			break;
		}
		if (!(command.equals(Action.PICKUP) || command.equals(Action.DROPOFF) || command.equals(Action.WAIT)
				|| command.equals(Action.HOLD))) {
			while (!(isRightOnLine() && isLeftOnLine())) {
				PILOT.forward();
				if (isLeftOnLine() && !isRightOnLine()) {
					Motor.C.setSpeed(Motor.C.getSpeed() * 0.1f);
					Delay.msDelay(40);
				}
				if (!isLeftOnLine() && isRightOnLine()) {
					Motor.A.setSpeed(Motor.A.getSpeed() * 0.1f);
					Delay.msDelay(40);
				}
				PILOT.setRotateSpeed(PILOT.getRotateMaxSpeed());
			}
		}
		PILOT.stop();
		rInterface.resetQuantity();
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

}
