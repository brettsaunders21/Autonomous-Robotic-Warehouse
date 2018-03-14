package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import communication.PCNetworkHandler;
import interfaces.Action;
import interfaces.Robot;
import job.Item;
import job.Job;

public class RouteExecution {
	private static final Logger rELogger = Logger.getLogger(RouteExecution.class);
	private Robot robot;
	private Job currentJob;
	private interfaces.Action currentCommand;
	private ArrayList<Item> ITEMS;
	private Queue<Item> itemsToDrop;
	private Queue<interfaces.Action> currentDirections;
	private PCNetworkHandler network;

	public RouteExecution(Robot _robot, PCNetworkHandler _network) {
		this.robot = _robot;
		this.network = _network;
		itemsToDrop = new LinkedList<Item>();
	}

	public void run() {
		currentJob = robot.getActiveJob();
		ITEMS = currentJob.getITEMS();
		currentDirections = currentJob.getCurrentroute().getDirections();

		try {
			while (!currentDirections.isEmpty()) { 
				currentCommand = currentDirections.poll();
				network.sendObject(currentCommand);
				if (currentCommand == interfaces.Action.PICKUP)
					network.sendObject(ITEMS.get(0).getQUANTITY());
				if (currentCommand == interfaces.Action.DROPOFF)
					network.sendObject(itemsToDrop.peek().getQUANTITY());
				if (!network.receiveAction().equals(Action.ACTION_COMPLETE)) {
					rELogger.error(robot.getRobotName() + " did not complete an action. Canceling Job");
					robot.cancelJob();
					break;
				}
				if (currentJob.isCanceled()) {
					robot.cancelJob();
				}
				if (currentCommand == interfaces.Action.PICKUP) {
					robot.setWeight(robot.getWeight() + 0);
					itemsToDrop.add(ITEMS.get(0));
					ITEMS.remove(0);
					rELogger.debug(robot.getRobotName() + " picked up items");
				} else if (currentCommand.equals(interfaces.Action.DROPOFF)) {
					robot.setWeight(robot.getWeight() - 0);
					itemsToDrop.poll();
					rELogger.debug(robot.getRobotName() + " dropped off items");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		rELogger.debug("Job " + currentJob.getID() + " has finished on " + robot.getRobotName() + " giving reward " + currentJob.getREWARD() + ". Robot total now " + robot.currentReward());
		robot.jobFinished();
	}
}