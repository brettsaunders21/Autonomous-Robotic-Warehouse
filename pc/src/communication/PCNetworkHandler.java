package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCNetworkHandler extends AbstractSenderReceiver {

	private NXTInfo nxtInfo;

	public PCNetworkHandler(NXTInfo _nxtInfo) {
		super();
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
}
