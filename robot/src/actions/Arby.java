package actions;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Arby extends Arbitrator{

	public Arby(Behavior[] behaviorList) {
		super(behaviorList);
	}
	
	public void stop() {
		return;
	}

}
