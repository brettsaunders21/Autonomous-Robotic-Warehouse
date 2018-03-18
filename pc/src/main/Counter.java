package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import interfaces.Robot;

public class Counter {
	private int time = 0;
	private boolean[] readyToMove;
	private ConcurrentMap<String, Integer> nameIndex = new ConcurrentHashMap<String, Integer>();
	
	public Counter(Robot[] ROBOTS) {
		readyToMove = new boolean[ROBOTS.length];
		int i=0;
		for (Robot robot : ROBOTS) {
			readyToMove[i] = false;
			nameIndex.put(robot.getRobotName(), i++);
		}
	}
	
	public synchronized void readyToMove(String robotName) {
		readyToMove[nameIndex.get(robotName)] = true;
	}
	
	public synchronized boolean canMove() {
		boolean flag = true;
		for (boolean move : readyToMove) {
			if (!move) {
				flag=false;
			}
		}
		if (flag) {
			time++;
			for (int i = 0; i<readyToMove.length; i++) {
				readyToMove[i] = false;
			}
		}
		return flag;
	}
	
	public int getTime() {
		return time;
	}
}