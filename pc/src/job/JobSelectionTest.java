package job;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;



public class JobSelectionTest {

	@Test
	public void test() {
		JobInput jI = new JobInput();
		JobSelection jS = new JobSelection(jI.getBetaValues());
		ArrayList<Job> orderedJobs = jS.prioritize();
		assertEquals(83, orderedJobs.size(), 0); //There should be only 83 jobs as the other 17 are predicted to be cancelled based on the beta values from training
		
	}

}
