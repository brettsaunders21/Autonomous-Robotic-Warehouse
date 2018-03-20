package actions;

import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/*
 * @author Huda Khan <hik649@student.bham.ac.uk>
 */

public class RobotInterface {

	private int itemQuantity;
	private int itemsHeld;
	String robotName;
	int jobCode;
	String dropLocation;
	String pickLocation;
	String currentRoute;
	Action currentDirections;

	public RobotInterface() {
		this.itemQuantity = 0;
		this.itemsHeld = 0;
		this.robotName = "";

	}

	public void setRobotName(String receiveString) {
		robotName = receiveString;

	}

	public void setJobCode(int jobID) {
		jobCode = jobID;
	}

	public void setDropLocation(String string) {
		dropLocation = string;
	}

	public void setPickLocation(String pickLocationPoint) {
		pickLocation = pickLocationPoint;
	}

	public void setCurrentRoute(String whereImGoing) {
		currentRoute = whereImGoing;
	}

	public void setCurrentDirection(Action receiveAction) {
		currentDirections = receiveAction;
	}

	/**
	 * Prints a message on the robot to tell the user to calibrate the sensors.
	 */

	public void sensorCalibrationMessage() {
		LCD.clear();
		System.out.println("Press a button to calibrate the sensors.");
		Button.waitForAnyPress();

	}

	/**
	 * Prints a message received from blue tooth.
	 * 
	 * @param message
	 */
	public void networkMessage(String message) {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);
		System.out.println("Status: " + message);

	}

	public void networkMessage(Action currentCommand) {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);if(Button.waitForAnyPress() == Button.ID_ESCAPE){
		System.out.println(currentCommand);

	}

	/**
	 * Prints a message on the robot to tell the user the robot is waiting for
	 * orders.
	 */
	public void waitingForOrdersMessage() {
		LCD.clear();
		System.out.println(robotName + "is waiting for orders.");

	}

	/**
	 * Prints a message when the robot is travelling towards the drop off
	 * location.
	 */
	public void movingToDestinationMessage() {
		LCD.clear();
		System.out.println(robotName + " is moving to the drop point.");
		System.out.println("Job ID: " + jobCode);
		System.out.println("Destination Coordinates: " + dropLocation);
	}

	/**
	 * Prints a message when the robot is travelling towards the item.
	 */
	public void movingToItemMessage() {
		LCD.clear();
		System.out.println(robotName + " is moving to the collection point.");
		System.out.println("Job ID: " + jobCode);
		System.out.println("Destination Coordinates: " + dropLocation);
	}

	/**
	 * A method to drop and pick items and cancel orders.
	 */
	public void waitForLoadingMessage(int amount) {
		while (itemQuantity != amount) {
			System.out.println("Current items " + itemQuantity);
			switch (Button.waitForAnyPress()) {
			case Button.ID_LEFT:
				itemQuantity = dropItems(itemQuantity);
				break;
			case Button.ID_RIGHT:
				itemQuantity = pickItems(itemQuantity);
				break;
			}
		}
		//

	}

	/**
	 * A method to pick items after the previous job has been unloaded.
	 */
	public void waitForUnloadingMessage() {
		while (itemsHeld != 0) {
			switch (Button.waitForAnyPress()) {
			case Button.ID_LEFT:
				itemsHeld = dropItems(itemsHeld);
				break;
			case Button.ID_RIGHT:
				itemsHeld = pickItems(itemsHeld);
				break;
			}
			System.out.println("Current items " + itemsHeld);
		}
	}

	/**
	 * Prints a message to tell the user to load items.
	 */
	public void loadItemsMessage(int amount) {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);
		System.out.println("Job ID: " + jobCode);
		System.out.println("Please load " + amount + " items.");
		System.out.println("Press a button to load the items.");
		waitForLoadingMessage(amount);
		itemsHeld += itemQuantity;
		resetQuantity();
	}

	/**
	 * Prints a message to tell the user to unload items.
	 */
	public void unloadItemsMessage() {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);
		System.out.println("Job ID: " + jobCode);
		System.out.println("Please unload " + itemsHeld + " items.");
		System.out.println("Press a button to unload the items.");
		System.out.println("Current items " + itemsHeld);
		waitForUnloadingMessage();
		LCD.clear();
	}

	/**
	 * A method to pick up items.
	 * 
	 * @param noOfItems
	 *            The number of items to be picked up.
	 */
	public int pickItems(int noOfItems) {
		return noOfItems + 1;
	}

	/**
	 * A method to drop items off.
	 * 
	 * @param noOfItems
	 *            The number of items to be dropped off.
	 */
	public int dropItems(int noOfItems) {
		noOfItems = noOfItems - 1;
		if (noOfItems <= 0) {
			noOfItems = 0;
		}
		return noOfItems;
	}

	/**
	 * A method to reset the quantity. Calling this method sets the quantity to
	 * 0.
	 */
	public void resetQuantity() {
		itemQuantity = 0;

	}

	/**
	 * A method to return the quantity of items.
	 * 
	 * @return the amount of items.
	 */
	public int getQuantity() {
		return itemQuantity;
	}

}