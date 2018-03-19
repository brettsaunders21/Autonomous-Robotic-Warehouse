package job;

import routeplanning.Map;
import interfaces.Action;
import interfaces.Pose;

import java.util.ArrayList;
import java.util.Arrays;
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
	private JobList jobList;
	private Robot[] robots;
	
	public JobAssignment(JobList _jobList, Counter _counter, ArrayList<Point> _drops, Robot[] _robots) {
		jobs = _jobList.getJobList();
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
		jobList = _jobList;
		robots = _robots;
	}


	public void assignJobs(Robot robot) {
		jobs = jobList.getJobList();
		Job job;
		if (!jobs.isEmpty()) {
			job = jobList.getNewJob(robot);
		}else{	
			return;	
		}
		jobs.removeIf(i -> i.getID() == job.getID());
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
		Pose initialPose = r.getCurrentPose();
		for (Item item : items) {
			if(item.getID().equals("droppoint")){
				Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,initialPose);
				itemRoute = routeMaker.generateRoute(currentRobotPosition,nearestDropoff, initialPose, getCurrentRoutes(r),timeCount);
				currentRobotPosition = nearestDropoff;
				Route routeWithDropoff = new Route(itemRoute, Action.DROPOFF);
				routes.add(routeWithDropoff);
			}else{
				itemRoute = routeMaker.generateRoute(currentRobotPosition, item.getPOSITION(), initialPose,getCurrentRoutes(r), timeCount);
				currentRobotPosition = item.getPOSITION();
				routes.add(itemRoute);
			}
			//r.setCurrentPose(itemRoute.getFinalPose()); 
			initialPose = itemRoute.getFinalPose();
			logger.trace(item);
			logger.trace(itemRoute);
			timeCount = counter.getTime();
		}
		Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,initialPose);
		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition,nearestDropoff , initialPose, getCurrentRoutes(r),timeCount);
		routes.add(dropoffRoute);
		logger.debug(initialPose);
		logger.debug(routes);
		return routes;
	}
	
	public Job getCurrentJob(){
		return recentJob;
	}
	
	private Route[] getCurrentRoutes(Robot currentRobot){
		ArrayList<Route> routes = new ArrayList<Route>();
		for (int i = 0; i < robots.length; i++) {
			if (robots[i].getActiveJob() != null 
					&& robots[i].getActiveJob().getCurrentroute() != null
					&& robots[i].getRobotName() != currentRobot.getRobotName()) {
				routes.add(robots[i].getActiveJob().getCurrentroute());
			}
		}
		return routes.toArray(new Route[routes.size()]);
	}
		
}