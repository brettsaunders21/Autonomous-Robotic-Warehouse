package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractSenderReceiver implements Runnable{
	protected DataOutputStream outputStream;
	protected DataInputStream inputStream;

	/*
	 * Method for receiving objects
	 */
	public Object receiveObject(Object inputObject) throws IOException {
		// Read the right kind of data dependent on the format of the input - most will
		// be handled by the enum or integer, the others included for redundancy
		if (inputObject instanceof String || inputObject instanceof Enum) {
			return inputStream.readUTF();
		} else if (inputObject instanceof Integer) {
			return inputStream.readInt();
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
		if (inputObject instanceof Integer || inputObject instanceof Enum) {
			outputStream.writeInt((int) inputObject);
		} else if (inputObject instanceof String) {
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
