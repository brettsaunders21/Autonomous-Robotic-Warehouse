package main;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter extends Thread {
	 public static AtomicInteger counter = new AtomicInteger(0);

	public void run() {
		try {
			Thread.sleep(1000);
			counter.getAndIncrement();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getCounter() {
		return counter.get();
	}
}
