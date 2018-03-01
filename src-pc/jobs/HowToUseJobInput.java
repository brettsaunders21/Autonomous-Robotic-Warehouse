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
		
		System.out.println("Reward, weight : " + itemRewardsWeights.get("a").toString());
		System.out.println("Reward, weight : " + itemRewardsWeights.get("b").toString());
		
		itemLocations = jb.getItemLocations();
		
		System.out.println("X cord,Ycord : " + itemLocations.get("a").toString());
		System.out.println("X cord,Ycord  : " + itemLocations.get("b").toString());
		
		availableOrders = jb.getAvailableOrders();
		
		System.out.println("What items it contains + qty : " + availableOrders.get(1001).toString());
		System.out.println("What items it contains + qty : " + availableOrders.get(1002).toString());
		
		

	}

}
