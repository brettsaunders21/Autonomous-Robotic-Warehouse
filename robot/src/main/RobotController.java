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

	private int lineValue = 0;
	private int backgroundValue = 0;
	int MID_BOUND;
	private int high = 0; //darker is higher value
	private int low = 999;

	public RobotController() {
		rInterface = new RobotInterface();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		running = true;
		MID_BOUND = 0;
		networkHandler = new RobotNetworkHandler();
	}

	@Override
	public void run() {
		userCalibration();
		lineValue = getLine();
		backgroundValue = getBackground();
		MID_BOUND = (lineValue + backgroundValue) /2;
		System.out.println("line" + lineValue);
		System.out.println("background " + backgroundValue);
		System.out.println("mid" + MID_BOUND);
		Button.waitForAnyPress();
		move = new Movement(MID_BOUND, rInterface);
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

					try {
						networkHandler.sendObject(Action.ACTION_COMPLETE);
					} catch (IOException e) {
						System.out.println("Couldn't send object in RobotController" + e.getMessage());
					}
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
	
	private int getAverageLight() {
		int value = (LEFT_SENSOR.getNormalizedLightValue() + RIGHT_SENSOR.getNormalizedLightValue()) / 2;
		System.out.println(value);
		return value;
	}

	public int getLine() {
		return this.high;
	}
	
	public int getBackground() {
		return this.low;
	}
	
	private void userCalibration() {
		for (int i = 0; i < 3; i ++) {
			System.out.println("Place the bot's sensors over line (" + i + ").");
			Button.waitForAnyPress();
			int readValue = getAverageLight();
			if (readValue > high) high = readValue;
		}
		
		for (int i = 0; i < 3; i ++) {
			System.out.println("Place the bot's sensors over background (" + i + ").");
			Button.waitForAnyPress();
			int readValue = getAverageLight();
			if (readValue < low) low = readValue;
		}
		
	}

	public static void main(String[] args) {
		RobotController rc = new RobotController();
		rc.run();
	}

}
