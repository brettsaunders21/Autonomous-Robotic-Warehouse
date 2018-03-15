package job;

import routeplanning.Map;
import interfaces.Action;
import interfaces.Pose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import interfaces.Robot;
import lejos.geom.Point;
import main.Counter;
import routeplanning.AStar;
import routeplanning.Route;

public class JobAssignment {
	
	private ArrayList<Job> jobs;
	private Robot[] robotsArray;
	private Map map = Map.generateRealWarehouseMap();
	private Counter counter;
	final static Logger logger = Logger.getLogger(JobAssignment.class);
	private Point dropoff1 = new Point(2,4);
	public Job currentProcessingJob;
	private ArrayList<Point> drops;
	private AStar routeMaker = new AStar(map);
	private Robot currentRobot;
	
	public JobAssignment(ArrayList<Job> j, Robot[] r, Counter _counter, ArrayList<Point> _drops) {
		robotsArray = r;
		jobs = j;
		counter = _counter;
		drops = _drops;
		logger.setLevel(Level.DEBUG);
	}


	public void assignJobs(Robot robot) {
		Job job = getClosestJob(robot);
		ArrayList<Item> items = job.getITEMS();
		ArrayList<Item> orderedItems = orderItems(items,robot,job);
		ArrayList<Route> routes = calculateRoute(robot, map, job, orderedItems);
		ArrayList<Action> actions = calculateActions(routes);
		Route routeForAllItems = new Route(routes, actions);
		Route routeWithDropoff = new Route(routeForAllItems, Action.DROPOFF);
		job.assignCurrentroute(routeWithDropoff);
		robot.setActiveJob(job);
		jobs.remove(job);
		logger.info(robot);
		logger.info(routeWithDropoff);
		logger.info(job);
		logger.info(items);
	
	}

	private ArrayList<Action> calculateActions(ArrayList<Route> routes) {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 1; i < routes.size(); i++) {
			actions.add(Action.PICKUP);
		}
		return actions;
	}
	
	//Brett
	private Job getClosestJob(Robot r) {
		Point currentRobotPosition = r.getCurrentPosition();
		AStar routeMaker = new AStar(map);
		Job closestJob = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (int i =0; i < 3; i++) {
			Job tempJob = jobs.get(i);
			int tempDistance = routeMaker.generateRoute(currentRobotPosition, tempJob.getITEMS().get(0).getPOSITION(), r.getCurrentPose(), new Route[] {}, 0).getLength();
			if (tempDistance < shortestDistance) {
				closestJob = tempJob;
				shortestDistance = tempDistance;
			}
		}
		return closestJob;
	}

	private ArrayList<Route> calculateRoute(Robot r, Map map, Job job, ArrayList<Item> items) {
		int timeCount = counter.getTime();
		Point currentRobotPosition = r.getCurrentPosition();
		ArrayList<Route> routes = new ArrayList<Route>();
		for (Item item : items) {
			Route itemRoute = routeMaker.generateRoute(currentRobotPosition, item.getPOSITION(), r.getCurrentPose(), new Route[] {}, timeCount);
			routes.add(itemRoute);
			r.setCurrentPose(itemRoute.getFinalPose()); 
			currentRobotPosition = item.getPOSITION();
			logger.trace(item);
			logger.trace(itemRoute);
			timeCount = counter.getTime();
		}
		Point nearestDropoff = nearestDropPoint(currentRobotPosition,r.getCurrentPose(), drops);
		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition,nearestDropoff , r.getCurrentPose(), new Route[] {},timeCount);
		routes.add(dropoffRoute);
		r.setCurrentPose(dropoffRoute.getFinalPose());
		logger.debug(r.getCurrentPose());
		logger.debug(routes);
		return routes;
	}
	
	private Point nearestDropPoint(Point currentLocation, Pose pose, ArrayList<Point> dropoffs) {
		Point closestPoint = dropoffs.get(0);
		int closestPointDistance = Integer.MAX_VALUE;
		for (Point point : dropoffs) {
			int routeToDrop1 = routeMaker.generateRoute(currentLocation, point, pose, new Route[] {}, 0).getLength();
			if (routeToDrop1 < closestPointDistance) {
				closestPointDistance = routeToDrop1;
				closestPoint = point;
			}
		}
		logger.debug("Closest dropoff point to " + currentLocation + " is "+ closestPoint);
		return closestPoint;
	}
	
	private ArrayList<Item> orderItems(ArrayList<Item> items, Robot robot, Job job) {
		ArrayList<Item> orderedItems = new ArrayList<>();
		ArrayList<Item> originalItems = new ArrayList<>(items);
		Item closestItem = new Item(null, 0, 0, robot.getCurrentPosition(), 0);
		while (!items.isEmpty()) {
			closestItem = nearestItemToPoint(closestItem.getPOSITION(), items);
			items.remove(closestItem);
			orderedItems.add(closestItem);
		}
		Route testroute = new Route(calculateRoute(robot, map, job, orderedItems));
		Route normalRoute = new Route(calculateRoute(robot, map, job, originalItems));
		logger.trace("Route optimised from " + normalRoute.getLength() + " to " + testroute.getLength());
		return orderedItems;
	}
	
	private Item nearestItemToPoint(Point point,  ArrayList<Item> items) {
		Item nearestItemSoFar = items.get(0);
		int smallestDistance = Integer.MAX_VALUE ;
		for (Item item : items) {
			int distance = routeMaker.generateRoute(point, item.getPOSITION(), Pose.POS_X,  new Route[] {}, 0).getLength();
			if (distance < smallestDistance) {
				nearestItemSoFar = item;
				smallestDistance = distance;
			}
		}
		return nearestItemSoFar;
	}


	public Job getCurrentJob(Robot robot1) {
		// TODO Auto-generated method stub
		return robot1.getActiveJob();
	}
	
}
