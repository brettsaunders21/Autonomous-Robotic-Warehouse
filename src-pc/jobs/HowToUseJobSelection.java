import java.util.ArrayList;

public class HowToUseJobSelection {

	public static void main(String[] args) {
		
		JobSelection js = new JobSelection();
		//This will get you arraylist with orders which are predicted not to be cancelled , in descending order of priority(e.g. the highest priority order is the first item of the array)
		ArrayList<Integer> prioritizedOrders = js.prioritize();
		// I can't test if the array is actually correct because I don't have the example files but that shouldn't matter as the method will be used the same no matter what

	}

}
