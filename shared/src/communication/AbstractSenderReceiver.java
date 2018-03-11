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
			// If action to be received, return the right action object
			
			// NOTE - can't call .valueOf() as this crashes lejos!
			String receivedString = inputStream.readUTF();

			Action receivedAction = null;
			switch (receivedString) {
			case "LEFT":
				receivedAction = Action.LEFT;
				break;
			case "FORWARD":
				receivedAction = Action.FORWARD;
				break;
			case "RIGHT":
				receivedAction = Action.RIGHT;
				break;
			case "BACKWARD":
				receivedAction = Action.BACKWARD;
				break;
			case "TURN_180":
				receivedAction = Action.TURN_180;
				break;
			case "TURN_LEFT":
				receivedAction = Action.TURN_LEFT;
				break;
			case "TURN_RIGHT":
				receivedAction = Action.RIGHT;
				break;
			case "WAIT":
				receivedAction = Action.WAIT;
				break;
			case "PICKUP":
				receivedAction = Action.PICKUP;
				break;
			case "DROPOFF":
				receivedAction = Action.DROPOFF;
				break;
			case "CANCEL":
				receivedAction = Action.CANCEL;
				break;
			case "SHUTDOWN":
				receivedAction = Action.SHUTDOWN;
				break;
			case "ACTION_COMPLETE":
				receivedAction = Action.BACKWARD;
				break;
			default:
				break;
			}

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
	
	public int receiveInt() throws IOException {
		return inputStream.readInt();
	}
	
	public String receiveString() throws IOException {
		return inputStream.readUTF();
	}
	
	public double receiveDouble() throws IOException {
		return inputStream.readDouble();
	}
	
	public float receiveFloat() throws IOException {
		return inputStream.readFloat();
	}
	
	public Action receiveAction() throws IOException {
		String receivedString = inputStream.readUTF();

		Action receivedAction = null;
		switch (receivedString) {
		case "LEFT":
			receivedAction = Action.LEFT;
			break;
		case "FORWARD":
			receivedAction = Action.FORWARD;
			break;
		case "RIGHT":
			receivedAction = Action.RIGHT;
			break;
		case "BACKWARD":
			receivedAction = Action.BACKWARD;
			break;
		case "TURN_180":
			receivedAction = Action.TURN_180;
			break;
		case "TURN_LEFT":
			receivedAction = Action.TURN_LEFT;
			break;
		case "TURN_RIGHT":
			receivedAction = Action.RIGHT;
			break;
		case "WAIT":
			receivedAction = Action.WAIT;
			break;
		case "PICKUP":
			receivedAction = Action.PICKUP;
			break;
		case "DROPOFF":
			receivedAction = Action.DROPOFF;
			break;
		case "CANCEL":
			receivedAction = Action.CANCEL;
			break;
		case "SHUTDOWN":
			receivedAction = Action.SHUTDOWN;
			break;
		case "ACTION_COMPLETE":
			receivedAction = Action.BACKWARD;
			break;
		default:
			break;
		}

		return receivedAction;
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
			outputStream.writeInt((Integer) inputObject);
		} else if (inputObject instanceof String) {
			outputStream.writeUTF((String) inputObject);
		} else if (inputObject instanceof Double) {
			outputStream.writeDouble((Double) inputObject);
		} else if (inputObject instanceof Float) {
			outputStream.writeFloat((Float) inputObject);
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
