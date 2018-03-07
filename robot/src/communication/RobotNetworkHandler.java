package communication;

import communication.AbstractSenderReceiver;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class RobotNetworkHandler extends AbstractSenderReceiver {
	private BTConnection bluetoothConnection;

	@Override
	public void run() {
		// Establish a bluetooth connection and start input and output streams
		System.out.println("Waiting for bluetooth connection...");
		bluetoothConnection = Bluetooth.waitForConnection();
		outputStream = bluetoothConnection.openDataOutputStream();
		inputStream = bluetoothConnection.openDataInputStream();
		System.out.println("Bluetooth connection established! Receiver and sender streams created");
	}
}
