package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCNetworkHandler implements Runnable {

	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private NXTInfo nxtInfo;

	public PCNetworkHandler(NXTInfo _nxtInfo) {
		nxtInfo = _nxtInfo;
	}
	
	@Override
	public void run() {
		System.out.println("Attempting to connect to: " + nxtInfo.name);
		
		try {
			NXTComm nxtComm = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);
			
			if (nxtComm.open(nxtInfo)) {

				inputStream = new DataInputStream(nxtComm.getInputStream());
				outputStream = new DataOutputStream(nxtComm.getOutputStream());
			}
		} catch (NXTCommException e) {
			System.out.println("Exception when connecting to NXT: " + e.getMessage());
		}
	}

	public boolean isConnected() {
		return outputStream != null;
	}

	/*
	 * Method for receiving objects from NXT
	 */
	public Object receiveObject(Object inputObject) throws IOException {
		// Read the right kind of data dependent on the format of the input - most will
		// be handled by the enum or integer, the others included for redundancy
		if (inputObject instanceof Integer || inputObject instanceof Enum) {
			return inputStream.readInt();
		} else if (inputObject instanceof String) {
			return inputStream.readUTF();
		} else if (inputObject instanceof Double) {
			return inputStream.readDouble();
		} else if (inputObject instanceof Float) {
			return inputStream.readFloat();
		}

		return null;
	}

	/*
	 * Method for sending objects to NXT
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
