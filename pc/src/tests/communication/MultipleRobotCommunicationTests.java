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
 * MultipleRobotCommunicationTests class
 * 
 * JUnit tests for bluetooth communication on 3 robots. It is required to run
 * CommunicationTestHelper on each of the 3 robots for these to work.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultipleRobotCommunicationTests {

	static NXTInfo nxtInfo1;
	static PCNetworkHandler networkHandler1;
	static NXTInfo nxtInfo2;
	static PCNetworkHandler networkHandler2;
	static NXTInfo nxtInfo3;
	static PCNetworkHandler networkHandler3;

	// Set up the bluetooth connection before any tests
	@BeforeClass
	public static void setup() throws InterruptedException {
		nxtInfo1 = new NXTInfo(NXTCommFactory.BLUETOOTH, "Spike", "0016530AA681");
		nxtInfo2 = new NXTInfo(NXTCommFactory.BLUETOOTH, "Marco", "00165317976F");
		nxtInfo3 = new NXTInfo(NXTCommFactory.BLUETOOTH, "Jeremy", "00165308E37C");

		networkHandler1 = new PCNetworkHandler(nxtInfo1);
		Thread.sleep(3000);
		networkHandler2 = new PCNetworkHandler(nxtInfo2);
		Thread.sleep(3000);
		networkHandler3 = new PCNetworkHandler(nxtInfo3);
		Thread.sleep(3000);
	}

	// Test if a connection can be established,
	@Test(timeout = 15000)
	public void multiRobotConnectionTest() {
		networkHandler1.run();
		networkHandler2.run();
		networkHandler3.run();
		assertTrue(networkHandler1.isConnected() && networkHandler2.isConnected() && networkHandler3.isConnected());
	}

	// Test if an Action object can be sent and received
	@Test(timeout = 3000)
	public void multiRobotSendReceiveActionTest() throws IOException {
		Action testObject = Action.WAIT;
		networkHandler1.sendObject(testObject);
		networkHandler2.sendObject(testObject);
		networkHandler3.sendObject(testObject);
		
		Action receivedObject1 = networkHandler1.receiveAction();
		Action receivedObject2 = networkHandler2.receiveAction();
		Action receivedObject3 = networkHandler3.receiveAction();
		
		assertTrue(testObject.equals(receivedObject1) && testObject.equals(receivedObject2)
				&& testObject.equals(receivedObject3));
	}

	// Test if a double can be sent and received
	@Test(timeout = 3000)
	public void multiRobotSendReceiveDoubleTest() throws IOException {
		double testObject = 0.973f;
		networkHandler1.sendObject(testObject);
		networkHandler2.sendObject(testObject);
		networkHandler3.sendObject(testObject);

		double receivedObject1 = networkHandler1.receiveDouble();
		double receivedObject2 = networkHandler2.receiveDouble();
		double receivedObject3 = networkHandler3.receiveDouble();

		assertTrue(testObject == receivedObject1 && testObject == receivedObject2
				&& testObject == receivedObject3);
	}

	// Test if a float can be sent and received
	@Test(timeout = 3000)
	public void multiRobotSendReceiveFloatTest() throws IOException {
		float testObject = 0.564f;
		networkHandler1.sendObject(testObject);
		networkHandler2.sendObject(testObject);
		networkHandler3.sendObject(testObject);

		float receivedObject1 = networkHandler1.receiveFloat();
		float receivedObject2 = networkHandler2.receiveFloat();
		float receivedObject3 = networkHandler3.receiveFloat();

		assertTrue(testObject == receivedObject1 && testObject == receivedObject2
				&& testObject == receivedObject3);
	}

	// Test if an integer can be sent and received
	@Test(timeout = 3000)
	public void multiRobotSendReceiveIntegerTest() throws IOException {
		int testObject = 43565;
		networkHandler1.sendObject(testObject);
		networkHandler2.sendObject(testObject);
		networkHandler3.sendObject(testObject);

		int receivedObject1 = networkHandler1.receiveInt();
		int receivedObject2 = networkHandler2.receiveInt();
		int receivedObject3 = networkHandler3.receiveInt();

		assertTrue(testObject == receivedObject1 && testObject == receivedObject2
				&& testObject == receivedObject3);
	}

	// Test if a string can be sent and received
	@Test(timeout = 3000)
	public void multiRobotSendReceiveStringTest() throws IOException {
		String testObject = "Test string";
		networkHandler1.sendObject(testObject);
		networkHandler2.sendObject(testObject);
		networkHandler3.sendObject(testObject);

		String receivedObject1 = networkHandler1.receiveString();
		String receivedObject2 = networkHandler2.receiveString();
		String receivedObject3 = networkHandler3.receiveString();

		assertTrue(testObject.equals(receivedObject1) && testObject.equals(receivedObject2)
				&& testObject.equals(receivedObject3));
	}
}