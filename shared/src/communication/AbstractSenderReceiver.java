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
	public Object receiveObject(CommunicationData dataType) throws IOException {
		// Read the right kind of data dependent on the format of the input - most will
		// be handled by Action or Integer.
		try {
			if (dataType == CommunicationData.ACTION) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Method for sending objects
	 */
	public void sendObject(Object inputObject) throws IOException {
		try {
			if (inputObject instanceof Integer) {
				outputStream.writeInt((int) inputObject);
			} else if (inputObject instanceof Action) {
				Action inputAction = (Action) inputObject;
				outputStream.writeUTF(inputAction.name());
			} else if (inputObject instanceof String) {
				outputStream.writeUTF((String) inputObject);
			} else if (inputObject instanceof Double) {
				outputStream.writeDouble((double) inputObject);
			} else if (inputObject instanceof Float) {
				outputStream.writeFloat((float) inputObject);
			}

			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return outputStream != null;
	}
}
