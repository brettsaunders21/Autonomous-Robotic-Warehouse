package interfaces;

public class Direction {
	/**Pose of the robot*/
	public static enum Pose{
		POS_X, POS_Y, NEG_X, NEG_Y
	}
	/**Direction instructions for robot*/
	public static enum Directions{
		LEFT, RIGHT, FORWARD, WAIT
	}
	/**Rotation instructions for robot, for rotations on the spot (coordinate remains unchanged)*/
	public static enum Rotation{
		TURN_180, TURN_LEFT, TURN_RIGHT, NONE
	}
}
