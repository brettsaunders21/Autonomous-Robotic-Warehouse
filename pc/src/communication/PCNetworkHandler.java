package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
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
	
	// boolean for whether the connection has failed after connection retries
	boolean connectionFailed;

	/**
	 * The constructor
	 *
	 * @param _nxtInfo
	 *            The NXTInfo object of the robot being connected to
	 */
	public PCNetworkHandler(NXTInfo _nxtInfo) {
		nxtInfo = _nxtInfo;
		connectionFailed = false;
	}

	@Override
	public void run() {
		// Amount of connection retries
		final int MAX_CONNECTION_RETRIES = 20;

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
				logger.warn("Attempt " + i + " failed. Retrying in " + RETRY_DELAY + " milliseconds");
				try {
					Thread.sleep(RETRY_DELAY);
				} catch (InterruptedException e) {
					logger.error("PCNetworkHandler thread interrupted.");
					return;
				}
			}
		}
		
		connectionFailed = true;
		return;
	}
	
	/*
	 * Method for returning whether all retries have failed
	 */
	public boolean getConnectionFailed() {
		return connectionFailed;
	}
	
	/**
	 * Method for receiving Action objects
	 * 
	 * @return the received Action object
	 */
	public Action receiveAction() throws IOException {
		try {
			// Call parent method to receive the object
			Action receivedAction = super.receiveAction();
			logger.info("Received Action: "+  receivedAction.name() + " from " + nxtInfo.name);
			
			return receivedAction;
		} catch (IOException e) {
			logger.error("IOException when trying to receive Action from " + nxtInfo.name + ": " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Method for receiving ints
	 * 
	 * @return the received int
	 */
	public int receiveInt() throws IOException {
		try {
			// Call parent method to receive the object
			int receivedInt = super.receiveInt();
			logger.info("Received int: " + receivedInt + " from " + nxtInfo.name);
			
			return receivedInt;
		} catch (IOException e) {
			logger.error("IOException when trying to receive int from " + nxtInfo.name + ": " + e.getMessage());
			return -1;
		}
	}

	/**
	 * Method for receiving strings
	 * 
	 * @return the received string
	 */
	public String receiveString() throws IOException {
		try {
			// Call parent method to receive the object
			String receivedString= super.receiveString();
			logger.info("Received string: " + receivedString + " from " + nxtInfo.name);
			
			return receivedString;
		} catch (IOException e) {
			logger.error("IOException when trying to receive string from " + nxtInfo.name + ": " + e.getMessage());
			return null;
		}
	}

	/**
	 * Method for receiving doubles
	 * 
	 * @return the received double
	 */
	public double receiveDouble() throws IOException {
		try {
			// Call parent method to receive the object
			double receivedDouble = super.receiveDouble();
			logger.info("Received double: " + receivedDouble + " from " + nxtInfo.name);
			
			return receivedDouble;
		} catch (IOException e) {
			logger.error("IOException when trying to receive double from " + nxtInfo.name + ": " + e.getMessage());
			return -1;
		}
	}

	/**
	 * Method for receiving floats
	 * 
	 * @return the received float
	 */
	public float receiveFloat() throws IOException {
		try {
			// Call parent method to receive the object
			float receivedFloat = super.receiveFloat();
			logger.info("Received float: " + receivedFloat + " from " + nxtInfo.name);
			
			return receivedFloat;
		} catch (IOException e) {
			logger.error("IOException when trying to receive float from " + nxtInfo.name + ": " + e.getMessage());
			return -1;
		}
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

			logger.info("Sent object of type" + inputObject.getClass() + " to " + nxtInfo.name + " Contents: " + inputObject.toString());
		} catch (IOException e) {
			logger.error("IOException when trying to send object to " + nxtInfo.name + ": " + e.getMessage());
		}
	}
}
