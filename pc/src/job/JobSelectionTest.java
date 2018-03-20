package job;

import static org.junit.Assert.*;

import org.junit.Test;

import interfaces.Robot;

import java.util.ArrayList;
import lejos.geom.Point;



public class JobSelectionTest {

	@Test
	public void testPrioritize() {
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		ArrayList<Job> orderedJobs = jS.prioritize();
		assertEquals(83, orderedJobs.size(), 0); //There should be only 83 jobs as the other 17 are predicted to be cancelled based on the beta values from training
		
	}
	@Test
	public void testGetJob() {
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		Point point = new Point(0,0);
		Robot robot = new Robot("test","test",point);
		Job job = jS.getJob(jS.prioritize(), robot);
		assertEquals(true, jS.prioritize().contains(job));
	}

}
