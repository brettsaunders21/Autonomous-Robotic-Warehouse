package actions;

//import job.Job;
//import interfaces.Robot;
import lejos.nxt.Button;
//import lejos.util.Delay;
//import lejos.nxt.ButtonListener;
//import lejos.nxt.LCD;

public class RobotInterface {
	
	private int itemQuantity;
	//private Robot robotInfo;
	//private Job jobInfo;
	
	
	public RobotInterface(){
		this.itemQuantity = 0;

	}


	/**
	 * Prints a message on the robot to tell the user to calibrate the sensors.
	 */
	
	/** public void sensorCalibrationMessage(){
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println("Press a button to calibrate the sensors.");
		Button.waitForAnyPress();
	
	}

	**/
	
	/**
	 * Prints a message received from blue tooth.
	 * @param message
	 */
	/** public void networkMessage(String message) {
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println(message);
		
	}
	
	/**
	 * Prints a message on the robot to tell the user the robot is waiting for orders.
	 */
	/** public void waitingForOrdersMessage(){
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + "is waiting for orders.");
		
	}
	
	/**
	 * Prints a message when the robot is travelling. Different messages are used depending on whether 
	 * the robot is travelling towards the item or the drop off location.
	 */
	/** public void movingMessage(){
		if(robotInfo.getActiveJob().getDropLocation() != null){
			movingToDestinationMessage();	
		}
		else{
			movingToItemMessage();
		}
	}
	
	/**
	 * Prints a message when the robot is travelling towards the drop off location.
	 */
	/** public void movingToDestinationMessage(){
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + " is moving to the drop point.");
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Destination Coordinates: " + jobInfo.getDropLocation());
		
	}
	
	/**
	 * Prints a message when the robot is travelling towards the item.
	 */
	/** public void movingToItemMessage(){
		LCD.clear();
		System.out.println(robotInfo.getRobotName() + " is moving to the collection point.");
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Destination Coordinates: ");  // need to add coordinates for item pickup
	}
	
	/**
	 * Prints a message when the robot arrives at the drop off location. Message dependent on whether the robot is preparing to load or unload.
	 */
	/** public void destinationMessage(){
		if(robotInfo.getActiveJob().getDropLocation() != null){
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
	if (Button.waitForAnyPress() == Button.ID_LEFT){
		dropItems(itemQuantity);
		if (Button.waitForAnyPress() == Button.ID_RIGHT){
			pickItems(itemQuantity);
		
			if(Button.waitForAnyPress() == Button.ID_ESCAPE){
				LCD.clear();
				System.out.println("You are cancelling the order. If you wish to confirm press ENTER. "
						+ "If you do not wish to cancel press any other button.");
				if(Button.waitForAnyPress() == Button.ID_ENTER){
					robotInfo.cancelJob();
				}
				else{
					loadItemsMessage();
				}
			}
		}
	}
	}
		
		}
	
	/**
	 * A method to pick items after the previous job has been unloaded.
	 */
	public void waitForUnloadingMessage(int amount){
		System.out.println("Drop off: " + itemQuantity);
		while (itemQuantity != 0) {
	if (Button.waitForAnyPress() == Button.ID_LEFT){
		pickItems(itemQuantity);
		if (Button.waitForAnyPress() == Button.ID_RIGHT){
			dropItems(itemQuantity);
		}
		
		}
		}
	}
		
	/**
	 * Prints a message to tell the user to load items.
	 */
	/** public void loadItemsMessage(){
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Press a button to load the items.");
		waitForLoadingMessage(itemQuantity);
	}
	
	/**
	 * Prints a message to tell the user to unload items.
	 */
	/** public void unloadItemsMessage(){
		LCD.clear();
		LCD.drawString(robotInfo.getRobotName(), 1, 0);
		System.out.println("Job ID: " + jobInfo.getID());
		System.out.println("Press a button to unload the items.");
		waitForUnloadingMessage(itemQuantity);
	}
	
	/**
	 * A method to pick up items.
	 * @param noOfItems The number of items to be picked up.
	 */
	public void pickItems(int noOfItems){
		itemQuantity++;
	}
	
	/**
	 * A method to drop items off. 
	 * @param noOfItems The number of items to be dropped off.
	 */
	public void dropItems(int noOfItems){
		itemQuantity--;
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


