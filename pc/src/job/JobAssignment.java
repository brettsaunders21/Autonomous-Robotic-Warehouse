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
	private Job recentJob;
	
	public JobAssignment(ArrayList<Job> j, Counter _counter, ArrayList<Point> _drops) {
		jobs = j;
		counter = _counter;
		drops = _drops;
		tsp = new TSP(drops);
	}


	public void assignJobs(Robot robot) {
		Job job;
		if (!jobs.isEmpty()) {
			job = jobs.get(0);
		}else{
			return;
		}
		ArrayList<Item> items = job.getITEMS();
		ArrayList<Item> orderedItems = tsp.orderItems(items,robot);
		ArrayList<Route> routes = calculateRoute(robot, map, job, orderedItems);
		ArrayList<Action> actions = calculateActions(routes);
		Route routeForAllItems = new Route(routes, actions);
		Route routeWithDropoff = new Route(routeForAllItems, Action.DROPOFF);
		for(Action a :routeWithDropoff.getDirectionArray())
			System.out.println(a);
		job.assignCurrentroute(routeWithDropoff);
		robot.setActiveJob(job);
		recentJob = job;
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
		System.out.println();
		System.out.println();
		int timeCount = counter.getTime();
		Point currentRobotPosition = r.getCurrentPosition();
		ArrayList<Route> routes = new ArrayList<Route>();
		Route itemRoute;
		int item1 = 0;
		for (Item item : items) {
			if(item.getID().equals("droppoint")){
				System.out.println("Triggered");
				System.out.println(item1);
				System.out.println("numitem = " + items.size());
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
			System.out.println();
			r.setCurrentPose(itemRoute.getFinalPose()); 
			logger.trace(item);
			logger.trace(itemRoute);
			timeCount = counter.getTime();
			item1++;
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
