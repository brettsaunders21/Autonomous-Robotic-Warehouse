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
	 * Method for receiving Action objects
	 * 
	 * @return the received Action object
	 */
	public Action receiveAction() throws IOException {
		// Get the Action in string form from input stream
		String receivedString = inputStream.readUTF();

		Action receivedAction = null;
		// Convert the string into an Action object
		
		//NOTE - Can't use valueOf as this crashes lejos! Have to use this sub-optimal method instead
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
			receivedAction = Action.ACTION_COMPLETE;
			break;
		default:
			break;
		}

		return receivedAction;
	}
	
	/**
	 * Method for receiving ints
	 * 
	 * @return the received int
	 */
	public int receiveInt() throws IOException {
		return inputStream.readInt();
	}

	/**
	 * Method for receiving strings
	 * 
	 * @return the received string
	 */
	public String receiveString() throws IOException {
		return inputStream.readUTF();
	}

	/**
	 * Method for receiving doubles
	 * 
	 * @return the received double
	 */
	public double receiveDouble() throws IOException {
		return inputStream.readDouble();
	}

	/**
	 * Method for receiving floats
	 * 
	 * @return the received float
	 */
	public float receiveFloat() throws IOException {
		return inputStream.readFloat();
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