package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import org.apache.log4j.Logger;

import interfaces.Action;

/*
 * PCNetworkHandler class
 * 
 * Handles the bluetooth connection for the PC
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
		final int MAX_CONNECTION_RETRIES = 10;

		// Time delay between attempting a reconnect
		final int RETRY_DELAY = 1000;

		logger.info("Attempting to connect to: " + nxtInfo.name);
		// Create an NXTComm object ready to connect using the bluetooth protocol
		NXTConnector nxtConnector = new NXTConnector();
		for (int i = 0; i < MAX_CONNECTION_RETRIES; i++) {

			/*
			 * If the NXT is ready for connection, connect to it and create data streams,
			 * otherwise try again after a delay
			 */
			if (nxtConnector.connectTo(nxtInfo, NXTComm.PACKET)) {
				inputStream = new DataInputStream(nxtConnector.getInputStream());
				outputStream = new DataOutputStream(nxtConnector.getOutputStream());

				logger.info("Successfully connected to: " + nxtInfo.name);
				return;
			} else {
				logger.warn("Attempt " + i + " failed. Retrying in " + RETRY_DELAY + " seconds");
				try {
					Thread.sleep(RETRY_DELAY);
				} catch (InterruptedException e) {
					logger.error("PCNetworkHandler thread interrupted.");
					return;
				}
			}
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
			// Call parent method to receive the object
			super.receiveObject(dataType);

			logger.info("Received object of type " + dataType.name() + " from " + nxtInfo.name);
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

			logger.info("Sent object of type" + inputObject.getClass() + " to " + nxtInfo.name);
		} catch (IOException e) {
			logger.error("IOException when trying to send object to " + nxtInfo.name + ": " + e.getMessage());
		}
	}
}
