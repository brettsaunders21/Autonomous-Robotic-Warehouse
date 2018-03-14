package job;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainingMain {

	public static void main(String[] args) {
        
		JobInput jb = new JobInput();
		
		HashMap<Integer, ArrayList<String>> availableOrders = jb.getTrainingJobs();
		HashMap<String, ArrayList<Float>> itemRewardsWeights = jb.getItemRewardsWeights();
		ArrayList<String> itemNamesList = jb.itemNames();
		
		TrainingAlgorithm tA = new TrainingAlgorithm(itemNamesList,availableOrders);
		
		HashMap<String, Double> betaValuesFromTrainingSet = jb.getBetaValues();
		TrainingAlgorithm tA1 = new TrainingAlgorithm(itemNamesList,availableOrders, betaValuesFromTrainingSet);
		
		
		tA.train();
		
	}

}