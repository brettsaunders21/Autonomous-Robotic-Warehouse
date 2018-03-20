package main;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

public class Configuration {
	public static final DifferentialPilot PILOT = new DifferentialPilot(56, 110.5, Motor.A, Motor.C);
	
	public static final SensorPort LEFT_LIGHT_SENSOR = SensorPort.S1;
	public static final SensorPort RIGHT_LIGHT_SENSOR = SensorPort.S3;
	
}