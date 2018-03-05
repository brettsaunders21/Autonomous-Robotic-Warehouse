package interfaces;

import job.Job;
import lejos.geom.Point;

public class Robot {
	public static final int MAX_WEIGHT = 50;	//max weight any robot can carry
	private Point currentCoords;
	private int weight;
	private Job activeJob;
	private boolean routeSet;
	private String robotName;
	
	public Robot(String _robotName){
		this.robotName = _robotName;
		this.routeSet = false;
		this.setWeight(0);
	}


	/*Returns current coordinates of robot*/
	public Point getCurrentPosition(){
		return currentCoords;
	}

	/*Returns all information relating to active job*/
	public Job getActiveJob(){
		return activeJob;
	}

	/*Returns true if the robot has directions set*/
	public boolean isRouteSet(){
		return routeSet;
	}


	/*Sets the current position of the robot*/
	public void setCurrentPosition(Point position){
		//need to check valid coordinate before assigning, not in wall, actually on map etc
	}


	/*Sets all information relating to the newly assigned job. */
	public void setActiveJob(Job job){
		//sets dropOffCoords, pickUpCoords and determines if more than one trip is necessary. Requests routes to be made
	}


	public String getRobotName() {
		return robotName;
	}


	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}


	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

}
