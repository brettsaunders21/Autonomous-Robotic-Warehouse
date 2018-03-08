package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/*
 * PCNetworkHandler class
 * 
 * Handles the bluetooth connection for the PC
 * 
 * Notes about pairing from lejos bluetooth example:
 * You must pair every NXT with the PC for the Bluetooth connection to work (hint: on Ubuntu I connected to each NXT
 * using a known PIN, although for some reason when setting up each connection I had to select PIN Options twice
 * before it remembered my choice). Once paired you can use nxjbrowse -b to get the name and address of the NXTs.
 * This information should be put into AdderConnection.java in place of the details I've put there. Next, compile and
 * upload AdderClient.java to each NXT then run it. Finally run AdderConnection.java and it should talk to all NXTs
 * in parallel and double-check what they're sending back. Note that it appeears that the NXTComm Bluetooth class (or
 * some other related part of the comms infrastructure) is not thread safe, so concurrent calls to it should be
 * synchronised.
 */

public class PCNetworkHandler extends AbstractSenderReceiver {

	private NXTInfo nxtInfo;

	/**
	 * The constructor
	 *
	 * @param _nxtInfo The NXTInfo object of the robot being connected to
	 */
	public PCNetworkHandler(NXTInfo _nxtInfo) {
		nxtInfo = _nxtInfo;
	}

	@Override
	public void run() {
		System.out.println("Attempting to connect to: " + nxtInfo.name);

		try {
			// Create an NXTComm object ready to connect using the bluetooth protocol
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

			// If the NXT is ready for connection, connect to it adnd create data streams
			if (nxtComm.open(nxtInfo)) {

				inputStream = new DataInputStream(nxtComm.getInputStream());
				outputStream = new DataOutputStream(nxtComm.getOutputStream());
			}
		} catch (NXTCommException e) {
			System.out.println("Exception when connecting to NXT: " + e.getMessage());
		}
	}
}
