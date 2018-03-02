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

	private TrainingAlgorithm tA = new TrainingAlgorithm(availableOrders, itemRewardsWeights);

	private HashMap<String, Double> betaValuesFromTrainingSet = tA.train();

	public ArrayList<Integer> prioritize() {

		Set<Integer> notOrderedOrdersSet = availableOrders.keySet();
		ArrayList<Integer> notOrderedOrdersList = new ArrayList<Integer>(notOrderedOrdersSet);
		// This loop will go through all the available orders and it will predict if the
		// order will be cancelled or not , it will also assign reward/weight ratio to
		// each order
		for (int i = 0; i < notOrderedOrdersList.size(); i++) {
			int order = notOrderedOrdersList.get(i);
			ArrayList<String> itemsAndQty = availableOrders.get(order);
			log4j.debug("Available orders " + itemsAndQty.toString());
			// ===========Prediction=========================
			float totalReward = 0;
			float totalWeight = 0;

			int qtyA;
			int qtyB;
			int qtyC;
			int qtyD;
			int qtyE;
			int qtyF;
			int qtyG;
			int qtyH;
			int qtyI;
			int qtyJ;
			int qtyK;
			int qtyL;
			int qtyM;
			int qtyN;
			int qtyO;
			int qtyP;
			int qtyQ;
			int qtyR;
			int qtyS;
			int qtyT;
			int qtyU;
			int qtyV;
			int qtyW;
			int qtyX;
			int qtyY;
			int qtyZ;

			if (itemsAndQty.contains("a")) {
				int position = itemsAndQty.indexOf("a");
				qtyA = Integer.parseInt(itemsAndQty.get(position + 1)); // Quantity of each item is always after the
																		// letter
				totalReward += qtyA * itemRewardsWeights.get("a").get(0);
				totalWeight += qtyA * itemRewardsWeights.get("a").get(1);

			} else {
				qtyA = 0;
			}
			if (itemsAndQty.contains("b")) {
				int position = itemsAndQty.indexOf("b");
				qtyB = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyB * itemRewardsWeights.get("b").get(0);
				totalWeight += qtyB * itemRewardsWeights.get("b").get(1);

			} else {
				qtyB = 0;

			}
			if (itemsAndQty.contains("c")) {
				int position = itemsAndQty.indexOf("c");
				qtyC = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyC * itemRewardsWeights.get("c").get(0);
				totalWeight += qtyC * itemRewardsWeights.get("c").get(1);

			} else {
				qtyC = 0;

			}
			if (itemsAndQty.contains("d")) {
				int position = itemsAndQty.indexOf("d");
				qtyD = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyD * itemRewardsWeights.get("d").get(0);
				totalWeight += qtyD * itemRewardsWeights.get("d").get(1);

			} else {
				qtyD = 0;

			}
			if (itemsAndQty.contains("e")) {
				int position = itemsAndQty.indexOf("e");
				qtyE = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyE * itemRewardsWeights.get("e").get(0);
				totalWeight += qtyE * itemRewardsWeights.get("e").get(1);

			} else {
				qtyE = 0;

			}
			if (itemsAndQty.contains("f")) {
				int position = itemsAndQty.indexOf("f");
				qtyF = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyF * itemRewardsWeights.get("f").get(0);
				totalWeight += qtyF * itemRewardsWeights.get("f").get(1);

			} else {
				qtyF = 0;

			}
			if (itemsAndQty.contains("g")) {
				int position = itemsAndQty.indexOf("g");
				qtyG = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyG * itemRewardsWeights.get("g").get(0);
				totalWeight += qtyG * itemRewardsWeights.get("g").get(1);

			} else {
				qtyG = 0;

			}
			if (itemsAndQty.contains("h")) {
				int position = itemsAndQty.indexOf("h");
				qtyH = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyH * itemRewardsWeights.get("h").get(0);
				totalWeight += qtyH * itemRewardsWeights.get("h").get(1);

			} else {
				qtyH = 0;

			}
			if (itemsAndQty.contains("i")) {
				int position = itemsAndQty.indexOf("i");
				qtyI = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyI * itemRewardsWeights.get("i").get(0);
				totalWeight += qtyI * itemRewardsWeights.get("i").get(1);

			} else {
				qtyI = 0;

			}
			if (itemsAndQty.contains("j")) {
				int position = itemsAndQty.indexOf("j");
				qtyJ = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyJ * itemRewardsWeights.get("j").get(0);
				totalWeight += qtyJ * itemRewardsWeights.get("j").get(1);

			} else {
				qtyJ = 0;

			}
			if (itemsAndQty.contains("k")) {
				int position = itemsAndQty.indexOf("k");
				qtyK = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyK * itemRewardsWeights.get("k").get(0);
				totalWeight += qtyK * itemRewardsWeights.get("k").get(1);

			} else {
				qtyK = 0;

			}
			if (itemsAndQty.contains("l")) {
				int position = itemsAndQty.indexOf("l");
				qtyL = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyL * itemRewardsWeights.get("l").get(0);
				totalWeight += qtyL * itemRewardsWeights.get("l").get(1);

			} else {
				qtyL = 0;

			}
			if (itemsAndQty.contains("m")) {
				int position = itemsAndQty.indexOf("m");
				qtyM = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyM * itemRewardsWeights.get("m").get(0);
				totalWeight += qtyM * itemRewardsWeights.get("m").get(1);

			} else {
				qtyM = 0;

			}
			if (itemsAndQty.contains("n")) {
				int position = itemsAndQty.indexOf("n");
				qtyN = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyN * itemRewardsWeights.get("n").get(0);
				totalWeight += qtyN * itemRewardsWeights.get("n").get(1);

			} else {
				qtyN = 0;

			}
			if (itemsAndQty.contains("o")) {
				int position = itemsAndQty.indexOf("o");
				qtyO = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyO * itemRewardsWeights.get("o").get(0);
				totalWeight += qtyO * itemRewardsWeights.get("o").get(1);

			} else {
				qtyO = 0;

			}
			if (itemsAndQty.contains("p")) {
				int position = itemsAndQty.indexOf("p");
				qtyP = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyP * itemRewardsWeights.get("p").get(0);
				totalWeight += qtyP * itemRewardsWeights.get("p").get(1);

			} else {
				qtyP = 0;

			}
			if (itemsAndQty.contains("q")) {
				int position = itemsAndQty.indexOf("q");
				qtyQ = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyQ * itemRewardsWeights.get("q").get(0);
				totalWeight += qtyQ * itemRewardsWeights.get("q").get(1);

			} else {
				qtyQ = 0;

			}
			if (itemsAndQty.contains("r")) {
				int position = itemsAndQty.indexOf("r");
				qtyR = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyR * itemRewardsWeights.get("r").get(0);
				totalWeight += qtyR * itemRewardsWeights.get("r").get(1);

			} else {
				qtyR = 0;

			}
			if (itemsAndQty.contains("s")) {
				int position = itemsAndQty.indexOf("s");
				qtyS = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyS * itemRewardsWeights.get("s").get(0);
				totalWeight += qtyS * itemRewardsWeights.get("s").get(1);

			} else {
				qtyS = 0;

			}
			if (itemsAndQty.contains("t")) {
				int position = itemsAndQty.indexOf("t");
				qtyT = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyT * itemRewardsWeights.get("t").get(0);
				totalWeight += qtyT * itemRewardsWeights.get("t").get(1);

			} else {
				qtyT = 0;

			}
			if (itemsAndQty.contains("u")) {
				int position = itemsAndQty.indexOf("u");
				qtyU = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyU * itemRewardsWeights.get("u").get(0);
				totalWeight += qtyU * itemRewardsWeights.get("u").get(1);

			} else {
				qtyU = 0;

			}
			if (itemsAndQty.contains("v")) {
				int position = itemsAndQty.indexOf("v");
				qtyV = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyV * itemRewardsWeights.get("v").get(0);
				totalWeight += qtyV * itemRewardsWeights.get("v").get(1);

			} else {
				qtyV = 0;

			}
			if (itemsAndQty.contains("w")) {
				int position = itemsAndQty.indexOf("w");
				qtyW = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyW * itemRewardsWeights.get("w").get(0);
				totalWeight += qtyW * itemRewardsWeights.get("w").get(1);

			} else {
				qtyW = 0;

			}
			if (itemsAndQty.contains("x")) {
				int position = itemsAndQty.indexOf("x");
				qtyX = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyX * itemRewardsWeights.get("x").get(0);
				totalWeight += qtyX * itemRewardsWeights.get("x").get(1);

			} else {
				qtyX = 0;

			}
			if (itemsAndQty.contains("y")) {
				int position = itemsAndQty.indexOf("y");
				qtyY = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyY * itemRewardsWeights.get("y").get(0);
				totalWeight += qtyY * itemRewardsWeights.get("y").get(1);

			} else {
				qtyY = 0;

			}
			if (itemsAndQty.contains("z")) {
				int position = itemsAndQty.indexOf("z");
				qtyZ = Integer.parseInt(itemsAndQty.get(position + 1));
				totalReward += qtyZ * itemRewardsWeights.get("z").get(0);
				totalWeight += qtyZ * itemRewardsWeights.get("z").get(1);

			} else {
				qtyZ = 0;

			}
			// Sigmoid function to predict the outcome with current beta values (Logistic
			// Regression)
			double prediction = 1 / (1 + Math.pow(Math.E,
					(betaValuesFromTrainingSet.get("bias") + betaValuesFromTrainingSet.get("a") * qtyA
							+ betaValuesFromTrainingSet.get("b") * qtyB + betaValuesFromTrainingSet.get("c") * qtyC
							+ betaValuesFromTrainingSet.get("d") * qtyD + betaValuesFromTrainingSet.get("e") * qtyE
							+ betaValuesFromTrainingSet.get("f") * qtyF + betaValuesFromTrainingSet.get("g") * qtyG
							+ betaValuesFromTrainingSet.get("h") * qtyH + betaValuesFromTrainingSet.get("i") * qtyI
							+ betaValuesFromTrainingSet.get("j") * qtyJ + betaValuesFromTrainingSet.get("k") * qtyK
							+ betaValuesFromTrainingSet.get("l") * qtyL + betaValuesFromTrainingSet.get("m") * qtyM
							+ betaValuesFromTrainingSet.get("n") * qtyN + betaValuesFromTrainingSet.get("o") * qtyO
							+ betaValuesFromTrainingSet.get("p") * qtyP + betaValuesFromTrainingSet.get("q") * qtyQ
							+ betaValuesFromTrainingSet.get("r") * qtyR + betaValuesFromTrainingSet.get("s") * qtyS
							+ betaValuesFromTrainingSet.get("t") * qtyT + betaValuesFromTrainingSet.get("u") * qtyU
							+ betaValuesFromTrainingSet.get("v") * qtyV + betaValuesFromTrainingSet.get("w") * qtyW
							+ betaValuesFromTrainingSet.get("x") * qtyX + betaValuesFromTrainingSet.get("y") * qtyY
							+ betaValuesFromTrainingSet.get("z") * qtyZ
							+ betaValuesFromTrainingSet.get("totalReward") * totalReward
							+ betaValuesFromTrainingSet.get("totalWeight") * totalWeight)));
			
			log4j.debug("Prediction " + prediction);

			if (prediction < 0.5) {
				prediction = 0; // not cancelled
				ordersNotToBeCancelled.add(order);
				orderRewardsRatio.put((totalReward / totalWeight), order);// I will only add the ratio to orders which are predicted not to be cancelled

			} else {
				prediction = 1; // cancelled
				ordersToBeCancelled.add(order);
			}
			// ===================Ratio Calculation=========================
			orderRewardsRatio.put((totalReward / totalWeight), order);

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
	    //Inserting the orders which are predicted not to be cancelled into the arraylist , highest priority order first
	    for (int i = 0; i < n; i++) {
	    	orderedOrders.add(orderRewardsRatio.get(ratiosList[i]));
	    	
	    }

		
	    log4j.debug("Ordered orders " + orderedOrders.toString());
		return orderedOrders;
	}

}
