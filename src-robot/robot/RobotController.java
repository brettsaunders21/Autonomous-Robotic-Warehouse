import lejos.nxt.LightSensor;
import lejos.util.Delay;
import rp.systems.StoppableRunnable;

public class RobotController implements StoppableRunnable{
	private Movement move;
	private  final LightSensor LEFT_SENSOR;
	private final LightSensor RIGHT_SENSOR;
	private boolean running;
	
	public RobotController() {
		LEFT_SENSOR = new LightSensor(Configuration.LEFT_LIGHT_SENSOR);
		RIGHT_SENSOR = new LightSensor(Configuration.RIGHT_LIGHT_SENSOR);
		move = new Movement(calibrate());
		running = true;
	}

	@Override
	public void run() {
		// TODO Start connection
		Action currentCommand = null;
		int pickAmount = 0;
		while (running) {
			//Print message
			currentCommand = Action.valueOf(receiveCommand());
			if (currentCommand != null) {
				//Print Message
				if (currentCommand.equals(Action.PICKUP)) {
					pickAmount = receiveAmmount();
				}
				move.nextAction(currentCommand, pickAmount);
			} else {
				System.out.println("Error: No command received");
				break;
			}
		}
		sendCommand("ACTION COMPLETE");
	}

	@Override
	public void stop() {
		running = false;
		
	}
	
	public String receiveCommand() {
		//Replace with bluetooth code
		return "FORWARD";
	}
	
	public int receiveAmmount() {
		return 3;
	}
	
	public void sendCommand(String command) {
		//Replace with bluetooth code
	}
	
	public int calibrate() {
		Delay.msDelay(500);
		return (LEFT_SENSOR.readValue() + RIGHT_SENSOR.readValue()) / 2;
	}
	
	public static void main(String[] args) {
		RobotController rc = new RobotController();
		rc.run();
	}

}
