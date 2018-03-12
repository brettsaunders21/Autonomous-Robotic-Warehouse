package job;

import routeplanning.Map;
import interfaces.Action;
import interfaces.Pose;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import interfaces.Robot;
import routeplanning.AStar;
import routeplanning.Route;

public class JobAssignment {
	
	private ArrayList<Job> jobs;
	private Robot[] robotsArray;
	private Map map = Map.generateRealWarehouseMap();
	private int time;
	final static Logger logger = Logger.getLogger(JobAssignment.class);
	
	public JobAssignment(ArrayList<Job> j, Robot[] r, int t) {
		robotsArray = r;
		jobs = j;
		time = t;
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
		AStar routeMaker = new AStar(map);
		ArrayList<Route> routes = new ArrayList<Route>();
		for (Item item : items) {
			Route itemRoute = routeMaker.generateRoute(r.getCurrentPosition(), item.getPOSITION(), r.getCurrentPose(), new Route[] {}, time);
			routes.add(itemRoute);
			r.setCurrentPose(itemRoute.getFinalPose()); 
			logger.trace(item);
			logger.trace(itemRoute);
		}
		logger.debug(currentPose);
		logger.debug(routes);
		return routes;
	}
	
}