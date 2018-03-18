package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import interfaces.Robot;

public class Counter {
	private AtomicInteger time;
	private AtomicInteger robotsMoved;
	
	private ConcurrentMap<String, Integer> nameIndex = new ConcurrentHashMap<String, Integer>();
	private List<Boolean> readyToMove;
	
	public Counter(Robot[] ROBOTS) {
		readyToMove = Collections.synchronizedList(new ArrayList<Boolean>());
		int i=0;
		for (Robot robot : ROBOTS) {
			readyToMove.add(i,false);
			nameIndex.put(robot.getRobotName(), i);
			i++;
		}
	}
	
	public synchronized void readyToMove(String robotName) {
		readyToMove.set(nameIndex.get(robotName), true);
	}
	
	public synchronized boolean canMove() {
		boolean flag = true;
		for (boolean move : readyToMove) {
			if (!move) {
				flag=false;
			}
		}
		return flag;
	}
	
	public void iMoved(){
		robotsMoved.getAndIncrement();
		if (robotsMoved.get() == readyToMove.size()) {
			time.getAndIncrement();
			for (int i = 0; i<readyToMove.size(); i++) {
				readyToMove.set(i, false);
			}
		}
	}
	
	public int getTime() {
		return time.get();
	}
}