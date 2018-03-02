import java.util.ArrayList;
import java.util.HashMap;

public class HowToUseJobInput {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashMap<String, ArrayList<Float>> itemRewardsWeights = new HashMap<>();
		HashMap<String, ArrayList<Float>> itemLocations = new HashMap<>();
		HashMap<Integer, ArrayList<String>> availableOrders = new HashMap<>();
		
		
		JobInput jb = new JobInput();
		
		itemRewardsWeights = jb.getItemRewardsWeights();
		
		itemLocations = jb.getItemLocations();
		
		availableOrders = jb.getAvailableOrders();
		
		

	}

}
