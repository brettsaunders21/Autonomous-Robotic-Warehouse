package interfaces;

/*
 * Action class
 * 
 * Holds all possible actions for the robot
 */

public enum Action {
	LEFT,		//Move forwards then turn left
	FORWARD,	//Move forwards
	RIGHT,		//Move forwards then turn right
	BACKWARD,	//Reverse
	TURN_180,	//Turn 180 degrees
	TURN_LEFT,	//Turn left
	TURN_RIGHT, //Turn right
	WAIT,		//Do nothing until next instruction is received
	PICKUP,		//Wait for correct number of items to be loaded onto robot
	DROPOFF,	//Wait for correct number of items to be unloaded from robot
	CANCEL,		//Cancel current job and dump items on job
	SHUTDOWN	//Shutdown the robot
}
