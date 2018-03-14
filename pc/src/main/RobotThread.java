package main;

import interfaces.Robot;
import job.JobAssignment;

public class RobotThread extends Thread{
	private Robot robot;
	private final JobAssignment TASKER;
	private Robot[] otherRobots;
	
	public RobotThread(Robot _robot, Robot[] _otherRobots, JobAssignment _tasker) {
		this.robot = _robot;
		this.otherRobots = _otherRobots;
		this.TASKER = _tasker;
	}
	
	public void run() {
		while(true) {
			if (true) { //Get from tasker if current job equals cancelled
				//Cancel job
			}
			if (robot.getJobCancelled() || robot.isJobFinished()) {
				//assign another job
				//calculate route
			}
			sendJob();
		}
	}
	
	public void sendJob() {
		
	}
}
