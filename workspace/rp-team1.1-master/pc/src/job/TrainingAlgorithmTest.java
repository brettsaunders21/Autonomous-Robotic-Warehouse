package job;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainingAlgorithmTest {

	@Test
	public void testTraining() {
		JobInput jI = new JobInput();
		HashMap<Integer, ArrayList<String>> availableOrders = jI.getTrainingJobs();
		ArrayList<String> itemNamesList = jI.itemNames();
		TrainingAlgorithm tA = new TrainingAlgorithm(itemNamesList,availableOrders);
		tA.train();
		assertEquals(0.14262465, jI.getBetaValues().get("aa"), 0.05);
	}

}
