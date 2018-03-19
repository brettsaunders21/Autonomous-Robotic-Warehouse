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
import routeplanning.AStar;
import routeplanning.Map;
import routeplanning.Route;

public class RouteExecution {
	private static final Logger rELogger = Logger.getLogger(RouteExecution.class);
	private Robot robot;
	private Job currentJob;
	private interfaces.Action currentCommand;
	private ArrayList<Item> ITEMS;
	private Queue<Item> itemsToDrop;
	private Queue<Action> currentDirections;
	private PCNetworkHandler network;
	private Counter counter;
	private PointsHeld heldPoints;
	private AStar routeMaker;
	private Robot[] robots;
	private Counter time;

	public RouteExecution(Robot _robot, PCNetworkHandler _network, Counter _counter, PointsHeld _heldPoints, Robot[] _robots, Counter _time) {
		this.robot = _robot;
		this.network = _network;
		itemsToDrop = new LinkedList<Item>();
		counter = _counter;
		heldPoints = _heldPoints;
		routeMaker = new AStar(Map.generateRealWarehouseMap());
		robots = _robots;
		time = _time;
	}

	public void run() {
		try {
		network.sendObject(robot.getRobotName());
		currentJob = robot.getActiveJob();
		network.sendObject(currentJob.getID());
		ITEMS = currentJob.getITEMS();
		currentDirections = currentJob.getCurrentroute().getDirections();
		Point[] arrayOfCoords = currentJob.getCurrentroute().getCoordinatesArray();
		int instructionCounter = -1;
		rELogger.debug(currentDirections);
		rELogger.debug(ITEMS);
			while (!currentDirections.isEmpty()) { 
				instructionCounter++;
				currentCommand = currentDirections.poll();
				if (robot.getJobCancelled()) {
					network.sendObject(Action.CANCEL);
					break;
				}
				counter.readyToMove(robot.getRobotName());
				if (!(currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF) || currentCommand.equals(Action.HOLD))) {
					while (!counter.canMove()) {
						Thread.sleep(100);
					}
				}
				else {
					Point heldCoord = arrayOfCoords[instructionCounter];
					heldPoints.holdAt(heldCoord);
				}
				network.sendObject(currentCommand);
				counter.iMoved();
				Point whereImGoing = currentJob.getCurrentroute().getadjustForCollisionsCoordinates().poll();
				network.sendObject(whereImGoing);
				rELogger.debug(whereImGoing);
				if (currentCommand == Action.PICKUP) {
					rELogger.debug(ITEMS.get(0) + " " + ITEMS.get(0).getQUANTITY());
					network.sendObject(ITEMS.get(0).getQUANTITY());
				}
				if (currentCommand == Action.DROPOFF)
					network.sendObject(itemsToDrop.peek().getQUANTITY());
				if (!network.receiveAction().equals(Action.ACTION_COMPLETE)) {
					rELogger.error(robot.getRobotName() + " did not complete an action. Canceling Job");
					robot.cancelJob();
					break;
				}
				if (currentCommand.equals(Action.HOLD)) {
					//hold instruction cannot be the last instruction
					Point heldCoord = arrayOfCoords[instructionCounter+1];
					while (heldPoints.isStillHeld(heldCoord)) {
						Thread.sleep(100);
					}
				}
				if ((currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF) || currentCommand.equals(Action.HOLD))) {
					Point heldCoord = arrayOfCoords[instructionCounter];
					heldPoints.freeUp(heldCoord);
					Route currentRoute = currentJob.getCurrentroute();
					Route[] routesRunning = new Route[robots.length];
					for (int i = 0; i < robots.length; i++) {
						routesRunning[i] = robot.getActiveJob().getCurrentroute();
					}
					currentRoute = routeMaker.adjustForCollisions(currentRoute,routesRunning,time.getTime(),instructionCounter);
					currentDirections = currentRoute.getDirections();
					
				}
				if (currentCommand == Action.PICKUP) {
					robot.setWeight(robot.getWeight() + ITEMS.get(0).getTOTAL_WEIGHT());
					itemsToDrop.add(ITEMS.get(0));
					ITEMS.remove(0);
					rELogger.debug(robot.getRobotName() + " picked up items");
				} else if (currentCommand.equals(Action.DROPOFF)) {
					robot.setWeight(0);
					itemsToDrop.poll();
					rELogger.debug(robot.getRobotName() + " dropped off items");
				}
				robot.setCurrentPose(getDirection(robot.getCurrentPosition(), whereImGoing));
				robot.setCurrentPosition(whereImGoing);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if (!robot.getJobCancelled()) {
			robot.jobFinished();
			robot.setCurrentPose(currentJob.getCurrentroute().getFinalPose());
			rELogger.debug("Job " + currentJob.getID() + " has finished on " + robot.getRobotName() + " giving reward " + currentJob.getREWARD() + ". Robot total now " + robot.currentReward());
		}
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