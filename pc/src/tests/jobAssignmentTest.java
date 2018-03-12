package tests;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import interfaces.Robot;
import job.Item;
import job.Job;
import job.JobAssignment;
import lejos.geom.Point;

public class jobAssignmentTest {
	//private final static Logger logger = Logger.getLogger(AssignmentTest.class);
	private Robot robot1 = new Robot("terry1", "0", new Point(0,0));
	private Robot[] robotList = {robot1};
	private JobAssignment jAssignment;
	final static Logger logger = Logger.getLogger(jobAssignmentTest.class);
	final static Logger jobAssignmentLogger = Logger.getLogger(JobAssignment.class);

	
	public jobAssignmentTest() {
		jAssignment = new JobAssignment(createJobList(), robotList, 0);
		jobAssignmentLogger.setLevel(Level.OFF);
		logger.setLevel(Level.DEBUG);
	}
	
	private Job createJob(int jobId) {
		ArrayList<Item> itemList = new ArrayList<Item>();
		Item item1 = new Item("1", 1, 1.0f, new Point(2,5), 2.0f);
		Item item2 = new Item("2", 1, 1.0f, new Point(2,7), 2.0f);
		itemList.add(item1);
		itemList.add(item2);
		Job job1 = new Job(jobId, itemList);
		return job1;
	}
	
	private ArrayList<Job> createJobList(){
		ArrayList<Job> jobList = new ArrayList<Job>();
		Job job1 = createJob(1);
		Job job2 = createJob(2);
		Job job3 = createJob(3);
		Job job4 = createJob(4);
		jobList.add(job1);
		jobList.add(job2);
		jobList.add(job3);
		jobList.add(job4);
		return jobList;
		
	}

	@Test
	public void checkJobAssigned() {
		robot1.setCurrentPosition(new Point(0,0));
		System.out.println("test1");
		System.out.println("before assign");
		jAssignment.assignJobs(robot1);
		assertEquals(1, robot1.getActiveJob().getID());
	}
	}
	
