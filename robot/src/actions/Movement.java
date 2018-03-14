package actions;

import interfaces.Action;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import main.Configuration;
import rp.systems.WheeledRobotSystem;

public class Movement {
	private final int adjustmentValue;
	private final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	private final int CORRECTION = 7;
	RobotInterface rI;

	public Movement(int _adjustmentValue, RobotInterface _rI) {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		adjustmentValue = _adjustmentValue;
		PILOT.setTravelSpeed(0.2f);
		rI = _rI;
	}

	public void nextAction(Action command, int pickAmount) {
		switch (command) {
		case WAIT: {
			Delay.msDelay(100);
			return;
		}
		case FORWARD: {
			PILOT.forward();
			break;
		}
		case LEFT: {
			PILOT.rotate(10);
			while (!isLeftOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(10);
			break;
		}
		case RIGHT: {
			PILOT.rotate(10);
			while (!isRightOnLine()) {
				PILOT.rotateRight();
			}
			PILOT.rotate(10);
			break;
		}
		case BACKWARD: {
			PILOT.travel(-0.1);
			PILOT.rotate(20);
			while (!isLeftOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(20);
			break;
		}
		case PICKUP: {
			rI.waitForLoadingMessage(pickAmount);
			break;
		}
		case DROPOFF: {
			rI.waitForunLoadingMessage(pickAmount);
			rI.resetQuantity();
			break;
		}
		case CANCEL: {
			break;
		}
		case SHUTDOWN: {
			break;
		}
		default: {
			break;
		}
		}

		if (PILOT.isMoving()) {
			while (!(isRightOnLine() && isLeftOnLine())) {
				while (isLeftOnLine() && !isRightOnLine()) {
					PILOT.rotateLeft();
				}
				while (!isLeftOnLine() && isRightOnLine()) {
					PILOT.rotateRight();
				}
			}
		}
		PILOT.stop();

	}

	public boolean isOnLine(int lightValue) {
		return Math.abs(adjustmentValue - lightValue) > CORRECTION;
	}

	public boolean isRightOnLine() {
		return isOnLine(RIGHT_SENSOR.readValue());
	}

	public boolean isLeftOnLine() {
		return isOnLine(LEFT_SENSOR.readValue());
	}

}
