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
import job.JobSelection;
import lejos.geom.Point;
import main.Counter;
import routeplanning.AStar;
import routeplanning.Route;

//Job input, job selection, job assignment, robot and route planning
public class jobAssignmentTest {
	//private final static Logger logger = Logger.getLogger(AssignmentTest.class);;
	JobInput jobInput =  new JobInput();
	private Robot robot1 = new Robot("Spike", "0016530AA681", new Point(0,0));
	private Robot[] robotList = {robot1};
	private JobAssignment jAssignment;
	private HashMap<String, Double> betaValues = jobInput.getBetaValues();
	final static Logger logger = Logger.getLogger(jobAssignmentTest.class);
	final static Logger jobAssignmentLogger = Logger.getLogger(JobAssignment.class);
	final static Logger AStarLogger = Logger.getLogger(AStar.class);
	final static Logger routeLog = Logger.getLogger(Route.class);
	Counter counter = new Counter(robotList);
	Job firstJobAssigned;
	ArrayList<Point> drops;

	
	JobSelection jobSelection = new JobSelection(betaValues);
	
	

	
	public jobAssignmentTest() {
		drops = jobInput.getDrops();
		jAssignment = new JobAssignment(createJobList(), counter, drops, jobSelection);
		jobAssignmentLogger.setLevel(Level.OFF);
		//logger.setLevel(Level.DEBUG);
		logger.setLevel(Level.OFF);
		AStarLogger.setLevel(Level.OFF);
		routeLog.setLevel(Level.OFF);
	}

	
	private ArrayList<Job> createJobList(){
		ArrayList<Job> jobList = jobSelection.prioritize();
		return jobList;
		
	}

	@Test
	public void checkJobAssigned() {
		jAssignment = new JobAssignment(createJobList(), counter, drops, jobSelection);
		jAssignment.assignJobs(robot1);
		firstJobAssigned = jAssignment.getCurrentJob();
		assertEquals(firstJobAssigned.getID(), robot1.getActiveJob().getID());
	}
	
	@Test
	public void checkTSP() {
		for (int i = 0; i < 1000; i++) {
			jAssignment.assignJobs(robot1);
			//System.out.println(i + "completed");
		}
		jAssignment.assignJobs(robot1);
		jAssignment.assignJobs(robot1);
		assertEquals(true,true);
	}
	
	@Test
	public void dropoffAtEndOfJob() {
		//robot1.setCurrentPosition(new Point(0,0));
		jAssignment.assignJobs(robot1);
		Job currentJob = robot1.getActiveJob();
		Route route = currentJob.getCurrentroute();
		Action[] routeArray = route.getDirectionArray();
		assertEquals(Action.DROPOFF, routeArray[routeArray.length-1]);	
	}
	}
	