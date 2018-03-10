package tests.communication;

import communication.RobotNetworkHandler;

public class CommunicationTestHelper {
	private RobotNetworkHandler networkHandler;
	
	public static void main(String[] args) {
		networkHandler = new RobotNetworkHandler();
		// singleRobotConnectionTest
		networkHandler.run();
		
	}
}
