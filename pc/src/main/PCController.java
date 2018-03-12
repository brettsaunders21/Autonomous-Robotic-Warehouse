package main;

import java.util.ArrayList;

import interfaces.Robot;
import job.Job;
import job.JobAssignment;
import job.JobInput;
import job.JobSelection;
import lejos.geom.Point;

public class PCController {
	private static final Robot[] ROBOTS = {
			new Robot("Spike", "0016530AA681", new Point(0, 0))
	};
	private static ArrayList<Job> orderedJobs;
	private static RobotThread[] r;
	private static int numOfRobots = 0;
	
	public static void main(String[] args) {
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		orderedJobs = jS.prioritize();
		JobAssignment jA = new JobAssignment(orderedJobs, ROBOTS, 0);
		for (Robot rob : ROBOTS) {
			r[numOfRobots] = new RobotThread(rob, jA);
			r[numOfRobots].setName(rob.getRobotName());
			r[numOfRobots].start();
		}
	}

}
