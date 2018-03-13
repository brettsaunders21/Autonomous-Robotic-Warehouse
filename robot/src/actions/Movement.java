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
	RobotInterface rI;
	
	public Movement(int _lineValue, int _backgroundValue, RobotInterface _rI) {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		MID_BOUND = (_lineValue + _backgroundValue) /2;
		PILOT.setTravelSpeed(0.05f);
		PILOT.setRotateSpeed(10);
		rI = _rI;
	}
	
	public void nextAction(Action command, int pickAmount) {
		switch(command) {
		case WAIT: 
			Delay.msDelay(100);
			return;
		case FORWARD:
			PILOT.travel(0.05);
			break;
		case LEFT:
			PILOT.travel(0.05);
			PILOT.rotate(-50);
			while (!isRightOnLine()) {
				PILOT.rotateRight();
			}
			PILOT.rotate(10);
			PILOT.forward();
			break;
		case RIGHT: 
			PILOT.travel(0.05);
			PILOT.rotate(50);
			while (!isLeftOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(-10);
			PILOT.forward();
			break;
		case BACKWARD: 
			PILOT.stop();
			PILOT.travel(-0.1);
			PILOT.rotate(20);
			while (!isLeftOnLine()) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(20);
			PILOT.forward();
			break;
		case PICKUP:
			PILOT.travel(0.05);
			rI.waitForLoadingMessage(pickAmount);
			break;
		case DROPOFF:
			PILOT.travel(0.05);
			rI.waitForunLoadingMessage(pickAmount);
			rI.resetQuantity();
			break;
		case CANCEL:
			break;
		case SHUTDOWN:
			break;
		default:
			break;
		}
		
		while (!(isRightOnLine() && isLeftOnLine())) {
			PILOT.forward();
			while (isLeftOnLine() && !isRightOnLine()) {
				PILOT.rotateRight();
			}
			while (!isLeftOnLine() && isRightOnLine()) {
				PILOT.rotateLeft();
			}
		}
		PILOT.stop();
	}
	
	public boolean isOnLine(int lightValue) {
		if (lightValue <= MID_BOUND) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRightOnLine() {
		return isOnLine(RIGHT_SENSOR.readValue());
	}
	
	public boolean isLeftOnLine() {
		return isOnLine(LEFT_SENSOR.readValue());
	}

}
