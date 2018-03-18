package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import communication.PCNetworkHandler;
import interfaces.Action;
import interfaces.Pose;
import interfaces.Robot;
import job.Item;
import job.Job;
import lejos.geom.Point;

public class RouteExecution {
	private static final Logger rELogger = Logger.getLogger(RouteExecution.class);
	private Robot robot;
	private Job currentJob;
	private interfaces.Action currentCommand;
	private ArrayList<Item> ITEMS;
	private Queue<Item> itemsToDrop;
	private Queue<interfaces.Action> currentDirections;
	private PCNetworkHandler network;
	private Counter counter;

	public RouteExecution(Robot _robot, PCNetworkHandler _network, Counter _counter) {
		this.robot = _robot;
		this.network = _network;
		itemsToDrop = new LinkedList<Item>();
		counter = _counter;
	}

	public void run() {
		currentJob = robot.getActiveJob();
		ITEMS = currentJob.getITEMS();
		currentDirections = currentJob.getCurrentroute().getDirections();
		rELogger.debug(currentDirections);
		rELogger.debug(ITEMS);
		try {
			while (!currentDirections.isEmpty()) { 
				currentCommand = currentDirections.poll();
				network.sendObject(currentCommand);
				if (!(currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF))) {
					while (!counter.canMove()) {
						Thread.sleep(100);
					}
				}
				Point whereImGoing = currentJob.getCurrentroute().getCoordinates().poll();
				rELogger.debug(whereImGoing);
				if (currentCommand == interfaces.Action.PICKUP) {
					rELogger.debug(ITEMS.get(0) + " " + ITEMS.get(0).getQUANTITY());
					network.sendObject(ITEMS.get(0).getQUANTITY());
				}
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
				//robot.setCurrentPose(currentJob.getCurrentroute().getFinalPose());
				robot.setCurrentPose(getDirection(robot.getCurrentPosition(), whereImGoing));
				robot.setCurrentPosition(whereImGoing);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		robot.jobFinished();
		robot.setCurrentPose(currentJob.getCurrentroute().getFinalPose());
		counter.readyToMove(robot.getRobotName());
		rELogger.debug("Job " + currentJob.getID() + " has finished on " + robot.getRobotName() + " giving reward " + currentJob.getREWARD() + ". Robot total now " + robot.currentReward());
	}
	
	public static Pose getDirection(Point firstPoint, Point secondPoint) {
		Pose direction;
		Point difference = secondPoint.subtract(firstPoint);
		if (difference.x != 0d) {
			if (difference.x == 1d) {
				direction = Pose.POS_X;
			} else {
				direction = Pose.NEG_X;
			}
		} else {
			if (difference.y == 1d) {
				direction = Pose.POS_Y;
			} else {
				direction = Pose.NEG_Y;
			}
		}
		return direction;
	}
}