import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

public class JobSelection {
	
	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	JobInput jb = new JobInput();

	private ArrayList<Integer> orderedOrders;
	private HashMap<Integer, ArrayList<String>> availableOrders = jb.getAvailableOrders();
	private HashMap<String, ArrayList<Float>> itemRewardsWeights = jb.getItemRewardsWeights();
	private HashMap<Float, Integer> orderRewardsRatio;
	private ArrayList<Integer> ordersToBeCancelled; // Orders which will be predicted to be cancelled will be stored
													// here
	private ArrayList<Integer> ordersNotToBeCancelled; // Orders which will be predicted not to be cancelled will be
														// stored here
	private ArrayList<String> itemNamesList = jb.itemNames();

	private TrainingAlgorithm tA = new TrainingAlgorithm(itemNamesList, availableOrders, itemRewardsWeights);

	private HashMap<String, Double> betaValuesFromTrainingSet = tA.train();

	public ArrayList<Integer> prioritize() {

		Set<Integer> notOrderedOrdersSet = availableOrders.keySet();
		ArrayList<Integer> notOrderedOrdersList = new ArrayList<Integer>(notOrderedOrdersSet);
		// This loop will go through all the available orders and it will predict if the
		// order will be cancelled or not , it will also assign reward/weight ratio to orders which are predicted not to be cancelled
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
			double prediction = 1 / (1 + Math.pow(Math.E,
					(betaValuesFromTrainingSet.get("bias") + qtyTimesBetaTotal + betaValuesFromTrainingSet.get("totalReward") * totalReward
							+ betaValuesFromTrainingSet.get("totalWeight") * totalWeight)));
			
			log4j.debug("Prediction " + prediction);

			if (prediction < 0.5) {  // not cancelled
				ordersNotToBeCancelled.add(order);
				orderRewardsRatio.put((totalReward / totalWeight), order);// I will only add the ratio to orders which are predicted not to be cancelled

			} else { // cancelled
				ordersToBeCancelled.add(order);
			}

		}
		// Sorting orders that are not to be cancelled depending on their reward/weight
		// ratio 

		Set<Float> ratiosSet = orderRewardsRatio.keySet();
		//Convert set to array;
		Float[] ratiosList = ratiosSet.toArray(new Float[ratiosSet.size()]);
		
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
	    //Inserting the orders which are predicted not to be cancelled into the arrayList , highest priority order first
	    for (int i = 0; i < n; i++) {
	    	orderedOrders.add(orderRewardsRatio.get(ratiosList[i]));
	    	
	    }

		
	    log4j.debug("Ordered orders " + orderedOrders.toString());
		return orderedOrders;
	}

}
