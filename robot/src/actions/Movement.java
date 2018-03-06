package actions;

import interfaces.Action;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import main.Configuration;
import rp.systems.WheeledRobotSystem;

public class Movement {
	private final int adjustmentValue;
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	private final int CORRECTION = 7;
	
	public Movement(int _adjustmentValue) {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		adjustmentValue = _adjustmentValue;
		PILOT.setTravelSpeed(0.2f);
	}
	
	public void nextAction(Action command, int pickAmount) {
		switch(command) {
		case WAIT: 
			Delay.msDelay(100);
			return;
		case FORWARD:
			break;
		case LEFT:
			PILOT.rotate(10);
			while (!isOnLine(LEFT_SENSOR.readValue())) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(10);
			break;
		case RIGHT: 
			PILOT.rotate(10);
			while (!isOnLine(RIGHT_SENSOR.readValue())) {
				PILOT.rotateRight();
			}
			PILOT.rotate(10);
			break;
		case BACKWARD: 
			PILOT.travel(-0.1);
			PILOT.rotate(20);
			while (!isOnLine(LEFT_SENSOR.readValue())) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(20);
			break;
		case PICKUP:
			//Need interface code
			break;
		case DROPOFF:
			//Need interface code
			break;
		case TURN_180: 
			PILOT.rotate(180);
			break;
		case TURN_LEFT:
			PILOT.rotate(90);
			break;
		case TURN_RIGHT:
			PILOT.rotate(-90);
			break;
		case CANCEL:
			break;
		case SHUTDOWN:
			break;
		}
		
		while (!(isRightOnLine() && isLeftOnLine())) {
			PILOT.forward();
			while (isLeftOnLine() && !isRightOnLine()) {
				PILOT.rotateLeft();
			}
			while (!isLeftOnLine() && isRightOnLine()) {
				PILOT.rotateRight();
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
