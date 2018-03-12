package actions;

//import job.Job;
//import interfaces.Robot;
//import lejos.nxt.Button;
//import lejos.util.Delay;
//import lejos.nxt.ButtonListener;
//import lejos.nxt.LCD;

public class RobotInterface {

	private int itemQuantity;
	//private Robot robotInfo;
	//private Job jobInfo;

	public RobotInterface() {
		this.itemQuantity = 0;

	}
/**
	public void sensorCalibrationMessage() {
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println("Press a button to calibrate the sensors.");
		Button.waitForAnyPress();

	}

	public void networkMessage(String message) {
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println(message);

	}

	public void waitingForOrdersMessage() {
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + "is waiting for orders.");

	}

	public void movingMessage() {
		if (robotInfo.getActiveJob().getDropLocation() != null) {
			movingToDestinationMessage();
		} else {
			movingToItemMessage();
		}
	}

	public void movingToDestinationMessage() {
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + " is moving to the drop point.");
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Destination Coordinates: " + jobInfo.getDropLocation());

	}

	public void movingToItemMessage() {
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + " is moving to the collection point.");
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Destination Coordinates: "); // need to add coordinates for item pickup
	}

	public void destinationMessage() {
		if (robotInfo.getActiveJob().getDropLocation() != null) {
			unloadItemsMessage();
		} else {
			loadItemsMessage();
		}

	}

	// public void waitForLoadingMessage(){

	// }

	// public void waitForUnloadingMessage(){

	// }

	public void loadItemsMessage() {
		LCD.clear();
		//LCD.drawString(robotInfo.getRobotName(), 1, 0);
		//System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Press a button to load the items.");
		Button.waitForAnyPress();
	}

	public void unloadItemsMessage() {
		LCD.clear();
		//LCD.drawString(robotInfo.getRobotName(), 1, 0);
		//System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Press a button to unload the items.");
		Button.waitForAnyPress();
	}
**/
	public void pickItems(int noOfItems) {
		itemQuantity++;
	}

	public void dropItems(int noOfItems) {
		itemQuantity--;
		if (itemQuantity < 0) {
			itemQuantity = 0;
		}
	}

	public void resetQuantity() {
		itemQuantity = 0;

	}

	public int getQuantity() {
		return itemQuantity;
	}

}
