package job;

import routeplanning.Map;
import interfaces.Action;
import interfaces.Pose;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import interfaces.Robot;
import routeplanning.AStar;
import routeplanning.Route;

public class JobAssignment {
	private ConcurrentHashMap<String, ArrayList<Integer>> robotJobMap;
	private ArrayList<Job> jobs;
	private Robot[] robotsArray;
	private Map map = Map.generateRealWarehouseMap();

	public JobAssignment(Route routes, ArrayList<Job> j, Robot[] r) {
		robotsArray = r;
		jobs = j;
		generateTable(r);
	}

	public ConcurrentHashMap<String, ArrayList<Integer>> assignJobs() {
		run();
		return robotJobMap;
	}

	private void generateTable(Robot[] robotsArray) {
		for (Robot r : robotsArray) {
			robotJobMap.put(r.getRobotName(), new ArrayList<Integer>());
		}
	}

	public void run() {
		generateTable(robotsArray);
		while (checkAvailableRobot()) {
			for (Job job : jobs) {
				Robot robot = getAvailableRobot();
				ArrayList<Item> items = job.getITEMS();
				ArrayList<Route> routes = calculateRoute(robot, map, job, items);
				ArrayList<Action> actions = calculateActions(routes);
				Route routeForAllItems = new Route(routes, actions);
				job.assignCurrentroute(routeForAllItems);
				robot.setActiveJob(job);
				ArrayList<Integer> currentJobs = robotJobMap.get(robot.getRobotName());
				currentJobs.add(job.getID());
				robotJobMap.put(robot.getRobotName(), currentJobs);
				jobs.remove(job);
			}
		}

	}

	private ArrayList<Action> calculateActions(ArrayList<Route> routes) {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 0; i < routes.size(); i++) {
			actions.add(Action.PICKUP);
		}
		return actions;
	}

	private boolean checkAvailableRobot() {
		for (Robot robot : robotsArray) {
			if (robotJobMap.get(robot.getRobotName()).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private Robot getAvailableRobot() {
		Robot leastJobRobot = robotsArray[0];
		for (Robot robot : robotsArray) {
			if (robotJobMap.get(robot.getRobotName()).isEmpty()) {
				return robot;
			} else if (robotJobMap.get(robot.getRobotName()).size() < robotJobMap.get(leastJobRobot.getRobotName())
					.size()) {
				leastJobRobot = robot;
			}
		}
		return leastJobRobot;
	}

	private ArrayList<Route> calculateRoute(Robot r, Map map, Job job, ArrayList<Item> items) {
		AStar routeMaker = new AStar(map);
		ArrayList<Route> routes = new ArrayList<Route>();
		for (Item item : items) {
			routes.add(
					routeMaker.generateRoute(r.getCurrentPosition(), item.getPOSITION(), Pose.POS_X, new Route[] {}));
		}
		return routes;
	}
}
