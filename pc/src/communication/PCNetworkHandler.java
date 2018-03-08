package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import org.apache.log4j.Logger;

import interfaces.Action;

/*
 * PCNetworkHandler class
 * 
 * Handles the bluetooth connection for the PC
 * 
 * Notes about pairing from lejos bluetooth example:
 * You must pair every NXT with the PC for the Bluetooth connection to work (hint: on Ubuntu I connected to each NXT
 * using a known PIN, although for some reason when setting up each connection I had to select PIN Options twice
 * before it remembered my choice). Once paired you can use nxjbrowse -b to get the name and address of the NXTs.
 * This information should be put into AdderConnection.java in place of the details I've put there. Next, compile and
 * upload AdderClient.java to each NXT then run it. Finally run AdderConnection.java and it should talk to all NXTs
 * in parallel and double-check what they're sending back. Note that it appeears that the NXTComm Bluetooth class (or
 * some other related part of the comms infrastructure) is not thread safe, so concurrent calls to it should be
 * synchronised.
 */

public class PCNetworkHandler extends AbstractSenderReceiver {

	// The logger
	final static Logger logger = Logger.getLogger(PCNetworkHandler.class);

	// NXTInfo object which holds the robot's bluetooth address + other info
	private NXTInfo nxtInfo;

	/**
	 * The constructor
	 *
	 * @param _nxtInfo
	 *            The NXTInfo object of the robot being connected to
	 */
	public PCNetworkHandler(NXTInfo _nxtInfo) {
		nxtInfo = _nxtInfo;
	}

	@Override
	public void run() {
		// Amount of connection retries
		final int MAX_CONNECTION_RETRIES = 3;

		// Time delay between attempting a reconnect
		final int RETRY_DELAY = 500;

		logger.info("Attempting to connect to: " + nxtInfo.name);

		try {
			// Create an NXTComm object ready to connect using the bluetooth protocol
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

			// Flag for whether the intial connection has been established or not
			boolean initialConnection = false;
			
			for (int i = 0; i < MAX_CONNECTION_RETRIES; i++) {
				
				// Check if the robot is ready to connect
				initialConnection = nxtComm.open(nxtInfo);
				
				/* If the NXT is ready for connection, connect to it and create data streams,
				 * otherwise try again after a delay
				 */
				if (initialConnection) {
					inputStream = new DataInputStream(nxtComm.getInputStream());
					outputStream = new DataOutputStream(nxtComm.getOutputStream());
					
					logger.info("Successfully connected to: " + nxtInfo.name);
					return;
				}
				else {
					logger.warn("Attempt + " + i + " failed. Retrying in " + RETRY_DELAY + " seconds");
					try {
						Thread.sleep(RETRY_DELAY);
					} catch (InterruptedException e) {
						logger.error("PCNetworkHandler thread interrupted.");
						return;
					}
				}
			}

		} catch (NXTCommException e) {
			logger.error("Exception when connecting to NXT: " + e.getMessage());
		}
	}
	
	/**
	 * Method for receiving objects
	 * 
	 * @param dataType
	 *            The expected type of data to be received
	 * 
	 * @return the received object
	 */
	@Override
	public Object receiveObject(CommunicationData dataType) throws IOException {
		try {
			super.receiveObject(dataType);
			
			logger.info("Received object of type " + dataType.name() + " from "+ nxtInfo.name);
		} catch (IOException e) {
			logger.error("IOException when trying to receive object from " + nxtInfo.name + ": " + e.getMessage());
		}

		return null;
	}
	
	/**
	 * Method for sending objects
	 * 
	 * @param inputObject
	 *            The type of object being sent
	 */
	@Override
	public void sendObject(Object inputObject) throws IOException {
		try {
			// Call parent method to send the object
			super.sendObject(inputObject);
			
			logger.info("Sent object of type" + inputObject.getClass() + " to "+ nxtInfo.name);
		} catch (IOException e) {
			logger.error("IOException when trying to send object to " + nxtInfo.name + ": " + e.getMessage());
		}
	}
}
