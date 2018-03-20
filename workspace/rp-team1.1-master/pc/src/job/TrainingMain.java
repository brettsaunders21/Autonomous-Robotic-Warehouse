package job;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainingMain {

	public static void main(String[] args) {
        
		JobInput jb = new JobInput();
		
		HashMap<Integer, ArrayList<String>> availableOrders = jb.getTrainingJobs();
		jb.getItemRewardsWeights();
		ArrayList<String> itemNamesList = jb.itemNames();
		
		TrainingAlgorithm tA = new TrainingAlgorithm(itemNamesList,availableOrders);
		
		HashMap<String, Double> betaValuesFromTrainingSet = jb.getBetaValues();
		new TrainingAlgorithm(itemNamesList,availableOrders, betaValuesFromTrainingSet);
		
		
		tA.train();
		
	}

}