package tests.motion;

import actions.Movement;
import actions.RobotInterface;
import interfaces.Action;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import main.Configuration;
import rp.systems.StoppableRunnable;

public class MotionTesting implements StoppableRunnable{
	private int lineValue = 0;
	private int backgroundValue = 0;
	private int MID_BOUND;
	private int high = 0; //darker is higher value
	private int low = 999;
	private Movement move;
	private LightSensor LEFT_SENSOR;
	private LightSensor RIGHT_SENSOR;
	private RobotInterface rInterface;
	
	public MotionTesting() {
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		MID_BOUND = 0;
	}

	@Override
	public void run() {
		rInterface = new RobotInterface();
		rInterface.setCurrentDirection(Action.FORWARD);
		rInterface.setDropLocation("");
		rInterface.setJobCode(000000);
		rInterface.setCurrentRoute("N/A");
		rInterface.setPickLocation("");
		userCalibration();
		lineValue = getLine();
		backgroundValue = getBackground();
		MID_BOUND = (lineValue + backgroundValue) /2;
		Button.waitForAnyPress();
		move = new Movement(MID_BOUND, rInterface);
		System.out.println("Test 1");
		//Test1();
		Button.waitForAnyPress();
		System.out.println("Test 2");
		Test2();
	}
	
	public static void main(String[] args) {
		MotionTesting mT = new MotionTesting();
		mT.run();
	}
	
	private int getAverageLight() {
		int value = (LEFT_SENSOR.getNormalizedLightValue() + RIGHT_SENSOR.getNormalizedLightValue()) / 2;
		System.out.println(value);
		return value;
	}

	public int getLine() {
		return high;
	}
	
	public int getBackground() {
		return low;
	}
	
	private void userCalibration() {
		for (int i = 0; i < 1; i ++) {
			System.out.println("Place the bot's sensors over line (" + i + ").");
			Button.waitForAnyPress();
			int readValue = getAverageLight();
			if (readValue > high) high = readValue;
		}
		
		for (int i = 0; i < 1; i ++) {
			System.out.println("Place the bot's sensors over background (" + i + ").");
			Button.waitForAnyPress();
			int readValue = getAverageLight();
			if (readValue < low) low = readValue;
		}
		
	}
	
	public void Test1() {
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
		move.nextAction(Action.FORWARD, 0);
	} 
	
	public void Test2() {
		move.nextAction(Action.RIGHT,0);
		move.nextAction(Action.FORWARD, 0);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
