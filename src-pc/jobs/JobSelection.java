import java.util.ArrayList;
import java.util.HashMap;

public class JobSelection {
	
	JobInput jb = new JobInput();
	
	
	private ArrayList<Integer> orderedOrders;
	private HashMap<Integer, ArrayList<String>> availableOrders = jb.getAvailableOrders();
	private HashMap<String, ArrayList<Float>> itemRewardsWeights = jb.getItemRewardsWeights();
	
	private TrainingAlgorithm tA = new TrainingAlgorithm(availableOrders, itemRewardsWeights);
	
	private HashMap<String, Double> betaValuesFromTrainingSet = tA.train();
	
	public ArrayList<Integer> prioritize() {
		
		
		
		
		return orderedOrders;				
	}
	
	
	

}
