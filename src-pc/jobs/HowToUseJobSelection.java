import java.util.ArrayList;
import java.util.HashMap;

public class HowToUseJobSelection {

	public static void main(String[] args) {
		
		
		
		JobInput jb = new JobInput();
		
		HashMap<String, Double> betaValuesFromTrainingSet = jb.getBetaValues();
		
		JobSelection js = new JobSelection(betaValuesFromTrainingSet);
		//This will get you arraylist with orders which are predicted not to be cancelled , in descending order of priority(e.g. the highest priority order is the first item of the array)
		ArrayList<Integer> prioritizedOrders = js.prioritize();
		// I can't test if the array is actually correct because I don't have the example files but that shouldn't matter as the method will be used the same no matter what

		
		//NOTE - You still need to use JobInput to get the details of the order (e.g items it contains , location of items etc.) see HowToUseJobInput for example
	}

}
