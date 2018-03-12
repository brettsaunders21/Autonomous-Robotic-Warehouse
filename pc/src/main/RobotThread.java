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
				robot.jobNotFinished();
				TASKER.assignJobs(robot);
			}
			RouteExecution rE = new RouteExecution(robot, networker);
			rE.setName(robot.getRobotName() + " : " + robot.getActiveJob());
			rE.run();
		}
	}
	
}
