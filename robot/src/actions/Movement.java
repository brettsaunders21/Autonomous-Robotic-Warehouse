package actions;

import interfaces.Action;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import main.Configuration;
import rp.systems.WheeledRobotSystem;

public class Movement {
	private final int MID_BOUND;
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	RobotInterface rInterface;
	
	public Movement(int _MID_BOUND, RobotInterface _rI) {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		MID_BOUND = _MID_BOUND;
		PILOT.setTravelSpeed(0.2f);
		PILOT.setRotateSpeed(60);
		rInterface = _rI;
	}
	
	public void nextAction(Action command, int pickAmount) {
		switch(command) {
		case HOLD:
			Delay.msDelay(100);
			return;
		case WAIT: 
			Delay.msDelay(100);
			return;
		case FORWARD:
			PILOT.travel(0.05);
			break;
		case LEFT:
			PILOT.travel(0.05);
			PILOT.rotate(-45);
			while (!isRightOnLine()) {
				PILOT.rotateRight();
			}
			PILOT.rotate(5);
			PILOT.forward();
			break;
		case RIGHT: 
			PILOT.travel(0.05);
			PILOT.rotate(45);
			while (!isLeftOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(-5);
			PILOT.forward();
			break;
		case BACKWARD: 
			PILOT.stop();
			PILOT.travel(-0.1);
			PILOT.rotate(20);
			while (!isRightOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(20);
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
		
		if (!(command.equals(Action.PICKUP) || command.equals(Action.DROPOFF)|| command.equals(Action.WAIT)|| command.equals(Action.HOLD))) {
		while (!(isRightOnLine() && isLeftOnLine())) {
			PILOT.forward();
			while (isLeftOnLine() && !isRightOnLine()) {
				PILOT.rotateRight();
			}
			while (!isLeftOnLine() && isRightOnLine()) {
				PILOT.rotateLeft();
			}
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