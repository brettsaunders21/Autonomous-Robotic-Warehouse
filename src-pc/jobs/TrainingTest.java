import java.util.ArrayList;
import java.util.HashMap;

public class TrainingTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
		
		
		JobInput jb = new JobInput();
		
		HashMap<Integer, ArrayList<String>> availableOrders = jb.getAvailableOrders();
		HashMap<String, ArrayList<Float>> itemRewardsWeights = jb.getItemRewardsWeights();
		ArrayList<String> itemNamesList = jb.itemNames();
		
		TrainingAlgorithm tA = new TrainingAlgorithm(itemNamesList,availableOrders, itemRewardsWeights);
		
		HashMap<String, Double> betaValuesFromTrainingSet = new HashMap<>();
		
		betaValuesFromTrainingSet = tA.train();
		
		
		

	}

}
