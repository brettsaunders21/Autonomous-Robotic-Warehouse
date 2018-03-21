package job;

import static org.junit.Assert.*;

import org.junit.Test;

import interfaces.Pose;
import interfaces.Robot;

import java.util.ArrayList;
import lejos.geom.Point;



public class JobSelectionTest {
	private float totalReward = 0;

	@Test
	public void testPrioritize() {
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		ArrayList<Job> orderedJobs = jS.prioritize();
		assertEquals(83, orderedJobs.size(), 0); //There should be only 83 jobs as the other 17 are predicted to be cancelled based on the beta values from training
		
	}
	

}
