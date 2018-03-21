package actions;

import java.io.IOException;

import communication.RobotNetworkHandler;
import interfaces.Action;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
/**
 * Detect Junctions Class
 * 
 * 
 * @author Brett Saunders (bjs730)
 * @author Ben Sassoon (bxs714)
 * @author Lik Kan Chung (llc721)
 * @author Dominik Rys (dxr714)
 */
public class DetectJunction extends AbstractBehaviour {
	private RobotNetworkHandler networkHandler;
	private RobotInterface rInterface;
	

	/**
	 * Constructor.
	 * @param rInterface 
	 * @param networkHandler 
	 * @param atJunction 
	 * @param forward 
	 * @param correctRight 
	 * @param correctLeft 
	 * @param arby 
	 * @param _config The WheeledRobotConfiguration being used by the robot
	 * @param route 
	 */
	public DetectJunction(int _MID_BOUND, RobotNetworkHandler _networkHandler, RobotInterface _rInterface) {
		super(_MID_BOUND);
		networkHandler = _networkHandler;
		rInterface = _rInterface;
	}

	@Override
	public boolean takeControl() {
		//If the right sensor detects a line then takeControl
		Delay.msDelay(1);
		return isLeftOnLine() && isRightOnLine();
	}

	@Override
	public void action() {
		PILOT.stop();
		Action currentCommand = null;
		int pickAmount = 0;
		while (true) {
			try {
				rInterface.setJobCode(networkHandler.receiveInt());
				rInterface.waitingForOrdersMessage();
				currentCommand = (Action) networkHandler.receiveAction();
				rInterface.setCurrentDirection(currentCommand);
				if (currentCommand != null) {
					rInterface.networkMessage(currentCommand);
					if (currentCommand.equals(Action.PICKUP) || currentCommand.equals(Action.DROPOFF)) {
						pickAmount = (int) networkHandler.receiveInt();
					}
					move.nextAction(currentCommand, pickAmount);
				} else {
					System.out.println("Error: No command received");
					break;
				}
				networkHandler.sendObject(Action.ACTION_COMPLETE);
			} catch (IOException e) {
				System.out.println("Couldn't receive object in RobotController" + e.getMessage());
			}
		}
	}
}
