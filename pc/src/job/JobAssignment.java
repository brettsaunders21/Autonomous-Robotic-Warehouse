package job;

import routeplanning.Map;
import interfaces.Action;
import interfaces.Pose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
	private ConcurrentHashMap<Robot, Optional<Job>> waitingMap;
	
	public JobAssignment(JobList _jobList, Counter _counter, ArrayList<Point> _drops, Robot[] _robots) {
		jobs = _jobList.getJobList();
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
		jobList = _jobList;
		robots = _robots;
		waitingMap = new ConcurrentHashMap<>();
		for (Robot robot : _robots)
			waitingMap.put(robot, Optional.empty());
	}


	public void assignJobs(Robot robot) {
		jobs = jobList.getJobList();
		if (robot.getActiveJob() != null) {
			jobList.removeRobotFromJob(robot.getActiveJob(), robot);
		}
		Job job;
		if (waitingMap.get(robot).isPresent()) {
			job = waitingMap.get(robot).get();
			waitingMap.put(robot, Optional.empty());
		}else if (!jobs.isEmpty()) {
			Job preJob = jobList.getNewJob(robot);
			HashMap<Robot, ArrayList<Item>> itemsForRobots = splitItemsBetweenRobots(preJob);
			job = createSubJob(robot, preJob,itemsForRobots);
		}else{	
			return;	
		}
		jobList.addJobToProgressMap(job, robot);
		jobList.addRobotToJob(job, robot);
		jobs.removeIf(i -> i.getID() == job.getID());
		ArrayList<Item> orderedItems = job.getITEMS();
		//ArrayList<Item> orderedItems = tsp.orderItems(items,robot);
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
		//logger.info(items);
	
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
	
	private Job createSubJob(Robot r, Job j, HashMap<Robot, ArrayList<Item>> _itemMap) {
		for (Robot robot : robots) {
			Job subJob = new Job(j.getID(), _itemMap.get(robot));
			waitingMap.put(robot, Optional.of(subJob));
		}
		return waitingMap.get(r).get();
	}
	
	
	private Point positionEndOfJob(Robot r) {
		Point[] coords;
		if (r.getActiveJob() != null) {
			coords= r.getActiveJob().getCurrentroute().getCoordinatesArray();
		}else {
			coords = new Point[]{new Point(0,0)};
		}

		Point lastCoord = coords[coords.length-1];
		return lastCoord;
	}
	
	private HashMap<Robot, ArrayList<Item>> splitItemsBetweenRobots(Job j) {
		HashMap<Robot, Point> positionMap = new HashMap<>();
		HashMap<Robot, ArrayList<Item>> itemMap = new HashMap<>();
		ArrayList<Item> items = j.getITEMS();
		
		for (Robot r : robots) {
			positionMap.put(r, positionEndOfJob(r));
			itemMap.put(r, new ArrayList<>());
		}
		
		while(!items.isEmpty()) {
			HashMap<Robot, Item> bidPickMap = new HashMap<>();
			HashMap<Robot, Integer> bidValueMap = new HashMap<>();
			HashMap<Robot, Float> weightMap = new HashMap<>();
			for (Robot robot : robots) {
				weightMap.put(robot, 0.0f);
			}
			Item bidPick = null;
			int bidVal = Integer.MAX_VALUE;
			for (Robot robot : robots) {
				float robotWeight = weightMap.get(robot);
				Item robotPick = null;
				int robotVal = Integer.MAX_VALUE;
				for (Item item : items) {
					if (robotWeight + item.getWEIGHT() < 50.0f) {
						int cost = calculateCost(item,itemMap,robot);
						if (cost < bidVal) {
							robotPick = item;
							robotVal = cost;
						}
					}
				}
				bidPickMap.put(robot, robotPick);
				bidValueMap.put(robot, robotVal);
			}
			Robot bidPickRobot = getBidPick(bidValueMap);
			bidPick = bidPickMap.get(bidPickRobot);
			ArrayList<Item> itemListForRobot = itemMap.get(bidPickRobot);
			itemListForRobot.add(bidPick);
			itemMap.put(bidPickRobot, itemListForRobot);
			items.remove(bidPick);
			//items.removeIf(i -> i.getID() == bidPick.getID());
			Float currentWeight = weightMap.get(bidPickRobot);
			weightMap.put(bidPickRobot, currentWeight + bidPick.getWEIGHT());
			positionMap.put(bidPickRobot, bidPick.getPOSITION());
		}
		return itemMap;
	}


	private Robot getBidPick(HashMap<Robot, Integer> bidValueMap) {
		Robot smallestValueRobot = null;
		int smallestValue = Integer.MAX_VALUE;
		for (Entry<Robot, Integer> entry : bidValueMap.entrySet()) {
			if (entry.getValue() < smallestValue) {
				smallestValue = entry.getValue();
				smallestValueRobot = entry.getKey();
			}
		}
		return smallestValueRobot;
	}


	private int calculateCost(Item item, HashMap<Robot, ArrayList<Item>> itemList, Robot _robot) {
		ArrayList<Item> itemArrayList = new ArrayList<>(itemList.get(_robot));
		itemArrayList.add(item);
		int distance = tsp.calculateJobDistance(itemArrayList, _robot);
		return distance;
	}
	
		
}