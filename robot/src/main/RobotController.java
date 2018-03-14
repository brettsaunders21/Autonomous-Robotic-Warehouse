package main;

import java.io.IOException;
import actions.Movement;
import actions.RobotInterface;
import communication.RobotNetworkHandler;
import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import rp.systems.StoppableRunnable;

public class RobotController implements StoppableRunnable {
	private Movement move;
	private final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private boolean running;
	private RobotNetworkHandler networkHandler;
	private RobotInterface rInterface;
	private int lineValue;
	private int backgroundValue;

	public RobotController() {
		rInterface = new RobotInterface();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		running = true;
		networkHandler = new RobotNetworkHandler();
	}

	@Override
	public void run() {
		System.out.println("On Line");
		Button.waitForAnyPress();
		lineValue = calibrate();
		System.out.println("Off Line");
		Button.waitForAnyPress();
		backgroundValue = calibrate();
		Button.waitForAnyPress();
		move = new Movement(lineValue, backgroundValue, rInterface);
		networkHandler.run();
		Action currentCommand = null;
		int pickAmount = 0;
		System.out.println("Starting receiveing commands");
		while (running) {
			try {
				// Print message
				currentCommand = (Action) networkHandler.receiveAction();

				if (currentCommand != null) {
					// Print Message
					if (currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF)) {
						pickAmount = (int) networkHandler.receiveInt();
					}

					move.nextAction(currentCommand, pickAmount);

				} else {
					System.out.println("Error: No command received");
					break;
				}
				networkHandler.sendObject(Action.ACTION_COMPLETE);
			} catch (IOException e) {
				System.out.println("Couldn't receive object in RobotController" + e.getMessage());
			}
		}
	}

	@Override
	public void stop() {
		running = false;

	}

	public int calibrate() {
		return (LEFT_SENSOR.readValue() + RIGHT_SENSOR.readValue()) / 2;
	}

	public static void main(String[] args) {
		RobotController rc = new RobotController();
		rc.run();
	}

}