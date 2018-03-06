package job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/*This algorithm is based on Logistic Regression using Stochastic Gradient Descent to adjust the beta values */
/*
 * @author Samuel Chorvat <sxc1101@student.bham.ac.uk>
 */
public class TrainingAlgorithm {

	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	// Beta values for each item will be stored here
	private HashMap<String, Double> betaValuesFromTrainingSet = new HashMap<>();
	private ArrayList<String> itemNamesList;

	private HashMap<Integer, ArrayList<String>> availableOrders = new HashMap<>();

	// File with past cancellation , this file + hashmap obtained from file with
	// available orders will be used to train
	private String cancelDataFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator
			+ "Desktop" + File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs"
			+ File.separator + "csv" + File.separator + "cancellations.csv";

	private String line = "";
	private String csvSplitBy = ",";

	// This constructor is called if you don't have any previous beta values
	public TrainingAlgorithm(ArrayList<String> itemN, HashMap<Integer, ArrayList<String>> availableOrders) {
		this.itemNamesList = itemN;
		this.availableOrders = availableOrders;
		// All beta values are initialised to zero
		for (int i = 0; i < itemNamesList.size(); i++) {
			betaValuesFromTrainingSet.put(itemNamesList.get(i), 0d);
		}
		betaValuesFromTrainingSet.put("bias", 0d);

	}

	// This constructor is used when you already have beta values from previous
	// training
	public TrainingAlgorithm(ArrayList<String> itemN, HashMap<Integer, ArrayList<String>> availableOrders,
			HashMap<String, Double> betaValuesFromTrainingSet) {
		this.itemNamesList = itemN;
		this.availableOrders = availableOrders;
		this.betaValuesFromTrainingSet = betaValuesFromTrainingSet;
	}

	/* ============== TRAINING =========== */
	public void train() {
		log4j.debug("beta START " + betaValuesFromTrainingSet.toString());
		// These loop will be repeated for each order in the file with past
		// cancellations
		int totalPredictions = 0;
		int correctPredictions = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(cancelDataFile))) {

			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvSplitBy);
				int order = Integer.parseInt(data[0]);
				int cancelledOrNot = Integer.parseInt(data[1]);
				log4j.debug("cancelledOrNot " + cancelledOrNot);
				log4j.debug("items " + availableOrders.get(order));
				ArrayList<String> itemsAndQty = availableOrders.get(order);

				log4j.debug("Items + qty " + itemsAndQty.toString());
				log4j.debug("beta " + betaValuesFromTrainingSet.toString());

				double alpha = 0.3; // learning rate

				double qtyTimesBetaTotal = 0;
				for (int i = 0; i < itemNamesList.size(); i++) {
					if (itemsAndQty.contains(itemNamesList.get(i))) {
						// Quantity of each item is always after the letter in the arraylist
						qtyTimesBetaTotal += (betaValuesFromTrainingSet.get(itemNamesList.get(i))
								* Integer.parseInt(itemsAndQty.get(itemsAndQty.indexOf(itemNamesList.get(i)) + 1)));
					}

				}

				// Sigmoid function to predict the outcome with current beta values (Logistic
				// Regression)
				double prediction = 1 / (1 + Math.exp(-(betaValuesFromTrainingSet.get("bias") + qtyTimesBetaTotal
				/*
				 * + betaValuesFromTrainingSet.get("totalReward") * totalReward +
				 * betaValuesFromTrainingSet.get("totalWeight") * totalWeight
				 */)));
				log4j.debug("Prediction " + prediction);

				if ((prediction < 0.5 && cancelledOrNot == 0) || (prediction > 0.5 && cancelledOrNot == 1)) {
					totalPredictions += 1;
					correctPredictions += 1;
				} else {
					totalPredictions += 1;
				}

				// Calculating new coeffs and updating the hashmap with them

				double bias = betaValuesFromTrainingSet.get("bias")
						+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * 1;
				betaValuesFromTrainingSet.put("bias", bias);

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
		log4j.debug("Total " + totalPredictions);
		log4j.debug("Correct " + correctPredictions);

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
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}