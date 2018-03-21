package main;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import rp.config.WheeledRobotConfiguration;

public class Configuration {
	
	public static final WheeledRobotConfiguration CUSTOM_EXPRESS_BOT = new WheeledRobotConfiguration(0.056f, 0.163f,
			0.240f, Motor.B, Motor.C);
	
	public static final SensorPort LEFT_LIGHT_SENSOR = SensorPort.S1;
	public static final SensorPort RIGHT_LIGHT_SENSOR = SensorPort.S3;
	
}