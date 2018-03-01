import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*This algorithm is based on Logistic Regression using Stochastic Gradient Descent to adjust the beta values */

public class TrainingAlgorithm {

	// Beta values for each item will be stored here
	private HashMap<String, Double> betaValuesFromTrainingSet = new HashMap<>();

	private HashMap<Integer, ArrayList<String>> availableOrders;
	private HashMap<String, ArrayList<Float>> itemRewardsWeights;

	// File with past cancellation , this file + hashmap obtained from file with
	// available orders will be used to train
	private String cancelDataFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator
			+ "Desktop" + File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs"
			+ File.separator + "csv" + File.separator + "orderCancellation.csv";

	private String line = "";
	private String csvSplitBy = ",";

	public TrainingAlgorithm(HashMap<Integer, ArrayList<String>> availableOrders,
			HashMap<String, ArrayList<Float>> itemRewardsWeights) {
		this.availableOrders = availableOrders;
		this.itemRewardsWeights = itemRewardsWeights;
	}

	/* ============== TRAINING =========== */
	public HashMap<String, Double> train() {

		// All beta values are initialised to zero
		betaValuesFromTrainingSet.put("bias", 0d);
		betaValuesFromTrainingSet.put("totalReward", 0d);
		betaValuesFromTrainingSet.put("totalWeight", 0d);
		betaValuesFromTrainingSet.put("a", 0d);
		betaValuesFromTrainingSet.put("b", 0d);
		betaValuesFromTrainingSet.put("c", 0d);
		betaValuesFromTrainingSet.put("d", 0d);
		betaValuesFromTrainingSet.put("e", 0d);
		betaValuesFromTrainingSet.put("f", 0d);
		betaValuesFromTrainingSet.put("g", 0d);
		betaValuesFromTrainingSet.put("h", 0d);
		betaValuesFromTrainingSet.put("i", 0d);
		betaValuesFromTrainingSet.put("j", 0d);
		betaValuesFromTrainingSet.put("k", 0d);
		betaValuesFromTrainingSet.put("l", 0d);
		betaValuesFromTrainingSet.put("m", 0d);
		betaValuesFromTrainingSet.put("n", 0d);
		betaValuesFromTrainingSet.put("o", 0d);
		betaValuesFromTrainingSet.put("p", 0d);
		betaValuesFromTrainingSet.put("q", 0d);
		betaValuesFromTrainingSet.put("r", 0d);
		betaValuesFromTrainingSet.put("s", 0d);
		betaValuesFromTrainingSet.put("t", 0d);
		betaValuesFromTrainingSet.put("u", 0d);
		betaValuesFromTrainingSet.put("v", 0d);
		betaValuesFromTrainingSet.put("w", 0d);
		betaValuesFromTrainingSet.put("x", 0d);
		betaValuesFromTrainingSet.put("y", 0d);
		betaValuesFromTrainingSet.put("z", 0d);

		// These loop will be repeated for each order in the file with past
		// cancellations
		try (BufferedReader br = new BufferedReader(new FileReader(cancelDataFile))) {

			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvSplitBy);
				int order = Integer.parseInt(data[0]);
				int cancelledOrNot = Integer.parseInt(data[1]);
				ArrayList<String> itemsAndQty = availableOrders.get(order);

				float totalReward = 0;
				float totalWeight = 0;

				double totalRewardBeta;
				double totalWeightBeta;
				double valA;
				int qtyA;
				double valB;
				int qtyB;
				double valC;
				int qtyC;
				double valD;
				int qtyD;
				double valE;
				int qtyE;
				double valF;
				int qtyF;
				double valG;
				int qtyG;
				double valH;
				int qtyH;
				double valI;
				int qtyI;
				double valJ;
				int qtyJ;
				double valK;
				int qtyK;
				double valL;
				int qtyL;
				double valM;
				int qtyM;
				double valN;
				int qtyN;
				double valO;
				int qtyO;
				double valP;
				int qtyP;
				double valQ;
				int qtyQ;
				double valR;
				int qtyR;
				double valS;
				int qtyS;
				double valT;
				int qtyT;
				double valU;
				int qtyU;
				double valV;
				int qtyV;
				double valW;
				int qtyW;
				double valX;
				int qtyX;
				double valY;
				int qtyY;
				double valZ;
				int qtyZ;

				double alpha = 0.3f; // learning rate

				if (itemsAndQty.contains("a")) {
					int position = itemsAndQty.indexOf("a");
					qtyA = Integer.parseInt(itemsAndQty.get(position + 1)); // Quantity of each item is always after the
																			// letter
					totalReward += qtyA * itemRewardsWeights.get("a").get(0);
					totalWeight += qtyA * itemRewardsWeights.get("a").get(1);
					/*
					 * System.out.println(totalReward); System.out.println(totalWeight);
					 */

				} else {
					qtyA = 0;
				}
				if (itemsAndQty.contains("b")) {
					int position = itemsAndQty.indexOf("b");
					qtyB = Integer.parseInt(itemsAndQty.get(position + 1));
					totalReward += qtyB * itemRewardsWeights.get("b").get(0);
					totalWeight += qtyB * itemRewardsWeights.get("b").get(1);
					/*
					 * System.out.println(totalReward); System.out.println(totalWeight);
					 */

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

				if (itemsAndQty.contains("a")) {
					valA = betaValuesFromTrainingSet.get("a")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyA;
					betaValuesFromTrainingSet.put("a", valA);

				}
				if (itemsAndQty.contains("b")) {
					valB = betaValuesFromTrainingSet.get("b")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyB;
					betaValuesFromTrainingSet.put("b", valB);

				}
				if (itemsAndQty.contains("c")) {
					valC = betaValuesFromTrainingSet.get("c")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyC;
					betaValuesFromTrainingSet.put("c", valC);

				}
				if (itemsAndQty.contains("d")) {
					valD = betaValuesFromTrainingSet.get("d")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyD;
					betaValuesFromTrainingSet.put("d", valD);
				}
				if (itemsAndQty.contains("e")) {
					valE = betaValuesFromTrainingSet.get("e")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyE;
					betaValuesFromTrainingSet.put("e", valE);
				}
				if (itemsAndQty.contains("f")) {
					valF = betaValuesFromTrainingSet.get("f")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyF;
					betaValuesFromTrainingSet.put("f", valF);
				}
				if (itemsAndQty.contains("g")) {
					valG = betaValuesFromTrainingSet.get("g")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyG;
					betaValuesFromTrainingSet.put("g", valG);
				}
				if (itemsAndQty.contains("h")) {
					valH = betaValuesFromTrainingSet.get("h")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyH;
					betaValuesFromTrainingSet.put("h", valH);
				}
				if (itemsAndQty.contains("i")) {
					valI = betaValuesFromTrainingSet.get("i")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyI;
					betaValuesFromTrainingSet.put("i", valI);
				}
				if (itemsAndQty.contains("j")) {
					valJ = betaValuesFromTrainingSet.get("j")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyJ;
					betaValuesFromTrainingSet.put("j", valJ);
				}
				if (itemsAndQty.contains("k")) {
					valK = betaValuesFromTrainingSet.get("k")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyK;
					betaValuesFromTrainingSet.put("k", valK);

				}
				if (itemsAndQty.contains("l")) {
					valL = betaValuesFromTrainingSet.get("l")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyL;
					betaValuesFromTrainingSet.put("l", valL);
				}
				if (itemsAndQty.contains("m")) {
					valM = betaValuesFromTrainingSet.get("m")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyM;
					betaValuesFromTrainingSet.put("m", valM);
				}
				if (itemsAndQty.contains("n")) {
					valN = betaValuesFromTrainingSet.get("n")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyN;
					betaValuesFromTrainingSet.put("n", valN);
				}
				if (itemsAndQty.contains("o")) {
					valO = betaValuesFromTrainingSet.get("o")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyO;
					betaValuesFromTrainingSet.put("o", valO);
				}
				if (itemsAndQty.contains("p")) {
					valP = betaValuesFromTrainingSet.get("p")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyP;
					betaValuesFromTrainingSet.put("p", valP);

				}
				if (itemsAndQty.contains("q")) {
					valQ = betaValuesFromTrainingSet.get("q")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyQ;
					betaValuesFromTrainingSet.put("q", valQ);
				}
				if (itemsAndQty.contains("r")) {
					valR = betaValuesFromTrainingSet.get("r")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyR;
					betaValuesFromTrainingSet.put("r", valR);
				}
				if (itemsAndQty.contains("s")) {
					valS = betaValuesFromTrainingSet.get("s")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyS;
					betaValuesFromTrainingSet.put("s", valS);
				}
				if (itemsAndQty.contains("t")) {
					valT = betaValuesFromTrainingSet.get("t")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyT;
					betaValuesFromTrainingSet.put("t", valT);

				}
				if (itemsAndQty.contains("u")) {
					valU = betaValuesFromTrainingSet.get("u")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyU;
					betaValuesFromTrainingSet.put("u", valU);
				}
				if (itemsAndQty.contains("v")) {
					valV = betaValuesFromTrainingSet.get("v")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyV;
					betaValuesFromTrainingSet.put("v", valV);
				}
				if (itemsAndQty.contains("w")) {
					valW = betaValuesFromTrainingSet.get("w")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyW;
					betaValuesFromTrainingSet.put("w", valW);

				}
				if (itemsAndQty.contains("x")) {
					valX = betaValuesFromTrainingSet.get("x")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyX;
					betaValuesFromTrainingSet.put("x", valX);

				}
				if (itemsAndQty.contains("y")) {
					valY = betaValuesFromTrainingSet.get("y")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyY;
					betaValuesFromTrainingSet.put("y", valY);
				}
				if (itemsAndQty.contains("z")) {
					valZ = betaValuesFromTrainingSet.get("z")
							+ alpha * (cancelledOrNot - prediction) * prediction * (1 - prediction) * qtyZ;
					betaValuesFromTrainingSet.put("z", valZ);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found");
		}

		return betaValuesFromTrainingSet;
	}

}
