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
import job.JobList;
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
	private JobList jobList;
	private Robot[] robots;
	private AStar routeMaker;


	
	public RouteExecution(Robot _robot, PCNetworkHandler _network, Counter _counter, PointsHeld _heldPoints,
			Robot[] _robots, JobList _jobList) {

		this.robot = _robot;
		this.network = _network;
		itemsToDrop = new LinkedList<Item>();
		counter = _counter;
		heldPoints = _heldPoints;
		jobList = _jobList;
		routeMaker = new AStar(Map.generateRealWarehouseMap());
		robots = _robots;
	}

	
	public void run() {
		try {
			currentJob = robot.getActiveJob();
			//retrieving Arraylist of items
			ITEMS = currentJob.getITEMS();
			currentDirections = currentJob.getCurrentroute().getDirections();
			
			heldPoints.holdAt(currentJob.getCurrentroute().getStartPoint(), new Point(-1, -1));
			
			Point previousPoint = currentJob.getCurrentroute().getStartPoint();
			
			rELogger.debug(currentDirections);
			rELogger.debug(currentJob.getCurrentroute().getCoordinates());
			rELogger.debug(currentJob.getCurrentroute().getStartTime());
			rELogger.debug(counter.getTime());
			rELogger.debug(ITEMS);
			//sending instructions (actions)
			while (!currentDirections.isEmpty()) {
				if (jobList.getJob(robot.getActiveJob().getID()) != null) {
					if (jobList.getJob(robot.getActiveJob().getID()).isCanceled()) {
						robot.cancelJob();
						network.sendObject(Action.CANCEL);
						robot.setWeight(0);
						rELogger.debug(robot.getActiveJob() + " has been canceled on " + robot.getRobotName());
						if (!network.receiveAction().equals(Action.ACTION_COMPLETE)) {
							rELogger.error("Never receivered response about cancelation from " + robot.getRobotName());
						}
						break;
					}
				}
				currentCommand = currentDirections.poll();
				Point whereImGoing = currentJob.getCurrentroute().getCoordinates().poll();
				
				if (!(currentCommand.equals(Action.PICKUP)||currentCommand.equals(Action.DROPOFF))) {
					boolean messageSent = false;
					counter.isNonMove(robot.getRobotName());
					while (heldPoints.isStillHeld(whereImGoing)) {
						if (!messageSent) {
							rELogger.debug("Waiting for coordinate (" + whereImGoing.x + ", " + whereImGoing.y + ") to free up.");
							messageSent = true;
						}
						Thread.sleep(1);
					}
					counter.isMoveable(robot.getRobotName());
				}
				
				heldPoints.holdAt(whereImGoing, previousPoint);
				previousPoint = whereImGoing;
				if (robot.getJobCancelled()) {
					network.sendObject(Action.CANCEL);
					break;
				}
				// if action received is not pickup, drop-off or hold, robot will sleep
				counter.readyToMove(robot.getRobotName());
				if (!(currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF)
						|| currentCommand.equals(Action.HOLD))) {
					while (!counter.canMove()) {
						Thread.sleep(100);
					}
				} else {
					counter.isNonMove(robot.getRobotName());
				}
				//sending job ID and currentCommand to RobotController
				network.sendObject(currentJob.getID());
				network.sendObject(currentCommand);
				counter.iMoved();
				rELogger.debug(whereImGoing);
				// if PICKUP is received, no of items to be picked up is sent to RobotController
				if (currentCommand == Action.PICKUP) {
					rELogger.debug(ITEMS.get(0) + " " + ITEMS.get(0).getQUANTITY());
					network.sendObject(ITEMS.get(0).getQUANTITY());
				}
				// if DROPOFF is received, no of items to be dropped off is sent to RobotController
				if (currentCommand == Action.DROPOFF) {
					network.sendObject(itemsToDrop.peek().getQUANTITY());
				}
				//if action complete message is not received job is cancelled
				if (!network.receiveAction().equals(Action.ACTION_COMPLETE)) {
					rELogger.error(robot.getRobotName() + " did not complete an action. Canceling Job");
					robot.cancelJob();
					break;
				}
				//if HOLD is received, robot will pause
				if (currentCommand.equals(Action.HOLD)) {
					counter.isMoveable(robot.getRobotName());
				}
				//robot will run route
				if ((currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF)
						|| currentCommand.equals(Action.HOLD))) {
					counter.isMoveable(robot.getRobotName());
					Route currentRoute = currentJob.getCurrentroute();
					Route[] routesRunning = new Route[robots.length - 1];
					int j = 0;
					for (int i = 0; i < robots.length; i++) {
						if (!robots[i].equals(robot)) {
							rELogger.debug(robots[i].getRobotName());
							routesRunning[j] = robots[j].getActiveJob().getCurrentroute();
							j++;
						}
					}
					currentRoute = routeMaker.adjustForCollisions(currentRoute, routesRunning, counter.getTime());
					rELogger.debug(whereImGoing);
					rELogger.debug(currentRoute.getStartPoint());
					rELogger.debug(currentRoute.getStartTime());
					rELogger.debug(counter.getTime());
					rELogger.debug(currentRoute.getStartPose());
					rELogger.debug(currentRoute.getDirections());
					rELogger.debug(currentRoute.getCoordinates());
					currentJob.assignCurrentroute(currentRoute);
					currentDirections = currentRoute.getDirections();

				}
				//setting weight for number of items, weight being adjusted for pickup and drop-off
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

				if (!robot.getCurrentPosition().equals(whereImGoing)) {
					robot.setCurrentPose(getDirection(robot.getCurrentPosition(), whereImGoing));
				}
				robot.setCurrentPosition(whereImGoing);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		//checking job finished and calculating reward
		if (!robot.getJobCancelled()) {
			robot.jobFinished();
			robot.setCurrentPose(currentJob.getCurrentroute().getFinalPose());
			rELogger.debug("Job " + currentJob.getID() + " has finished on " + robot.getRobotName() + " giving reward "
					+ currentJob.getREWARD() + ". Robot total now " + robot.currentReward());
		}
	}

	/**
	 * gets the direction for the robot to travel
	 * @param firstPoint
	 * @param secondPoint
	 * @return direction
	 */
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