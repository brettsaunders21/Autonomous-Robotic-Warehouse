package tests.communication;

import java.io.IOException;

import communication.CommunicationData;
import communication.RobotNetworkHandler;
import interfaces.Action;

/*
 * CommTestHelper class
 * 
 * To be run on the robot when the bluetooth communication tests are run on the PC
 */

public class CommTestHelper {

	public static void main(String[] args) throws IOException {
		RobotNetworkHandler networkHandler = new RobotNetworkHandler();

		// singleRobotConnectionTest
		networkHandler.run();

		// singleRobotSendReceiveActionTest
		Action receivedObject = (Action) networkHandler.receiveObject(CommunicationData.ACTION);
		System.out.println("Received Action");
		networkHandler.sendObject(receivedObject);
		System.out.println("Sent Action");

		// singleRobotSendReceiveDoubleTest
		double receivedObject4 = (double) networkHandler.receiveObject(CommunicationData.DOUBLE);
		System.out.println("Received double");
		networkHandler.sendObject(receivedObject4);
		System.out.println("Sent double");

		// singleRobotSendReceiveFloatTest
		float receivedObject3 = (float) networkHandler.receiveObject(CommunicationData.FLOAT);
		System.out.println("Received float");
		networkHandler.sendObject(receivedObject3);
		System.out.println("Sent float");

		// singleRobotSendReceiveIntegerTest
		int receivedObject1 = (int) networkHandler.receiveObject(CommunicationData.INT);
		System.out.println("Received int");
		networkHandler.sendObject(receivedObject1);
		System.out.println("Sent int");

		// singleRobotSendReceiveStringTest
		String receivedObject2 = (String) networkHandler.receiveObject(CommunicationData.STRING);
		System.out.println("Received String");
		networkHandler.sendObject(receivedObject2);
		System.out.println("Sent String");

	}
}
