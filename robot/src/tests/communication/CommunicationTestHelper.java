package tests.communication;

import java.io.IOException;

import communication.CommunicationData;
import communication.RobotNetworkHandler;
import interfaces.Action;

/*
 * CommunicationTestHelper class
 * 
 * To be run on the robot when the bluetooth communication tests are run on the PC
 */

public class CommunicationTestHelper {
	
	
	public static void main(String[] args) throws IOException {
		RobotNetworkHandler networkHandler = new RobotNetworkHandler();
		
		// singleRobotConnectionTest
		networkHandler.run();
		
		// singleRobotSendReceiveActionTest
		Action receivedObject = (Action) networkHandler.receiveObject(CommunicationData.ACTION);
		networkHandler.sendObject(receivedObject);
		
		// singleRobotSendReceiveIntegerTest
		int receivedObject1 = (int) networkHandler.receiveObject(CommunicationData.INT);
		networkHandler.sendObject(receivedObject1);
		
		// singleRobotSendReceiveStringTest
		String receivedObject2 = (String) networkHandler.receiveObject(CommunicationData.STRING);
		networkHandler.sendObject(receivedObject2);
		
		// singleRobotSendReceiveFloatTest
		float receivedObject3 = (float) networkHandler.receiveObject(CommunicationData.FLOAT);
		networkHandler.sendObject(receivedObject3);
		
		// singleRobotSendReceiveDoubleTest
		double receivedObject4 = (double) networkHandler.receiveObject(CommunicationData.DOUBLE);
		networkHandler.sendObject(receivedObject4);
	}
}
