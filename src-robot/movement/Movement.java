import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class Movement {
	private final int adjustmentValue;
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	private final int CORRECTION = 7;
	private Configuration c = new Configuration();
	
	public Movement(int _adjustmentValue) {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		adjustmentValue = _adjustmentValue;
	}
	
	public void nextAction(Action a) {
		switch(a) {
		case WAIT: 
			Delay.msDelay(100);
			return;
		case FORWARD:
			break;
		case LEFT:
			PILOT.rotate(10);
			while (!isOnLine(LEFT_SENSOR.readValue())) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(10);
			break;
		case RIGHT: 
			PILOT.rotate(10);
			while (!isOnLine(RIGHT_SENSOR.readValue())) {
				PILOT.rotateRight();
			}
			PILOT.rotate(10);
			break;
		case BACKWARD: 
			PILOT.travel(-0.1);
			PILOT.rotate(20);
			while (!isOnLine(LEFT_SENSOR.readValue())) {
				PILOT.rotateLeft();
			}
			PILOT.rotate(20);
			break;
		}
	}
	
	public boolean isOnLine(int lightValue) {
		return Math.abs(adjustmentValue - lightValue) > CORRECTION;
	}

}
