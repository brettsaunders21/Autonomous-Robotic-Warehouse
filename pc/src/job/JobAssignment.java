package job;

import routeplanning.Map;
import interfaces.Action;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import interfaces.Robot;
import lejos.geom.Point;
import main.Counter;
import routeplanning.AStar;
import routeplanning.Route;

public class JobAssignment {
	
	private ArrayList<Job> jobs;
	private Map map = Map.generateRealWarehouseMap();
	private Counter counter;
	final static Logger logger = Logger.getLogger(JobAssignment.class);
	private ArrayList<Point> drops;
	private AStar routeMaker = new AStar(map);
	private TSP tsp;
	private JobSelection jS;
	
	public JobAssignment(ArrayList<Job> j, Counter _counter, ArrayList<Point> _drops, JobSelection _jS) {
		jobs = j;
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
		jS = _jS;
	}


	public void assignJobs(Robot robot) {
		Job job = jS.getJob(jobs, robot);
		ArrayList<Item> items = job.getITEMS();
		ArrayList<Item> orderedItems = tsp.orderItems(items,robot);
		job.setItems(orderedItems);
		//Add weight droppoints
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
		Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,r.getCurrentPose());
		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition,nearestDropoff , r.getCurrentPose(), new Route[] {},timeCount);
		routes.add(dropoffRoute);
		r.setCurrentPose(dropoffRoute.getFinalPose());
		logger.debug(r.getCurrentPose());
		logger.debug(routes);
		return routes;
	}
		
}
