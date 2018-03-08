package main;

import communication.PCNetworkHandler;
import interfaces.Robot;
import job.JobAssignment;

public class RobotThread extends Thread{
	private Robot robot;
	private final JobAssignment TASKER;
	private final PCNetworkHandler networker;
	
	public RobotThread(Robot _robot, JobAssignment _tasker) {
		this.robot = _robot;
		this.TASKER = _tasker;
		networker = new PCNetworkHandler(robot.getNXTInfo());
	}
	
	public void run() {
		while(true) {
			//Get from tasker if current job equals cancelled
				//Cancel job
			if (robot.getJobCancelled() || robot.isJobFinished()) {
				TASKER.assignJobs(robot);
			}
			RouteExecution rE = new RouteExecution(robot, networker);
			rE.setName(robot.getRobotName() + " : " + robot.getActiveJob());
		}
	}
	
}
