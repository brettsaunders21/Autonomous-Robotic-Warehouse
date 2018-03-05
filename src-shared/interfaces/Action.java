public enum Action {
	LEFT,		//Move forwards then turn left
	FORWARD,	//Move forwards
	RIGHT,		//Move forwards then turn right
	BACKWARD,	//Reverse
	WAIT,		//Do nothing until next instruction is received
	PICKUP,		//Wait for correct number of items to be loaded onto robot
	DROPOFF,	//Wait for correct number of items to be unloaded from robot
	TURN_180,	//Turn through 180 whilst remaining at same coordinates
	TURN_LEFT,	//Turn left whilst remaining at same coordinates
	TURN_RIGHT,	//Turn right whilst remaining at same coordinates
	CANCEL,		//Cancel current job and dump items on job
	SHUTDOWN	//Shutdown the robot
}