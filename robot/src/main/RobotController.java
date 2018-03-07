package main;

import java.io.IOException;

import actions.Movement;
import communication.RobotNetworkHandler;
import communication.CommunicationData;
import interfaces.Action;
import lejos.nxt.LightSensor;
import lejos.util.Delay;
import rp.systems.StoppableRunnable;

public class RobotController implements StoppableRunnable{
	private Movement move;
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private boolean running;
	private RobotNetworkHandler networkHandler;
	
	public RobotController() {
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		move = new Movement(calibrate());
		running = true;
		networkHandler = new RobotNetworkHandler();
	}

	@Override
	public void run() {
		networkHandler.run();
		Action currentCommand = null;
		int pickAmount = 0;
		while (running) {
			//Print message
			try {
				currentCommand = (Action) networkHandler.receiveObject(CommunicationData.ACTION);
				switch (currentCommand) {
				case LEFT:
					break;
				case FORWARD:
					break;
				case RIGHT:
					break;
				case BACKWARD:
					break;
				case WAIT:
					break;
				case PICKUP:
					pickAmount = (int) networkHandler.receiveObject(CommunicationData.INT);
					move.nextAction(currentCommand, pickAmount);
					break;
				case DROPOFF:
					break;
				case TURN_180:
					break;
				case TURN_LEFT:
					break;
				case TURN_RIGHT:
					break;
				case CANCEL:
					break;
				case SHUTDOWN:
					break;
				default:
					System.out.println("Error: No valid command received");
					break;
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			networkHandler.sendObject("ACTION COMPLETE");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		running = false;
		
	}
	
	public int calibrate() {
		Delay.msDelay(500);
		return (LEFT_SENSOR.readValue() + RIGHT_SENSOR.readValue()) / 2;
	}
	
	public static void main(String[] args) {
		RobotController rc = new RobotController();
		rc.run();
	}

}