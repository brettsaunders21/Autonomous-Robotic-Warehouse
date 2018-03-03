import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class Movement {
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private final DifferentialPilot PILOT;
	private Configuration c = new Configuration();
	
	public Movement() {
		PILOT = new WheeledRobotSystem(Configuration.CUSTOM_EXPRESS_BOT).getPilot();
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
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
			
		}
	}
	
	public boolean isOnLine(int lightValue) {
		return Math.abs(10 - lightValue) > 6;
	}

}
