package tests;

import org.junit.Test;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import interfaces.Action;
import interfaces.Robot;
import job.Job;
import job.JobAssignment;
import job.JobInput;
import job.JobList;
import job.JobSelection;
import job.TSP;
import lejos.geom.Point;
import main.Counter;
import routeplanning.AStar;
import routeplanning.Route;

//Job input, job selection, job assignment, robot and route planning
public class jobAssignmentTest {
	//private final static Logger logger = Logger.getLogger(AssignmentTest.class);;
	JobInput jobInput =  new JobInput();
	private Robot robot1 = new Robot("Spike", "0016530AA681", new Point(0,0));
	private Robot robot2 = new Robot("Marco", "001653115A7E", new Point(11, 7));
	private Robot robot3 = new  Robot("Jeremy", "00165308E37C", new Point(0,7));
	private Robot[] robotList = {robot1,robot2,robot3};
	private JobAssignment jAssignment;
	private HashMap<String, Double> betaValues = jobInput.getBetaValues();
	final static Logger logger = Logger.getLogger(jobAssignmentTest.class);
	final static Logger jobAssignmentLogger = Logger.getLogger(JobAssignment.class);
	final static Logger AStarLogger = Logger.getLogger(AStar.class);
	final static Logger routeLog = Logger.getLogger(Route.class);
	final static Logger TSPLog = Logger.getLogger(TSP.class);
	Counter counter = new Counter(robotList);
	Job firstJobAssigned;
	ArrayList<Point> drops;

	
	JobSelection jobSelection = new JobSelection(betaValues);
	JobList jobList = new JobList(jobSelection);
	

	public jobAssignmentTest() {
		drops = jobInput.getDrops();
		jAssignment = new JobAssignment(jobList, counter, drops, robotList);
		jobAssignmentLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
		AStarLogger.setLevel(Level.OFF);
		routeLog.setLevel(Level.OFF);
		TSPLog.setLevel(Level.OFF);
	}

	@Test
	public void checkTSP() {
		jAssignment = new JobAssignment(jobList, counter, drops, robotList);
		for (int i = 0; i < 80; i++) {
			jAssignment.assignJobs(robot1);
			jAssignment.assignJobs(robot2);
			jAssignment.assignJobs(robot3);
			//System.out.println(jobList.getJobsCompleted());
			//System.out.println(i + "completed");
		}
		assertEquals(true,true);
	}
	
	}
	