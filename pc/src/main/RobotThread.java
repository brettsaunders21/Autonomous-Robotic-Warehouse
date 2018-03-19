package main;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import communication.PCNetworkHandler;
import interfaces.Robot;
import job.Job;
import job.JobAssignment;

public class RobotThread extends Thread{
	private static final Logger rTLogger = Logger.getLogger(RobotThread.class);
	private Robot robot;
	private final JobAssignment TASKER;
	private final PCNetworkHandler networker;
	private Counter counter;
	private PointsHeld heldPoints;
	private ArrayList<Job> completedJobs;
	
	public RobotThread(Robot _robot, JobAssignment _tasker, Counter _counter, PointsHeld _heldPoints, ArrayList<Job> _completedJobs) {
		this.robot = _robot;
		this.TASKER = _tasker;
		networker = new PCNetworkHandler(robot.getNXTInfo());
		counter = _counter;
		heldPoints = _heldPoints;
		completedJobs = _completedJobs;
	}
	
	public void run() {
		// Run the PCNetworkHandler to try and establish a connection with the robot
		networker.run();
		
		// Keep checking whether the pc has connected to the robot 
		while (!networker.isConnected()) {
			try {
			sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (networker.getConnectionFailed()) {
			// Print to logger that connection failed and return
			return;
		}
		
		while(true) {
			if (robot.getJobCancelled() || robot.isJobFinished()) {
				if (robot.isJobFinished()) {
					if (robot.getActiveJob() != null) {
						completedJobs.add(robot.getActiveJob());
					}
				}
				robot.jobNotFinished();
				TASKER.assignJobs(robot);
				rTLogger.debug("Assigned " + robot.getRobotName() + " job: " + robot.getActiveJob());
			}
			RouteExecution rE = new RouteExecution(robot, networker, counter, heldPoints);
			rE.run();
			rTLogger.debug("Executing robot job");
		}
	}
	
}
