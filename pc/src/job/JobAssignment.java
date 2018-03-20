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
	private ConcurrentHashMap<Robot, Point> positionMap;
	
	public JobAssignment(JobList _jobList, Counter _counter, ArrayList<Point> _drops, Robot[] _robots) {
		jobs = _jobList.getJobList();
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
		jobList = _jobList;
		robots = _robots;
		waitingMap = new ConcurrentHashMap<>();
		positionMap = new ConcurrentHashMap<>();
		for (Robot robot : _robots)
			waitingMap.put(robot, Optional.empty());
	}



	public synchronized void assignJobs(Robot robot) {
		logger.info("");
		//jobs = jobList.getJobList();
		if (robot.getActiveJob() != null) {
			jobList.removeRobotFromJob(robot.getActiveJob(), robot);
			//jobs = jobList.getJobList();
		}
		Job job;
		if (waitingMap.get(robot).isPresent()) {
			job = waitingMap.get(robot).get();
			waitingMap.put(robot, Optional.empty());
		}else if (!jobs.isEmpty()) {
			Job preJob = jobList.getNewJob(robot);
			ConcurrentHashMap<Robot, ArrayList<Item>> itemsForRobots = splitItemsBetweenRobots(preJob);
			preJob.setDropLocation(tsp.bestDropPoint(itemsForRobots, preJob));
			logger.info("Job " + preJob.getID() + " drop point is " + preJob.getDropLocation());
			job = createSubJob(robot, preJob,itemsForRobots);
			//job.setDropLocation(tsp.bestDropPoint(itemsForRobots, job));
			waitingMap.put(robot, Optional.empty());
			jobs.removeIf(i -> i.getID() == preJob.getID());
		}else{	
			return;	
		}

		if (job == null) {
			logger.info("Null job for " + robot.getRobotName());
			return;
		}
		jobList.addJobToProgressMap(job, robot);

		jobList.addRobotToJob(job, robot);

		//jobs.removeIf(i -> i.getID() == job.getID());

		ArrayList<Item> orderedItems = job.getITEMS();
		//ArrayList<Item> orderedItems = tsp.orderItems(items,robot);

		job.setItems(orderedItems);
		logger.info(job);
		logger.info("Calculate route for " + robot.getRobotName());
		logger.info("Job drop " + job.getDropLocation());
		logger.info("Robot position " + robot.getCurrentPosition());
		logger.info("Ordered items " + orderedItems);
		ArrayList<Route> routes = calculateRoute(robot, map, job, orderedItems);
		logger.info("Route calculated for job " + job.getID() + " for robot " + robot.getRobotName());
		ArrayList<Action> actions = calculateActions(routes);

		Route routeForAllItems = new Route(routes, actions);

		Route routeWithDropoff = new Route(routeForAllItems, Action.DROPOFF);

		job.assignCurrentroute(routeWithDropoff);
		robot.setActiveJob(job);
		recentJob = job;
		logger.debug(robot);
		logger.debug(routeWithDropoff);
		logger.debug(job);

	
	}

	private ArrayList<Action> calculateActions(ArrayList<Route> routes) {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 1; i < routes.size(); i++) {
			actions.add(Action.PICKUP);
		}
		return actions;
	}
	

	private  ArrayList<Route> calculateRoute(Robot r, Map map, Job job, ArrayList<Item> items) {
		int timeCount = counter.getTime();
		Point currentRobotPosition = r.getCurrentPosition();
		ArrayList<Route> routes = new ArrayList<Route>();
		Route itemRoute;
		Pose initialPose = r.getCurrentPose();
		List<Route> generatedList = new ArrayList<Route>(getCurrentRoutes(r,robots)) ;
		logger.info("Routes for AStar " + generatedList);
		Route[] routesForAStar = new Route[generatedList.size()];
		logger.info(routesForAStar.length + " length");
		logger.info("Routes for AStar as array " + routesForAStar);
		routesForAStar = generatedList.toArray(routesForAStar);

		for (Item item : items) {
			if(item.getID().equals("droppoint")){
				//Point nearestDropoff = tsp.bestDropPoint(positionMap);
				//Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,initialPose);
				itemRoute = routeMaker.generateRoute(currentRobotPosition,job.getDropLocation(), initialPose, routesForAStar,timeCount);
				currentRobotPosition = job.getDropLocation();
				Route routeWithDropoff = new Route(itemRoute, Action.DROPOFF);
				routes.add(routeWithDropoff);

			}else{
				itemRoute = routeMaker.generateRoute(currentRobotPosition, item.getPOSITION(), initialPose,routesForAStar, timeCount);

				currentRobotPosition = item.getPOSITION();
				routes.add(itemRoute);
			}
			initialPose = itemRoute.getFinalPose();
			logger.trace(item);
			logger.trace(itemRoute);
			timeCount = counter.getTime();
		}
		//Point bestDropPoint = tsp.bestDropPoint(positionMap);
		//Point nearestDropoff = tsp.nearestDropPoint(currentRobotPosition,initialPose);
		logger.info(currentRobotPosition);
		logger.info(job.getDropLocation());
		logger.info(initialPose);
		//logger.info(getCurrentRoutes(r));
		logger.info(timeCount);

		Route dropoffRoute = routeMaker.generateRoute(currentRobotPosition,job.getDropLocation() , initialPose, routesForAStar ,timeCount);
		routes.add(dropoffRoute);
		logger.info("Dropoff for " + r.getRobotName() + " is " + job.getDropLocation() + " for job " + job.getID());
		logger.debug(initialPose);
		logger.debug(routes);
		return routes;
	}
	
	public Job getCurrentJob(){
		return recentJob;
	}
	
	private List<Route> getCurrentRoutes(Robot r, Robot[] robots){
		//ArrayList<Route> routes = new ArrayList<Route>();
		logger.info("test2");
		Robot currentRobot = r;
		List<Route> routes = Collections.synchronizedList(new ArrayList<Route>());
		for (int i = 0; i < robots.length; i++) {
			
			if (robots[i].getActiveJob() != null 
					&& robots[i].getActiveJob().getCurrentroute() != null
					&& robots[i].getRobotName() != currentRobot.getRobotName()) {
				routes.add(robots[i].getActiveJob().getCurrentroute());
			}
		}
		//logger.info(routes);
		//return routes.toArray(new Route[routes.size()]);
		logger.info("test");
		logger.info("Returning " + routes.toArray(new Route[routes.size()]));
		//logger.info("Test " + 	routes.toArray(new Route[routes.size()]));
		return routes;
	}
	
	private Job createSubJob(Robot r, Job j, ConcurrentHashMap<Robot, ArrayList<Item>> itemsForRobots) {
		for (Robot robot : robots) {
			Job subJob = new Job(j.getID(), itemsForRobots.get(robot));
			subJob.setDropLocation(j.getDropLocation());
			logger.info(robot.getRobotName() + " items " + subJob.getITEMS() + " for job " + j.getID());
			if (!subJob.getITEMS().isEmpty()) {
				logger.debug(robot.getRobotName() + " has items");
				waitingMap.put(robot, Optional.of(subJob));
			}else {
				logger.debug(robot.getRobotName() + " has no items");
				waitingMap.put(robot, Optional.empty());
			}
		}
		if (Optional.ofNullable(waitingMap.get(r)).equals(Optional.empty())){
			logger.info("Value present " + waitingMap.get(r));
			return waitingMap.get(r).get();
		}else{
			logger.info("Value not present");
			return null;
		}
		
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
	
	private ConcurrentHashMap<Robot, ArrayList<Item>> splitItemsBetweenRobots(Job j) {
		logger.info("New job being split");
		ConcurrentHashMap<Robot, ArrayList<Item>> itemMap = new ConcurrentHashMap<>();
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
						int cost = calculateCost(item,itemMap,robot,robot.getCurrentPosition());
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
			Float currentWeight = weightMap.get(bidPickRobot);
			weightMap.put(bidPickRobot, currentWeight + bidPick.getWEIGHT());
			logger.info(bidPickRobot.getRobotName() + " assigned item " + bidPick.getID() + " in job " + j.getID() + " position " + bidPick.getPOSITION());;
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


	private int calculateCost(Item item, ConcurrentHashMap<Robot, ArrayList<Item>> itemMap, Robot _robot, Point robotPosition) {
		Robot robotCopy = new Robot("TestRobot", "", robotPosition);
		ArrayList<Item> itemArrayList = new ArrayList<>(itemMap.get(_robot));
		itemArrayList.add(item);
		int distance = tsp.calculateJobDistance(itemArrayList, robotCopy);
		return distance;
	}
	
		
}