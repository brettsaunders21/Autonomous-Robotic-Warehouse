package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.List;

import interfaces.Robot;

public class Counter {
	private int time = 0;
	//private List<Boolean> readyToMove;
	private ConcurrentMap<String, Integer> nameIndex = new ConcurrentHashMap<String, Integer>();
	private List<Boolean> readyToMove;
	
	public Counter(Robot[] ROBOTS) {
		//readyToMove = new boolean[ROBOTS.length];
		readyToMove = Collections.synchronizedList(new ArrayList<Boolean>());
		int i=0;
		for (Robot robot : ROBOTS) {
			readyToMove.add(i,false);
			nameIndex.put(robot.getRobotName(), i);
			i++;
		}
	}
	
	public synchronized void readyToMove(String robotName) {
		//readyToMove[nameIndex.get(robotName)] = true;
		readyToMove.set(nameIndex.get(robotName), true);
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
			for (int i = 0; i<readyToMove.size(); i++) {
				//readyToMove[i] = false;
				readyToMove.set(i, false);
			}
		}
		return flag;
	}
	
	public int getTime() {
		return time;
	}
}