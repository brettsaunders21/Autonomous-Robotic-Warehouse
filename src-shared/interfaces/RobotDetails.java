package interfaces;

import java.util.Queue;
import lejos.geom.Point;
import jobs.JobDetails;

public class RobotDetails {
	public static final int MAX_WEIGHT = 50;	//max weight any robot can carry

	private final int idNum;	//unique identifier of this robot

	private Point currentCoords;
	private int weight;


	private Point dropOffCoords;
	private Point pickUpCoords;	//Should we make this an ArrayList of Coordinate so that more than one pickup can be specified?

	private JobDetails activeJob;

	private boolean routeSet;
	private Queue directions;
	private Queue coordsOnRoute;
	
	public RobotDetails(int id){
		this.idNum = id;
		this.routeSet = false;
		this.weight = 0;
	}


	/*Returns current coordinates of robot*/
	public Point getCurrentPosition(){
		return currentCoords;
	}

	/*Returns coordinate of robot's drop off location (*/
	public Point getDropOffCoords(){
		return dropOffCoords;
	}

	/*Returns coordinate of robot's pickup location*/
	public Point getPickUpCoords(){
		return pickUpCoords;
	}

	/*Returns all information relating to active job*/
	public JobDetails getActiveJob(){
		return activeJob;
	}

	/*Returns true if the robot has directions set*/
	public boolean isRouteSet(){
		return routeSet;
	}

	/*Returns a queue (NEED TO DECIDE WHICH SORT OF QUEUE) of directions. E.g. Left, Right, Wait, Right etc.*/
	public Queue getDirections(){
		return directions;
	}

	/*Returns a queue (NEED TO DECIDE WHICH SORT OF QUEUE) of coordinates that should be passed through when directions are executed successfully*/
	public Queue getCoordsOnRoute(){
		return coordsOnRoute;
	}

	/*Sets the current position of the robot*/
	public void setCurrentPosition(Point position){
		//need to check valid coordinate before assigning, not in wall, actually on map etc
	}


	/*Sets all information relating to the newly assigned job. */
	public void setActiveJob(JobDetails job){
		//sets dropOffCoords, pickUpCoords and determines if more than one trip is necessary. Requests routes to be made
	}	
}
