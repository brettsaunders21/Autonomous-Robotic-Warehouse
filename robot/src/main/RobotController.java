package main;

import java.io.IOException;

import actions.DetectJunction;
import actions.DriveForward;
import actions.LeftOfLine;
import actions.Movement;
import actions.RightOfLine;
import actions.RobotInterface;
import communication.RobotNetworkHandler;
import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
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
		move = new Movement(MID_BOUND, rInterface);
		move.start();
	}

	@Override
	public void run() {
		running = true;
		rInterface.sensorCalibrationMessage();
		userCalibration();
		lineValue = getLine();
		backgroundValue = getBackground();
		MID_BOUND = (lineValue + backgroundValue) /2;
		Button.waitForAnyPress();
		move = new Movement(MID_BOUND, rInterface);
		networkHandler.run();
		rInterface.networkMessage("Connected!");
		try {
			rInterface.setRobotName(networkHandler.receiveString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Behavior correctLeft = new LeftOfLine(MID_BOUND);
		Behavior correctRight = new RightOfLine(MID_BOUND);
		Behavior forward = new DriveForward(MID_BOUND);
		Behavior junction = new DetectJunction(MID_BOUND, networkHandler, rInterface, move);
		Arbitrator arby = new Arbitrator(new Behavior[] { forward, correctLeft, correctRight, junction});
		arby.start();
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

	@Override
	public void stop() {
		running = false;
	}

}