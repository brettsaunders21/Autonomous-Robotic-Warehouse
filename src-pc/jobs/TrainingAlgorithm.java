import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/*This algorithm is based on Logistic Regression using Stochastic Gradient Descent to adjust the beta values */

public class TrainingAlgorithm {

	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	// Beta values for each item will be stored here
	private HashMap<String, Double> betaValuesFromTrainingSet = new HashMap<>();
	private ArrayList<String> itemNamesList;

	private HashMap<Integer, ArrayList<String>> availableOrders = new HashMap<>();
	private HashMap<String, ArrayList<Float>> itemRewardsWeights = new HashMap<>();

	// File with past cancellation , this file + hashmap obtained from file with
	// available orders will be used to train
	private String cancelDataFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator
			+ "Desktop" + File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs"
			+ File.separator + "csv" + File.separator + "cancellations.csv";

	private String line = "";
	private String csvSplitBy = ",";

	// This constructor is called if you don't have any previous beta values
	public TrainingAlgorithm(ArrayList<String> itemN, HashMap<Integer, ArrayList<String>> availableOrders,
			HashMap<String, ArrayList<Float>> itemRewardsWeights) {
		this.itemNamesList = itemN;
		this.availableOrders = availableOrders;
		this.itemRewardsWeights = itemRewardsWeights;
		// All beta values are initialised to zero
		for (int i = 0; i < itemNamesList.size(); i++) {
			betaValuesFromTrainingSet.put(itemNamesList.get(i), 0d);
		}
		betaValuesFromTrainingSet.put("bias", 0d);
		betaValuesFromTrainingSet.put("totalReward", 0d);
		betaValuesFromTrainingSet.put("totalWeight", 0d);

	}

	// This constructor is used when you already have beta values from previous
	// training
	public TrainingAlgorithm(ArrayList<String> itemN, HashMap<Integer, ArrayList<String>> availableOrders,
			HashMap<String, ArrayList<Float>> itemRewardsWeights, HashMap<String, Double> betaValuesFromTrainingSet) {
		this.itemNamesList = itemN;
		this.availableOrders = availableOrders;
		this.itemRewardsWeights = itemRewardsWeights;
		this.betaValuesFromTrainingSet = betaValuesFromTrainingSet;
	}

	/* ============== TRAINING =========== */
	public void train() {

		// These loop will be repeated for each order in the file with past
		// cancellations
		try (BufferedReader br = new BufferedReader(new FileReader(cancelDataFile))) {

			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvSplitBy);
				int order = Integer.parseInt(data[0]);
				int cancelledOrNot = Integer.parseInt(data[1]);
				log4j.debug("items " + availableOrders.get(order));
				ArrayList<String> itemsAndQty = availableOrders.get(order);

				log4j.debug("Items + qty " + itemsAndQty.toString());
				log4j.debug("beta " + betaValuesFromTrainingSet.toString());

				float totalReward = 0;
				float totalWeight = 0;

				double totalRewardBeta;
				double totalWeightBeta;

				double alpha = 0.3f; // learning rate

				double qtyTimesBetaTotal = 0;
				for (int i = 0; i < itemNamesList.size(); i++) {
					if (itemsAndQty.contains(itemNamesList.get(i))) {
						// Quantity of each item is always after the letter in the arraylist
						qtyTimesBetaTotal += (betaValuesFromTrainingSet.get(itemNamesList.get(i))
								* Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(i)) + 1)));
						totalReward += (Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(i)) + 1))
								* itemRewardsWeights.get(itemNamesList.get(i)).get(0));
						totalWeight += (Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(i)) + 1))
								* itemRewardsWeights.get(itemNamesList.get(i)).get(1));
					}

				}

				// Sigmoid function to predict the outcome with current beta values (Logistic
				// Regression)
				double prediction = 1 / (1 + Math.pow(Math.E,
						(betaValuesFromTrainingSet.get("bias") + qtyTimesBetaTotal
								+ betaValuesFromTrainingSet.get("totalReward") * totalReward
								+ betaValuesFromTrainingSet.get("totalWeight") * totalWeight)));
				log4j.debug("Prediction " + prediction);

				// Calculating new coeffs and updating the hashmap with them

				double bias = betaValuesFromTrainingSet.get("bias")
						+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * 1;
				betaValuesFromTrainingSet.put("bias", bias);

				totalRewardBeta = betaValuesFromTrainingSet.get("totalReward")
						+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * totalReward;
				betaValuesFromTrainingSet.put("totalReward", totalRewardBeta);

				totalWeightBeta = betaValuesFromTrainingSet.get("totalWeight")
						+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * totalWeight;
				betaValuesFromTrainingSet.put("totalWeight", totalWeightBeta);

				for (int i = 0; i < itemNamesList.size(); i++) {
					if (itemsAndQty.contains(itemNamesList.get(i))) {
						double oldBeta = betaValuesFromTrainingSet.get(itemNamesList.get(i));
						double newBeta = oldBeta + alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction)
								* Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(i)) + 1));
						betaValuesFromTrainingSet.put(itemNamesList.get(i), newBeta);

					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found");
		}
		log4j.debug("Beta values after training " + betaValuesFromTrainingSet.toString());

		try {
			FileWriter writer = new FileWriter("betaValuesFromTraining.csv");
			for (int i = 0; i < itemNamesList.size(); i++) {
				writer.append(itemNamesList.get(i));
				writer.append(',');
				writer.append(betaValuesFromTrainingSet.get(itemNamesList.get(i)).toString());
				writer.append('\n');
			}
			writer.append("bias");
			writer.append(',');
			writer.append(betaValuesFromTrainingSet.get("bias").toString());
			writer.append('\n');
			writer.append("totalReward");
			writer.append(',');
			writer.append(betaValuesFromTrainingSet.get("totalReward").toString());
			writer.append('\n');
			writer.append("totalWeight");
			writer.append(',');
			writer.append(betaValuesFromTrainingSet.get("totalWeight").toString());
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
