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
	private List<Boolean> moveable;
	
	public Counter(Robot[] ROBOTS) {
		time = new AtomicInteger(0);
		robotsMoved = new AtomicInteger(0);
		readyToMove = Collections.synchronizedList(new ArrayList<Boolean>());
		moveable = Collections.synchronizedList(new ArrayList<Boolean>());
		int i=0;
		for (Robot robot : ROBOTS) {
			readyToMove.add(i,false);
			moveable.add(i, true);
			nameIndex.put(robot.getRobotName(), i);
			i++;
		}
	}
	//sets the boolean as true if robot is ready to move 
	public synchronized void readyToMove(String robotName) {
		readyToMove.set(nameIndex.get(robotName), true);
	}
	
	//checks if the robot can move
	public synchronized boolean canMove() {
		boolean flag = true;
		for (int i = 0; i<readyToMove.size(); i++) {
			if (moveable.get(i)&&!readyToMove.get(i)) {
				flag=false;
			}
		}
		return flag;
	}
	
	
	public void iMoved(){
		robotsMoved.incrementAndGet();
		if (robotsMoved.get() == readyToMove.size()) {
			time.incrementAndGet();
			for (int i = 0; i<readyToMove.size(); i++) {
				readyToMove.set(i, false);
			}
			robotsMoved.set(0);
		}
	}
	
	public int getTime() {
		return time.get();
	}

	//sets whether the robot can move to false
	public synchronized void isNonMove(String robotName) {
		moveable.set(nameIndex.get(robotName), false);
	}
	
	//sets whether the robot can move to true
	public synchronized void isMoveable(String robotName) {
		moveable.set(nameIndex.get(robotName), true);
	}
}