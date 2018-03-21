package actions;

import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

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
	Action currentDirection;
	
	
	
	public RobotInterface(){
		this.itemQuantity = 0;
		this.itemsHeld = 0;
		this.robotName = "";
		
	}
	
	public void setRobotName(String receiveString) {
		robotName = receiveString;
		
	}
	
	public void setJobCode(int jobID){
		jobCode = jobID;
	}
	
	
	public void setDropLocation(String string){
		dropLocation = string;
	}
	
	
	public void setPickLocation(String pickLocationPoint){
		pickLocation = pickLocationPoint;
	}
	
	
	public void setCurrentRoute(String whereImGoing){
		currentRoute = whereImGoing;
	}
	
	
	public void setCurrentDirection(Action receiveAction) {
		currentDirection = receiveAction;
	}
	
	

	/**
	 * Prints a message on the robot to tell the user to calibrate the sensors.
	 */
	
	 public void sensorCalibrationMessage(){
		LCD.clear();
		LCD.drawString("Press a button", 1, 0 );
		LCD.drawString("to calibrate", 1, 1 );
		LCD.drawString("the sensors", 1, 2 );
		Button.waitForAnyPress();
	
	}
	
	
	/**
	 * Prints a message received from blue tooth.
	 * @param message
	 */
	public void networkMessage(String message) {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);
		LCD.drawString("Status", 1, 1 );
		LCD.drawString(message, 1, 2 );
		
	}
	
	public void networkMessage(Action currentCommand) {
		LCD.clear();
		LCD.drawString(robotName, 1, 0);
		System.out.println(currentCommand);
		
	}
	
	/**
	 * Prints a message on the robot to tell the user the robot is waiting for orders.
	 */
	public void waitingForOrdersMessage(){
		LCD.clear();
		LCD.drawString(robotName, 1, 0 );
		LCD.drawString("is waiting",1, 1 );
		LCD.drawString("for orders.", 1, 2);
		
		
	}
	
	/**
	 * Prints a message when the robot is travelling towards the drop off location.
	 */
	 public void movingToDestinationMessage(){
		LCD.clear();
		LCD.drawString(robotName, 1, 0 );
		LCD.drawString("is moving to", 1, 1 );
		LCD.drawString("the drop point.", 1, 2 );
		LCD.drawString("Job ID: ", 1, 3);
		LCD.drawInt(jobCode, 1,4);
		LCD.drawString("Destination Coordinates: ", 1, 5);
		LCD.drawString(dropLocation, 1,6);
	}
	
	/**
	 * Prints a message when the robot is travelling towards the item.
	 */
	 public void movingToItemMessage(){
		LCD.clear();
		LCD.drawString(robotName + " is", 1, 0);
		LCD.drawString("moving to the collection point", 1, 1);
		LCD.drawString("Job ID: " + jobCode, 1, 2);
		LCD.drawString("Destination Coordinates: ", 1, 3);
		LCD.drawString(dropLocation, 1,4);
	}
	
	/**
	 * A method to drop and pick items and cancel orders.
	 */
	public void waitForLoadingMessage(int amount){
		while (itemQuantity != amount) {
			LCD.drawString("Current items " + itemQuantity, 1,0);
			Button.waitForAnyPress();
			itemQuantity = pickItems(itemQuantity);
			LCD.drawString("Items picked up: " + itemQuantity, 1, 1);
			LCD.clear();
			}
		}
//			if(Button.waitForAnyPress() == Button.ID_ESCAPE){
//				LCD.clear();
//				System.out.println("You are cancelling the order. If you wish to confirm press ENTER. "
//						+ "If you do not wish to cancel press any other button.");
//				if(Button.waitForAnyPress() == Button.ID_ENTER){
//					robotInfo.cancelJob();
//				}
//				else{
//					loadItemsMessage();
//				}
//			}
			
	
		
	
	/**
	 * A method to pick items after the previous job has been unloaded.
	 */
	public void waitForUnloadingMessage(){
		while (itemsHeld != 0) {
			System.out.println("Current items " + itemsHeld);
			Button.waitForAnyPress(); 
			itemsHeld = dropItems(itemsHeld);
			LCD.drawString("Items dropped off: " + itemsHeld, 1, 1);
			LCD.clear();
			break;
			}
			
		}
		
		
	/**
	 * Prints a message to tell the user to load items.
	 */
	 public void loadItemsMessage(int amount){
		LCD.clear();
		Sound.twoBeeps();
		LCD.drawString(robotName, 1, 0);
		LCD.drawString("Job ID: " + jobCode, 1, 1);
		LCD.drawString("Please load ", 1, 2);
		LCD.drawString(amount + " items.", 1, 3);
		LCD.drawString("Press a button", 1, 4);
		LCD.drawString("to load the", 1, 5);
		LCD.drawString("items.", 1, 6);
		waitForLoadingMessage(amount);
		itemsHeld += itemQuantity;
		resetQuantity();
	}
	
	/**
	 * Prints a message to tell the user to unload items.
	 */
	 public void unloadItemsMessage(){
		LCD.clear();
		Sound.twoBeeps();
		LCD.drawString(robotName, 1, 0);
		LCD.drawString("Job ID: " + jobCode, 1, 1);
		LCD.drawString("Please unload ", 1, 2);
		LCD.drawString(itemsHeld + " items.", 1, 3);
		LCD.drawString("Press a button", 1, 4);
		LCD.drawString("to unload the", 1, 5);
		LCD.drawString("items.", 1, 6);
		LCD.drawString("Current items " + itemsHeld, 1, 7);
		waitForUnloadingMessage();
		//LCD.clear();
	}
	
	/**
	 * A method to pick up items.
	 * @param noOfItems The number of items to be picked up.
	 */
	public int pickItems(int noOfItems){
		return noOfItems + 1;
	}
	
	/**
	 * A method to drop items off. 
	 * @param noOfItems The number of items to be dropped off.
	 */
	public int dropItems(int noOfItems){
		noOfItems = noOfItems - 1;
		if(noOfItems <= 0){
			noOfItems = 0;
		}
		return noOfItems;
	}

	/** 
	 * A method to reset the quantity. Calling this method sets the quantity to 0.
	 */
	public void resetQuantity(){
		itemQuantity = 0;
		
	}
	/**
	 * A method to return the quantity of items.
	 * @return the amount of items.
	 */
	public int getQuantity(){
		return itemQuantity;
	}
	

	public void getRobotName() {
		// TODO Auto-generated method stub
		System.out.println(robotName);
	}

	public void getJobCode() {
		// TODO Auto-generated method stub
		System.out.println(jobCode);
	}

	public void getDropLocation() {
		// TODO Auto-generated method stub
		System.out.println(dropLocation);
	}

	public void getCurrentDirection() {
		// TODO Auto-generated method stub
		System.out.println(currentDirection);
	}

	

	

}