package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import communication.PCNetworkHandler;
import interfaces.Robot;
import job.Item;
import job.Job;

public class RouteExecution extends Thread {

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
					network.sendObject(itemsToDrop.peek());
				if (!network.receiveAction().equals("ACTION COMPLETE")) {
					//Error
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
				} else if (currentCommand.equals(interfaces.Action.DROPOFF)) {
					robot.setWeight(robot.getWeight() - 0);
					itemsToDrop.poll();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		robot.jobFinished();
	}
}