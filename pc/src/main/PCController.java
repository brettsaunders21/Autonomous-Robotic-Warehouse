package main;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import interfaces.Robot;
import job.Job;
import job.JobAssignment;
import job.JobInput;
import job.JobSelection;
import lejos.geom.Point;
import routeplanning.AStar;
import warehouse_interface.JobsInterface;
import warehouse_interface.WarehouseInterface;

public class PCController {
	private static final Logger controllerLogger = Logger.getLogger(PCController.class);
	private static final Logger aStarLogger = Logger.getLogger(AStar.class);
	private static final Logger routeExeLogger = Logger.getLogger(RouteExecution.class);
	private static final Logger jobAssLogger = Logger.getLogger(JobAssignment.class);
	private static final Robot[] ROBOTS = {
			new Robot("Spike", "0016530AA681", new Point(0, 0))			
	};
	private static ArrayList<Job> orderedJobs;
	private static int numOfRobots = 0;
	private static RobotThread[] r = new RobotThread[numOfRobots+1];

	
	public static void main(String[] args) {
		controllerLogger.setLevel(Level.ALL);
		aStarLogger.setLevel(Level.ALL);
		routeExeLogger.setLevel(Level.ALL);
		jobAssLogger.setLevel(Level.ALL);
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		Counter counter = new Counter();
		counter.run();
		orderedJobs = jS.prioritize();
		JobAssignment jA = new JobAssignment(orderedJobs, ROBOTS, counter);
		for (Robot rob : ROBOTS) {
			r[numOfRobots] = new RobotThread(rob, jA);
			r[numOfRobots].setName(rob.getRobotName());
			r[numOfRobots].start();
			controllerLogger.debug("Started robot thread: " + rob.getRobotName());
		}
	}

}
