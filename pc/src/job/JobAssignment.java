package job;

import routeplanning.Map;
import interfaces.Action;

import java.awt.font.NumericShaper.Range;
import java.util.ArrayList;

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
	private Counter time;
	final static Logger logger = Logger.getLogger(JobAssignment.class);
	private Point dropoff1 = new Point(2,4);
	
	public JobAssignment(ArrayList<Job> j, Robot[] r, Counter counter) {
		robotsArray = r;
		jobs = j;
		time = counter;
		logger.setLevel(Level.OFF);
	}

	public void assignJobs(Robot robot) {
		Job job = jobs.get(0);
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

	private ArrayList<Route> calculateRoute(Robot r, Map map, Job job, ArrayList<Item> items) {
		int timeCount = time.getCounter();
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
			timeCount = time.getCounter();
		}
		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition, dropoff1, r.getCurrentPose(), new Route[] {},timeCount);
		routes.add(dropoffRoute);
		r.setCurrentPose(dropoffRoute.getFinalPose());
		logger.debug(r.getCurrentPose());
		logger.debug(routes);
		return routes;
	}
	
}
