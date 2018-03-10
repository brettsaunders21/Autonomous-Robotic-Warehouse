package tests.communication;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import communication.CommunicationData;
import communication.PCNetworkHandler;
import interfaces.Action;

public class CommunicationTests {
	
	NXTInfo nxtInfo;
	PCNetworkHandler networkHandler;

	// Set up the bluetooth connection before any tests
	@BeforeClass
	public void setup() {
		// Drawer A3 - Spike - 0016530AA681
		nxtInfo.deviceAddress = "0016530AA681";
		nxtInfo.protocol = NXTCommFactory.BLUETOOTH;
		nxtInfo.name = "Spike";
		
		networkHandler = new PCNetworkHandler(nxtInfo);
	}
	
	// Test if a connection can be established
	@Test(timeout=1000)
	public void singleRobotConnectionTest() throws InterruptedException{
		networkHandler.run();
		Thread.sleep(5000);
		assertTrue(networkHandler.isConnected());
	}
	
	// Test if an Action object can be sent and received
	@Test(timeout=1000)
	public void singleRobotSendReceiveActionTest() throws IOException, InterruptedException {
		Action testObject = Action.WAIT;
		networkHandler.sendObject(testObject);
		
		Thread.sleep(1000);
		
		Action receivedObject = (Action) networkHandler.receiveObject(CommunicationData.ACTION);
		
		assertEquals(testObject, receivedObject);
	}
	
	// Test if an integer can be sent and received
	@Test(timeout=1000)
	public void singleRobotSendReceiveIntegerTest() throws IOException, InterruptedException {
		int testObject = 43565;
		networkHandler.sendObject(testObject);
		
		Thread.sleep(1000);
		
		int receivedObject = (int) networkHandler.receiveObject(CommunicationData.INT);
		
		assertEquals(testObject, receivedObject);
	}
	
	// Test if a string can be sent and received
	@Test(timeout=1000)
	public void singleRobotSendReceiveStringTest() throws IOException, InterruptedException {
		String testObject = "Test string";
		networkHandler.sendObject(testObject);
		
		Thread.sleep(1000);
		
		String receivedObject = (String) networkHandler.receiveObject(CommunicationData.STRING);
		
		assertEquals(testObject, receivedObject);
	}
	
	// Include more tests + multiple robot tests?
}