package job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import lejos.geom.Point;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/*
 * @author Samuel Chorvat <sxc1101@student.bham.ac.uk>
 */

public class JobSelection {

	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	JobInput jb = new JobInput();

	private ArrayList<Integer> orderedOrders = new ArrayList<Integer>();
	private ArrayList<Job> orderedJobs = new ArrayList<Job>();
	private HashMap<Integer, ArrayList<String>> availableOrders = jb.getAvailableOrders();
	private HashMap<String, ArrayList<Float>> itemRewardsWeights = jb.getItemRewardsWeights();
	private HashMap<String, ArrayList<Float>> itemLocations = jb.getItemLocations();
	private HashMap<Float, Integer> orderRewardsRatio = new HashMap<Float, Integer>();
	private Set<Float> ratiosSet;
	private ArrayList<Integer> ordersToBeCancelled = new ArrayList<Integer>(); // Orders which will be predicted to be cancelled will be stored
													// here
	private ArrayList<Integer> ordersNotToBeCancelled = new ArrayList<Integer>(); // Orders which will be predicted not to be cancelled will be
														// stored here
	private ArrayList<String> itemNamesList = jb.itemNames();

	private HashMap<String, Double> betaValuesFromTrainingSet = new HashMap<>();

	public JobSelection(HashMap<String, Double> betaValues) {
		this.betaValuesFromTrainingSet = betaValues;
		log4j.setLevel(Level.OFF);

	}

	public ArrayList<Job> prioritize() {

		Set<Integer> notOrderedOrdersSet = availableOrders.keySet();
		ArrayList<Integer> notOrderedOrdersList = new ArrayList<Integer>(notOrderedOrdersSet);
		// This loop will go through all the available orders and it will predict if the
		// order will be cancelled or not , it will also assign reward/weight ratio to
		// orders which are predicted not to be cancelled
		for (int i = 0; i < notOrderedOrdersList.size(); i++) {
			int order = notOrderedOrdersList.get(i);
			ArrayList<String> itemsAndQty = availableOrders.get(order);
			log4j.debug("Available orders " + itemsAndQty.toString());
			// ===========Prediction=========================
			float totalReward = 0;
			float totalWeight = 0;

			double qtyTimesBetaTotal = 0;
			for (int j = 0; j < itemNamesList.size(); j++) {
				if (itemsAndQty.contains(itemNamesList.get(j))) {
					// Quantity of each item is always after the letter in the arraylist
					qtyTimesBetaTotal += (betaValuesFromTrainingSet.get(itemNamesList.get(j))
							* Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(j)) + 1)));
					totalReward += (Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(j)) + 1))
							* itemRewardsWeights.get(itemNamesList.get(j)).get(0));
					totalWeight += (Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(j)) + 1))
							* itemRewardsWeights.get(itemNamesList.get(j)).get(1));
				}

			}
			// Sigmoid function to predict the outcome with current beta values (Logistic
			// Regression)
			double prediction = 1 / (1 + Math.exp(-(betaValuesFromTrainingSet.get("bias") + qtyTimesBetaTotal)));
			log4j.debug("Prediction " + prediction);

			if (prediction < 0.5) { // not cancelled
				ordersNotToBeCancelled.add(order);
				orderRewardsRatio.put((totalReward / totalWeight), order);// I will only add the ratio to orders which
																			// are predicted not to be cancelled

			} else { // cancelled
				log4j.debug(order);
				log4j.debug(ordersToBeCancelled.toString());
				ordersToBeCancelled.add(order);
			}

		}
		log4j.debug("reward/weight map " + orderRewardsRatio.toString());
		// Sorting orders that are not to be cancelled depending on their reward/weight
		// ratio

		ratiosSet = orderRewardsRatio.keySet();
		log4j.debug("RatioSet " + ratiosSet);
		// Convert set to array;
		Float[] ratiosList = ratiosSet.toArray(new Float[ratiosSet.size()]);
		log4j.debug("Ratios " + Arrays.toString(ratiosList));

		int n = ratiosList.length;
		float temp = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {

				if (ratiosList[j - 1] < ratiosList[j]) {
					temp = ratiosList[j - 1];
					ratiosList[j - 1] = ratiosList[j];
					ratiosList[j] = temp;
				}

			}
		}
		// Inserting the orders which are predicted not to be cancelled into the
		// arrayList , highest priority order first
		for (int i = 0; i < n; i++) {
			orderedOrders.add(orderRewardsRatio.get(ratiosList[i]));

		}
		log4j.debug("Ordered orders " + orderedOrders.toString());
		log4j.debug("Ordered orders size " + orderedOrders.size());

		
		//Inserting into array of Jobs
		for (int i = 0; i < orderedOrders.size(); i++) {
			ArrayList<Item> items = new ArrayList<Item>();
			ArrayList<String> itemsAndQty = availableOrders.get(orderedOrders.get(i));
			for (int j = 0; j < itemsAndQty.size(); j = j + 2) {
				items.add(new Item(itemsAndQty.get(j), Integer.parseInt(itemsAndQty.get(j + 1)),
						itemRewardsWeights.get(itemsAndQty.get(j)).get(1),
						new Point(itemLocations.get(itemsAndQty.get(j)).get(0),
								itemLocations.get(itemsAndQty.get(j)).get(1)),
						itemRewardsWeights.get(itemsAndQty.get(j)).get(0)));

			}
			orderedJobs.add(new Job(orderedOrders.get(i), items));

		}
		log4j.debug("Ordered jobs " + orderedJobs.toString());
		return orderedJobs;
	}

}
