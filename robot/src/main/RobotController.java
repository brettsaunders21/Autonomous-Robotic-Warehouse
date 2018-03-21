package main;

import java.io.IOException;
import actions.Movement;
import actions.RobotInterface;
import communication.RobotNetworkHandler;
import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
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
	private int high = 0; // darker is higher value
	private int low = 999;

	public RobotController() {
		LEFT_SENSOR = new LightSensor(SensorPort.S1);
		RIGHT_SENSOR = new LightSensor(SensorPort.S3);
		rInterface = new RobotInterface();
		running = true;
		MID_BOUND = 0;
		networkHandler = new RobotNetworkHandler();
	}

	@Override
	public void run() {
		rInterface.sensorCalibrationMessage();
		userCalibration();
		lineValue = getLine();
		backgroundValue = getBackground();
		MID_BOUND = (lineValue + backgroundValue) / 2;
		Button.waitForAnyPress();
		move = new Movement(MID_BOUND, rInterface);
		networkHandler.run();
		rInterface.networkMessage("Connected!");
		try {
			rInterface.setRobotName(networkHandler.receiveString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Action currentCommand = null;
		int pickAmount = 0;
		while (running) {
			try {
				rInterface.setJobCode(networkHandler.receiveInt());
				rInterface.waitingForOrdersMessage();
				currentCommand = (Action) networkHandler.receiveAction();
				System.out.println(currentCommand);
				if (currentCommand != null) {
					if (!currentCommand.equals(Action.CANCEL)) {
						rInterface.setCurrentDirection(currentCommand);
						rInterface.networkMessage(currentCommand);
						if (currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF)) {
							pickAmount = (int) networkHandler.receiveInt();
						}
						move.nextAction(currentCommand, pickAmount);
					} else {
						rInterface.dropAll();
					}
				} else {
					System.out.println("Error: No command received");
					break;
				}
				networkHandler.sendObject(Action.ACTION_COMPLETE);
			} catch (IOException e) {
				System.out.println("Couldn't receive object in RobotController " + e.getMessage());
			}
		}
	}

	@Override
	public void stop() {
		running = false;

	}

	private int getAverageLight() {
		int value = (LEFT_SENSOR.getNormalizedLightValue() + RIGHT_SENSOR.getNormalizedLightValue()) / 2;
		return value;
	}

	public int getLine() {
		return this.high;
	}

	public int getBackground() {
		return this.low;
	}

	private void userCalibration() {
		System.out.println("Place the bot's sensors over line.");
		Button.waitForAnyPress();
		high = getAverageLight();
		System.out.println("Place the bot's sensors over background.");
		Button.waitForAnyPress();
		low = getAverageLight();
	}

	public static void main(String[] args) {
		RobotController rc = new RobotController();
		rc.run();
	}

}