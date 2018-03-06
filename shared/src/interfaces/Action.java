package interfaces;

// Commands

// Note - coded as strings instead of ints as the robot will receive
// an int value relating to the amount of a certain object it should pick up
public enum Action {
	LEFT("LEFT"), 				// Move forwards then turn left
	FORWARD("FORWARD"), 		// Move forwards
	RIGHT("RIGHT"), 			// Move forwards then turn right
	BACKWARD("BACKWARD"), 		// Reverse
	WAIT("WAIT"), 				// Do nothing until next instruction is received
	PICKUP("PICKUP"), 			// Wait for correct number of items to be loaded onto robot
	DROPOFF("DROPOFF"), 		// Wait for correct number of items to be unloaded from robot
	TURN_180("TURN_180"), 		// Turn through 180 whilst remaining at same coordinates
	TURN_LEFT("TURN_LEFT"), 	// Turn left whilst remaining at same coordinates
	TURN_RIGHT("TURN_RIGHT"), 	// Turn right whilst remaining at same coordinates
	CANCEL("CANCEL"), 			// Cancel current job and dump items on job
	SHUTDOWN("SHUTDOWN") 		// Shutdown the robot
	;

	// Variable which holds the string representation of the action
	private final String actionString;

	/*
	 * The constructor
	 */
	Action(String _actionString) {
		this.actionString = _actionString;
	}

	// Return the string representation of the action
	public String getActionString() {
		return this.actionString;
	}

	// Change a string into an action
	public static Action convertToAction(String inputString) {
		for (Action current : Action.values()) {
			if (current.getActionString().equals(inputString)) {
				return current;
			}
		}
		return null;
	}
}