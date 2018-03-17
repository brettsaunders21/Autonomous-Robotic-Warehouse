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

public class PCController {
	private static final Logger controllerLogger = Logger.getLogger(PCController.class);
	private static final Logger aStarLogger = Logger.getLogger(AStar.class);
	private static final Logger routeExeLogger = Logger.getLogger(RouteExecution.class);
	private static final Logger jobAssLogger = Logger.getLogger(JobAssignment.class);
	private static final Robot[] ROBOTS = {
			new Robot("Spike", "0016530AA681", new Point(0, 0)),
			new Robot("Jeremy", "00165308E37C", new Point(5, 5)),
			new Robot("Marco", "00165315678E", new Point(10, 10))
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
		Counter counter = new Counter(ROBOTS);
		orderedJobs = jS.prioritize();
		JobAssignment jA = new JobAssignment(orderedJobs, counter, jI.getDrops());
		for (Robot rob : ROBOTS) {
			r[numOfRobots] = new RobotThread(rob, jA, counter);
			r[numOfRobots].setName(rob.getRobotName());
			r[numOfRobots].start();
			controllerLogger.debug("Started robot thread: " + rob.getRobotName());
		}
	}

}
