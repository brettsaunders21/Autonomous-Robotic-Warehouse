package job;

import routeplanning.Map;
import interfaces.Action;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import interfaces.Robot;
import lejos.geom.Point;
import main.Counter;
import routeplanning.AStar;
import routeplanning.Route;

public class JobAssignment {

	
	private List<Job> jobs;
	private Map map = Map.generateRealWarehouseMap();
	private Counter counter;
	final static Logger logger = Logger.getLogger(JobAssignment.class);
	private ArrayList<Point> drops;
	private AStar routeMaker = new AStar(map);
	private TSP tsp;
	private Job recentJob;
	private JobSelection jS;
	
	public JobAssignment(ArrayList<Job> j, Counter _counter, ArrayList<Point> _drops, JobSelection _jS) {
		jobs = Collections.synchronizedList(new ArrayList<Job>(j));
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
		jS = _jS;
	}


	public void assignJobs(Robot robot) {
		Job job;
		if (!jobs.isEmpty()) {
			job = jS.getJob(jobs, robot);
		}else{	
			return;	
		}
		System.out.println("Before size " + jobs.size());
		jobs.removeIf(i -> i.getID() == job.getID());
		System.out.println("After size " + jobs.size());
		ArrayList<Item> items = job.getITEMS();
		ArrayList<Item> orderedItems = tsp.orderItems(items,robot);
		job.setItems(orderedItems);
		ArrayList<Route> routes = calculateRoute(robot, map, job, orderedItems);
		ArrayList<Action> actions = calculateActions(routes);
		Route routeForAllItems = new Route(routes, actions);
		Route routeWithDropoff = new Route(routeForAllItems, Action.DROPOFF);
		job.assignCurrentroute(routeWithDropoff);
		robot.setActiveJob(job);
		recentJob = job;
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
		Route itemRoute;
		for (Item item : items) {
			if(item.getID().equals("droppoint")){
				Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,r.getCurrentPose());
				itemRoute = routeMaker.generateRoute(currentRobotPosition,nearestDropoff, r.getCurrentPose(), new Route[] {},timeCount);
				currentRobotPosition = nearestDropoff;
				Route routeWithDropoff = new Route(itemRoute, Action.DROPOFF);
				routes.add(routeWithDropoff);
			}else{
				itemRoute = routeMaker.generateRoute(currentRobotPosition, item.getPOSITION(), r.getCurrentPose(), new Route[] {}, timeCount);
				currentRobotPosition = item.getPOSITION();
				routes.add(itemRoute);
			}
			r.setCurrentPose(itemRoute.getFinalPose()); 
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
	
	public Job getCurrentJob(){
		return recentJob;
	}
		
}