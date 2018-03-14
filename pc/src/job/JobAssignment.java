package job;

import routeplanning.Map;
import interfaces.Action;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	public JobAssignment(ArrayList<Job> j, Robot[] r, Counter _counter, ArrayList<Point> _drops) {
		robotsArray = r;
		jobs = j;
		counter = _counter;
		drops = _drops;
		logger.setLevel(Level.OFF);
	}


	public void assignJobs(Robot robot) {
		Job job = getClosestJob(robot);
		currentProcessingJob = job;
		ArrayList<Item> items = job.getITEMS();
		ArrayList<Route> routes = calculateRoute(robot, map, job, items);
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
		System.out.println(timeCount);
		Point currentRobotPosition = r.getCurrentPosition();
		AStar routeMaker = new AStar(map);
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
		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition, nearestDropPoint(currentRobotPosition), r.getCurrentPose(), new Route[] {},timeCount);
		routes.add(dropoffRoute);
		r.setCurrentPose(dropoffRoute.getFinalPose());
		logger.debug(r.getCurrentPose());
		logger.debug(routes);
		return routes;
	}
	
	private Point nearestDropPoint(Point currentLocation) {
		return drops.get(0);
	}
}
