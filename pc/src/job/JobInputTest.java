package job;


import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

public class JobInputTest {

	@Test
	public void test() {
		JobInput jI = new JobInput();

		HashMap<String, ArrayList<Float>> itemRewardsWeights = jI.getItemRewardsWeights();
		assertEquals(true, itemRewardsWeights.containsKey("aa"));

		assertEquals(11.1f, itemRewardsWeights.get("aa").get(0), 0.0f);
		assertEquals(1.23f, itemRewardsWeights.get("aa").get(1), 0.0f);

		HashMap<String, ArrayList<Float>> itemLocations = jI.getItemLocations();
		assertEquals(true, itemLocations.containsKey("aa"));

		assertEquals(2, itemLocations.get("aa").get(0), 0);
		assertEquals(1, itemLocations.get("aa").get(1), 0);

		HashMap<Integer, ArrayList<String>> availableOrders = jI.getAvailableOrders();
		assertEquals(true, availableOrders.containsKey(10000));

		assertEquals("fc", availableOrders.get(10000).get(0));
		assertEquals(2, Integer.parseInt(availableOrders.get(10000).get(1)), 0);
		assertEquals("ge", availableOrders.get(10000).get(2));
		assertEquals(3, Integer.parseInt(availableOrders.get(10000).get(3)), 0);
		assertEquals("dc", availableOrders.get(10000).get(4));
		assertEquals(1, Integer.parseInt(availableOrders.get(10000).get(5)), 0);
		assertEquals("ej", availableOrders.get(10000).get(6));
		assertEquals(1, Integer.parseInt(availableOrders.get(10000).get(7)), 0);

		HashMap<String, ArrayList<Integer>> drops = jI.getDrops();
		assertEquals(true, drops.containsKey("drop1"));
		
		assertEquals(4, drops.get("drop1").get(0), 0);
		assertEquals(7, drops.get("drop1").get(1), 0);
		
		
		
		HashMap<String, Double> betaValuesFromTraining = jI.getBetaValues();
		assertEquals(true, betaValuesFromTraining.containsKey("aa"));
		
		HashMap<Integer, ArrayList<String>> trainingJobs = jI.getTrainingJobs();
		assertEquals(true, trainingJobs.containsKey(10100));
		
		ArrayList<String> itemNamesList = jI.itemNames();
		assertEquals("aa", itemNamesList.get(0));
		
	}

}
