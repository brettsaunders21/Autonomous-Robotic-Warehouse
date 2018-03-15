package tests.communication;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import communication.PCNetworkHandler;
import interfaces.Action;

/*
 * CommunicationTests class
 * 
 * JUnit tests for bluetooth communication. It is required to run
 * CommunicationTestHelper on the robot for there to work.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommunicationTests {

	static NXTInfo nxtInfo;
	static PCNetworkHandler networkHandler;

	// Set up the bluetooth connection before any tests
	@BeforeClass
	public static void setup() {
		// Drawer A3 - Spike - 0016530AA681
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "Spike", "0016530AA681");

		networkHandler = new PCNetworkHandler(nxtInfo);
	}

	// Test if a connection can be established
	@Test(timeout = 11000)
	public void singleRobotConnectionTest() {
		networkHandler.run();
		assertTrue(networkHandler.isConnected());
	}

	// Test if an Action object can be sent and received
	@Test(timeout = 3000)
	public void singleRobotSendReceiveActionTest() throws IOException {
		Action testObject = Action.WAIT;
		networkHandler.sendObject(testObject);;

		Action receivedObject = networkHandler.receiveAction();

		assertEquals(testObject, receivedObject);
	}

	// Test if a double can be sent and received
	@Test(timeout = 3000)
	public void singleRobotSendReceiveDoubleTest() throws IOException {
		double testObject = 0.97338474f;
		networkHandler.sendObject(testObject);

		double receivedObject = networkHandler.receiveDouble();

		assertEquals(testObject, receivedObject, 0.5f);
	}

	// Test if a float can be sent and received
	@Test(timeout = 3000)
	public void singleRobotSendReceiveFloatTest() throws IOException {
		float testObject = 0.56443f;
		networkHandler.sendObject(testObject);

		float receivedObject = networkHandler.receiveFloat();

		assertEquals(testObject, receivedObject, 0.5f);
	}

	// Test if an integer can be sent and received
	@Test(timeout = 3000)
	public void singleRobotSendReceiveIntegerTest() throws IOException {
		int testObject = 43565;
		networkHandler.sendObject(testObject);

		int receivedObject = networkHandler.receiveInt();

		assertEquals(testObject, receivedObject);
	}

	// Test if a string can be sent and received
	@Test(timeout = 3000)
	public void singleRobotSendReceiveStringTest() throws IOException {
		String testObject = "Test string";
		networkHandler.sendObject(testObject);

		String receivedObject = networkHandler.receiveString();

		assertEquals(testObject, receivedObject);
	}

	// Include more tests + multiple robot tests?
}