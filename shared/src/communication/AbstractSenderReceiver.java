package communication;

import interfaces.Action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * AbstractSenderReceiverClass
 * 
 * Contains the data input and output streams which will be used by the PC an robot,
 * and includes methods for sending and receiving objects.
 */

public abstract class AbstractSenderReceiver implements Runnable {
	// Input/output data streams
	protected DataOutputStream outputStream;
	protected DataInputStream inputStream;

	/**
	 * Method for receiving objects
	 * 
	 * @param dataType
	 *            The expected type of data to be received
	 * 
	 * @return the received object
	 */
	public Object receiveObject(CommunicationData dataType) throws IOException {
		// Read the right kind of data dependent on the format of the input
		if (dataType == CommunicationData.ACTION) {
			// If an action is being received, convert it from a string to an action object
			Action receivedAction = Action.valueOf(inputStream.readUTF());
			return receivedAction;
		} else if (dataType == CommunicationData.INT) {
			return inputStream.readInt();
		} else if (dataType == CommunicationData.STRING) {
			return inputStream.readUTF();
		} else if (dataType == CommunicationData.DOUBLE) {
			return inputStream.readDouble();
		} else if (dataType == CommunicationData.FLOAT) {
			return inputStream.readFloat();
		}

		return null;
	}

	/**
	 * Method for sending objects
	 * 
	 * @param inputObject
	 *            The type of object being sent
	 */
	public void sendObject(Object inputObject) throws IOException {
		if (inputObject instanceof Action) {
			// If an action is being sent, change it to a string first.
			Action inputAction = (Action) inputObject;
			outputStream.writeUTF(inputAction.name());
		} else if (inputObject instanceof Integer) {
			outputStream.writeInt((int) inputObject);
		} else if (inputObject instanceof String) {
			outputStream.writeUTF((String) inputObject);
		} else if (inputObject instanceof Double) {
			outputStream.writeDouble((double) inputObject);
		} else if (inputObject instanceof Float) {
			outputStream.writeFloat((float) inputObject);
		}

		// Include an exception for any other data types
		outputStream.flush();
	}

	/**
	 * Method for checking whether the PC or robot is connected to the other device
	 * 
	 * @return true if the device is connected
	 */
	public boolean isConnected() {
		return outputStream != null;
	}
}
