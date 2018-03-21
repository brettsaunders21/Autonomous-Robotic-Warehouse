package actions;

import interfaces.Action;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class Movement extends Thread{
	private final int MID_BOUND;
	private final DifferentialPilot PILOT;
	private RobotInterface rInterface;
	private final float DEGREETURN = 90f;
	private final float HALFLENGTH = 24.5f;

	public Movement(int _MID_BOUND, RobotInterface _rI) {
		PILOT = new DifferentialPilot(56, 110.5, Motor.A, Motor.C);
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
		PILOT.stop();
		rInterface.resetQuantity();
	}

}
