package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class RobotNetworkHandler implements Runnable {

	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private BTConnection bluetoothConnection;
	private boolean connected;

	/*
	 * The constructor
	 */
	public RobotNetworkHandler() {
		connected = false;
	}

	@Override
	public void run() {
		// Establish a bluetooth connection and start input and output streams
		System.out.println("Waiting for bluetooth connection...");
		bluetoothConnection = Bluetooth.waitForConnection();
		outputStream = bluetoothConnection.openDataOutputStream();
		inputStream = bluetoothConnection.openDataInputStream();
		connected = true;
		System.out.println("Bluetooth connection established! Receiver and sender streams created");
	}

	public boolean isConnected() {
		return connected;
	}

	/*
	 * Method for receiving objects from PC
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
	 * Method for sending objects to PC
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
}
