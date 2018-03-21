package job;

import static org.junit.Assert.*;

import org.junit.Test;

import interfaces.Robot;

import java.util.ArrayList;
import lejos.geom.Point;



public class JobSelectionTest {

	private Robot robot1 = new Robot("Spike", "0016530AA681", new Point(0,0));
	private Robot robot2 = new Robot("Marco", "001653115A7E", new Point(11, 7));
	private Robot robot3 = new  Robot("Jeremy", "00165308E37C", new Point(0,7));
	private Robot[] robotList = {robot1,robot2,robot3};
	private JobInput jI = new JobInput();
	private TSP tsp = new TSP(jI.getDrops());
	
	@Test
	public void testPrioritize() {
		JobSelection jS = new JobSelection(jI.getBetaValues(), tsp);
		ArrayList<Job> orderedJobs = jS.prioritize();
		jS.calculateRatios(orderedJobs, robotList);
		assertEquals(83, orderedJobs.size(), 0); //There should be only 83 jobs as the other 17 are predicted to be cancelled based on the beta values from training
		
	}
	@Test
	public void testGetJob() {
		JobSelection jS = new JobSelection(jI.getBetaValues(), tsp);
		Point point = new Point(0,0);
		Robot robot = new Robot("test","test",point);
		Job job = jS.getJob(jS.prioritize(), robotList);
		assertEquals(true, jS.prioritize().contains(job));
	}

}
