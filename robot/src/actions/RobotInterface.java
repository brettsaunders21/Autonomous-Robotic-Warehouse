package actions;

import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LCD;
//import lejos.nxt.remote.RemoteNXT;

/*
 * @author Huda Khan <hik649@student.bham.ac.uk>
 */

public class RobotInterface {
	
	private int itemQuantity;
	//private Robot robotInfo;
	String robotName;
	int jobCode;
	String dropLocation;
	String pickLocation;
	String currentRoute;
	Action currentDirections;
	
	//public String brickName = RemoteNXT.getBrickName();
	
	
	public RobotInterface(){
		this.itemQuantity = 0;
		
		
		
	}
// function to read job info (code and drop location)
	//way to get the name from nxt
	
	public void setRobotName(String receiveString) {
		robotName = receiveString;
		
	}
	
	public void setJobCode(int jobID){
		jobCode = jobID;
	}
	
	public int getJobCode(){
		return jobCode;
	}
	
	public void setDropLocation(String string){
		dropLocation = string;
	}
	
	public String getDropLocation(){
		return dropLocation;
	}
	
	public void setPickLocation(String pickLocationPoint){
		pickLocation = pickLocationPoint;
	}
	
	public String getPickLocation() {
		return pickLocation;
	}
	
	public void setCurrentRoute(String whereImGoing){
		currentRoute = whereImGoing;
	}
	
	public String getCurrentRoute(){
		return currentRoute;
	}
	
	public void setCurrentDirection(Action receiveAction) {
		currentDirections = receiveAction;
	}
	
	public Action getCurrentDirection(){
		return currentDirections;
	}
	

	/**
	 * Prints a message on the robot to tell the user to calibrate the sensors.
	 */
	
	 public void sensorCalibrationMessage(){
		LCD.clear();
		
		LCD.drawString("brickName", 1, 0);
		System.out.println("Press a button to calibrate the sensors.");
		Button.waitForAnyPress();
	
	}
	
	
	/**
	 * Prints a message received from blue tooth.
	 * @param message
	 */
	public void networkMessage(String message) {
		LCD.clear();
		LCD.drawString("brickName", 1, 0);
		System.out.println("Status: " + message);
		
	}
	
	public void networkMessage(Action currentCommand) {
		LCD.clear();
		LCD.drawString("brickName", 1, 0);
		System.out.println(currentCommand);
		
	}
	
	/**
	 * Prints a message on the robot to tell the user the robot is waiting for orders.
	 */
	public void waitingForOrdersMessage(){
		LCD.clear();
		System.out.println("brickName" + "is waiting for orders.");
		
	}
	
	/**
	 * Prints a message when the robot is travelling. Different messages are used depending on whether 
	 * the robot is travelling towards the item or the drop off location.
	 */
	 public void movingMessage(){
		if(getDropLocation() != null){
			movingToDestinationMessage();	
		}
		else{
			movingToItemMessage();
		}
	}
	
	/**
	 * Prints a message when the robot is travelling towards the drop off location.
	 */
	 public void movingToDestinationMessage(){
		LCD.clear();
		System.out.println("brickName" + " is moving to the drop point.");
		System.out.println("Job ID: " + getJobCode());
		System.out.println("Destination Coordinates: " + getDropLocation());
	}
	
	/**
	 * Prints a message when the robot is travelling towards the item.
	 */
	 public void movingToItemMessage(){
		LCD.clear();
		System.out.println("brickName" + " is moving to the collection point.");
		System.out.println("Job ID: " + getJobCode());
		System.out.println("Destination Coordinates: " + getDropLocation()); 
	}
	
	
	/**
	 * Prints a message when the robot arrives at the drop off location. Message dependent on whether the robot is preparing to load or unload.
	 */
	 public void destinationMessage(){
		if(getDropLocation() != null){
			unloadItemsMessage();	
		}
		else{
			loadItemsMessage();
		}
		
	}
	
	/**
	 * A method to drop and pick items and cancel orders.
	 */
	public void waitForLoadingMessage(int amount){
		System.out.println("Pick up: " + amount);
		while (itemQuantity != amount) {
			switch (Button.waitForAnyPress()) {
			case Button.ID_LEFT:
				dropItems(itemQuantity);
				break;
			case Button.ID_RIGHT:
				pickItems(itemQuantity);
				break;
			}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
	}
		
	
	/**
	 * A method to pick items after the previous job has been unloaded.
	 */
	public void waitForUnloadingMessage(int amount){
		System.out.println("Drop off: " + itemQuantity);
		itemQuantity = 1;
		while (itemQuantity != 0) {
			switch (Button.waitForAnyPress()) {
			case Button.ID_LEFT:
				dropItems(itemQuantity);
				break;
			case Button.ID_RIGHT:
				pickItems(itemQuantity);
				break;
			}
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	/**
	 * Prints a message to tell the user to load items.
	 */
	 public void loadItemsMessage(){
		LCD.clear();
		LCD.drawString("brickName", 1, 0);
		System.out.println("Job ID: " + getJobCode());
		System.out.println("Please load " + itemQuantity + "items.");
		System.out.println("Press a button to load the items.");
		waitForLoadingMessage(itemQuantity);
	}
	
	/**
	 * Prints a message to tell the user to unload items.
	 */
	 public void unloadItemsMessage(){
		LCD.clear();
		LCD.drawString("brickName", 1, 0);
		System.out.println("Job ID: " + getJobCode());
		System.out.println("Please unload " + itemQuantity + "items.");
		System.out.println("Press a button to unload the items.");
		waitForUnloadingMessage(itemQuantity);
	}
	
	/**
	 * A method to pick up items.
	 * @param noOfItems The number of items to be picked up.
	 */
	public void pickItems(int noOfItems){
		itemQuantity++;
		System.out.println(itemQuantity);
	}
	
	/**
	 * A method to drop items off. 
	 * @param noOfItems The number of items to be dropped off.
	 */
	public void dropItems(int noOfItems){
		itemQuantity--;
		System.out.println(itemQuantity);
		if(itemQuantity < 0){
			itemQuantity = 0;
		}
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

	

	

}