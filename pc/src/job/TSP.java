package job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import interfaces.Pose;
import interfaces.Robot;
import lejos.geom.Point;
import routeplanning.AStar;
import routeplanning.Map;
import routeplanning.Route;

public class TSP {
	private AStar routeMaker = new AStar(Map.generateRealWarehouseMap());
	private ArrayList<Point> drops;
	final static Logger logger = Logger.getLogger(TSP.class);
	public ConcurrentHashMap<Robot, Point> positionMap;
			
	public TSP(ArrayList<Point> _drops) {
		drops = _drops;
		positionMap = new ConcurrentHashMap<>();
	}

	public ArrayList<Item> orderItems(ArrayList<Item> items, Robot robot) {
		Pose prePose = robot.getCurrentPose();
		ArrayList<Item> orderedItems = new ArrayList<>();
		Item closestItem = new Item(null, 0, 0, robot.getCurrentPosition(), 0);
		while (!items.isEmpty()) {
			closestItem = nearestItemToPoint(closestItem.getPOSITION(), items);
			items.remove(closestItem);
			orderedItems.add(closestItem);
		}
		ArrayList<Item> dropItems = addDropPoints(orderedItems);
		robot.setCurrentPose(prePose);
		return dropItems;
	}
	
	private ArrayList<Item> addDropPoints(ArrayList<Item> items) {
		int weightRunningTotal = 0;
		ArrayList<Item> withDrops = new ArrayList<Item>();
		for (Item item : items) {
			weightRunningTotal += item.getTOTAL_WEIGHT();
			if (weightRunningTotal >= 50) {
				System.out.println("Add dropoff");
				Item dropPoint = new Item("droppoint", 0, 0, nearestDropPoint(item.getPOSITION(), Pose.POS_X), 0);
				withDrops.add(dropPoint);
				weightRunningTotal = 0;
			}
			withDrops.add(item);
		}
		return withDrops;
	}
	
	public int calculateJobDistance(Job job, Robot robot) {
		ArrayList<Item> tempItems = new ArrayList<Item>(job.getITEMS());
		Robot tempR = robot;
		//tempItems = orderItems(tempItems, tempR);
		//tempItems = addDropPoints(tempItems);
		int distance = 0;
		if (tempItems.size() > 1) 
		for (int i = 1; i <= tempItems.size() - 1; i++) {
			distance += routeMaker.generateRoute(tempItems.get(i-1).getPOSITION(), tempItems.get(i).getPOSITION(), Pose.POS_X, new Route[] {}, 0).getLength();
		}
		distance += routeMaker.generateRoute(robot.getCurrentPosition(), tempItems.get(0).getPOSITION(), robot.getCurrentPose(), new Route[] {}, 0).getLength();
		return distance;
	}
	
	public int calculateJobDistance(ArrayList<Item> items, Robot robot) {
		ArrayList<Item> tempItems = new ArrayList<Item>(items);
		Robot tempR = robot;
		//tempItems = orderItems(tempItems, tempR);
		//tempItems = addDropPoints(tempItems);
		int distance = 0;
		if (tempItems.size() > 1) 
		for (int i = 1; i <= tempItems.size() - 1; i++) {
			distance += routeMaker.generateRoute(tempItems.get(i-1).getPOSITION(), tempItems.get(i).getPOSITION(), Pose.POS_X, new Route[] {}, 0).getLength();
			tempR.setCurrentPosition(tempItems.get(i).getPOSITION());
		}
		distance += routeMaker.generateRoute(robot.getCurrentPosition(), tempItems.get(0).getPOSITION(), robot.getCurrentPose(), new Route[] {}, 0).getLength();
		return distance;
	}
	
	
	private Item nearestItemToPoint(Point point,  ArrayList<Item> items) {
		Item nearestItemSoFar = items.get(0);
		int smallestDistance = Integer.MAX_VALUE;
		for (Item item : items) {
			int distance = 0;
			if (!point.equals(item.getPOSITION())) {
				distance = routeMaker.generateRoute(point, item.getPOSITION(), Pose.POS_X,  new Route[] {}, 0).getLength();
			}
			if (distance < smallestDistance) {
				nearestItemSoFar = item;
				smallestDistance = distance;
			}
		}
		return nearestItemSoFar;
	}
	
	public Point nearestDropPoint(Point currentLocation, Pose pose) {
		Point closestPoint = drops.get(0);
		int closestPointDistance = Integer.MAX_VALUE;
		for (Point point : drops) {
			int routeToDrop1 = routeMaker.generateRoute(currentLocation, point, pose, new Route[] {}, 0).getLength();
			if (routeToDrop1 < closestPointDistance) {
				closestPointDistance = routeToDrop1;
				closestPoint = point;
			}
		}
		return closestPoint;
	}

	public Point bestDropPoint(ConcurrentHashMap<Robot, ArrayList<Item>> itemsForRobots, Job j) {
		// TODO Auto-generated method stub
		HashMap<Point, Integer> dropMap = new HashMap<>();
		Entry<Point, Integer> bestDropPoint = null;
		for (Entry<Robot, ArrayList<Item>> entry : itemsForRobots.entrySet()) {
			Robot thisRobot = entry.getKey();
			if (entry.getValue().isEmpty()) {
				continue;
			}
			Item lastItem = entry.getValue().get(entry.getValue().size()-1);
			Point bestDrop = nearestDropPoint(lastItem.getPOSITION(), thisRobot.getCurrentPose());
			if (dropMap.containsKey(bestDrop)) {
				dropMap.put(bestDrop, dropMap.get(bestDrop)+1);
			}else{
				dropMap.put(bestDrop, 1);
			}
		}
		for (Entry<Point, Integer> entry : dropMap.entrySet()) {
			if (bestDropPoint == null || entry.getValue() > bestDropPoint.getValue()) {
				bestDropPoint = entry;
			}
		}
		//j.setDropLocation(bestDropPoint.getKey());
		return bestDropPoint.getKey();
	}
	
	public synchronized List<Route> getCurrentRoutes(Robot currentRobot, Robot[] robots){
		//ArrayList<Route> routes = new ArrayList<Route>();
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
		return routes;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	ConcurrentHashMap<Robot, ArrayList<Item>> splitItemsBetweenRobots(Job j, Robot[] robots) {
		logger.debug("New job being split");
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
					if (robotWeight + item.getTOTAL_WEIGHT() < 50.0f) {
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
			itemListForRobot = addDropPoints(itemListForRobot);
			itemListForRobot.add(bidPick);
			itemMap.put(bidPickRobot, itemListForRobot);
			items.remove(bidPick);
			Float currentWeight = weightMap.get(bidPickRobot);
			weightMap.put(bidPickRobot, currentWeight + bidPick.getTOTAL_WEIGHT());
			logger.debug(bidPickRobot.getRobotName() + " assigned item " + bidPick.getID() + " in job " + j.getID() + " position " + bidPick.getPOSITION());;
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
		int distance = calculateJobDistance(itemArrayList, robotCopy);
		return distance;
	}
}
