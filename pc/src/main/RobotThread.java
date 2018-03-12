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
		networker.run();
		while (!networker.isConnected()) {
			try {
			sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(true) {
			if (robot.getJobCancelled() || robot.isJobFinished()) {
				robot.jobNotFinished();
				TASKER.assignJobs(robot);
			}
			RouteExecution rE = new RouteExecution(robot, networker);
			rE.setName(robot.getRobotName() + " : " + robot.getActiveJob());
			rE.run();
		}
	}
	
}
