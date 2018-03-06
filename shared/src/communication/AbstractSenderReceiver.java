package communication;

import interfaces.Action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractSenderReceiver implements Runnable {
	protected DataOutputStream outputStream;
	protected DataInputStream inputStream;

	/*
	 * Method for receiving objects
	 */
	public Object receiveObject(Object inputObject) throws IOException {
		// Read the right kind of data dependent on the format of the input - most will
		// be handled by Action or Integer. 
		if (inputObject instanceof String) {
			Action receivedAction = Action.valueOf(inputStream.readUTF());
			return receivedAction;
		} else if (inputObject instanceof Integer) {
			return inputStream.readInt();
		}
		
		// These provided for redundancy - could be changed to throw exceptions instead?
		else if (inputObject instanceof String) {
			return inputStream.readUTF();
		} else if (inputObject instanceof Double) {
			return inputStream.readDouble();
		} else if (inputObject instanceof Float) {
			return inputStream.readFloat();
		} 
		
		return null;
	}

	/*
	 * Method for sending objects
	 */
	public void sendObject(Object inputObject) throws IOException {
		// Most data types will be
		if (inputObject instanceof Integer) {
			outputStream.writeInt((int) inputObject);
		} else if (inputObject instanceof Action) {
			Action inputAction = (Action) inputObject;
			outputStream.writeUTF(inputAction.name());
		}

		// Below provided for redundancy - could be changed to throw exceptions instead?
		else if (inputObject instanceof String) {
			outputStream.writeUTF((String) inputObject);
		} else if (inputObject instanceof Double) {
			outputStream.writeDouble((double) inputObject);
		} else if (inputObject instanceof Float) {
			outputStream.writeFloat((float) inputObject);
		}

		outputStream.flush();
	}

	public boolean isConnected() {
		return outputStream != null;
	}
}
