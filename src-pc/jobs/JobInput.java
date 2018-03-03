
//C:\Users\samko\Desktop\rp-team1.1\src-pc\jobs

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;


public class JobInput {
	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	private HashMap<String, ArrayList<Float>> itemRewardsWeights = new HashMap<>();
	private HashMap<String, ArrayList<Float>> itemLocations = new HashMap<>();
	private HashMap<Integer, ArrayList<String>> availableOrders = new HashMap<>();
	private HashMap<String, ArrayList<Integer>> drops = new HashMap<>();

	// Change this depending on your location of files 
	String itemRewardsWeightsFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator
			+ "Desktop" + File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs"
			+ File.separator + "csv" + File.separator + "items.csv";
	String itemLocationsFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator + "Desktop"
			+ File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs" + File.separator
			+ "csv" + File.separator + "locations.csv";
	String availableOrdersFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator + "Desktop"
			+ File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs" + File.separator
			+ "csv" + File.separator + "jobs.csv";
	String dropsFile = "C:" + File.separator + "Users" + File.separator + "samko" + File.separator + "Desktop"
			+ File.separator + "rp-team1.1" + File.separator + "src-pc" + File.separator + "jobs" + File.separator
			+ "csv" + File.separator + "drops.csv";

	private String line = "";
	private String csvSplitBy = ",";

	public HashMap<String, ArrayList<Float>> getItemRewardsWeights() {

		try (BufferedReader br = new BufferedReader(new FileReader(itemRewardsWeightsFile))) {

			while ((line = br.readLine()) != null) {

				String[] data = line.split(csvSplitBy);
				ArrayList<Float> rewardWeight = new ArrayList<Float>();

				rewardWeight.add(Float.parseFloat(data[1]));
				rewardWeight.add(Float.parseFloat(data[2]));

				itemRewardsWeights.put(data[0], rewardWeight);

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: File not found");
		}
		log4j.debug("Reward, weight : " + itemRewardsWeights.get("aa").toString());
		return itemRewardsWeights;

	}

	public HashMap<String, ArrayList<Float>> getItemLocations() {
		try (BufferedReader br = new BufferedReader(new FileReader(itemLocationsFile))) {

			while ((line = br.readLine()) != null) {

				String[] data = line.split(csvSplitBy);
				ArrayList<Float> xy = new ArrayList<Float>();

				xy.add(Float.parseFloat(data[0]));
				xy.add(Float.parseFloat(data[1]));

				itemLocations.put(data[2], xy);

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: File not found");
		}
        log4j.debug("X cord,Ycord : " + itemLocations.get("aa").toString());
		return itemLocations;

	}

	public HashMap<Integer, ArrayList<String>> getAvailableOrders() {
		try (BufferedReader br = new BufferedReader(new FileReader(availableOrdersFile))) {

			while ((line = br.readLine()) != null) {

				ArrayList<String> itemQty = new ArrayList<String>();
				String[] data = line.split(csvSplitBy);
				int dataSize = data.length;
				for (int i = 1; i < dataSize; i++) {
					itemQty.add(data[i]);
				}

				availableOrders.put(Integer.parseInt(data[0]), itemQty);

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: File not found");
		}
		log4j.debug("What items it contains + qty : " + availableOrders.get(10000).toString());
		return availableOrders;

	}
	
	public HashMap<String , ArrayList<Integer>> getDrops() {
		try (BufferedReader br = new BufferedReader(new FileReader(dropsFile))) {
			int i = 1;
			while ((line = br.readLine()) != null) {
                
				String[] data = line.split(csvSplitBy);
				ArrayList<Integer> xy = new ArrayList<Integer>();

				xy.add(Integer.parseInt(data[0]));
				xy.add(Integer.parseInt(data[1]));
				
				drops.put("drop"+ i, xy);
				
				i += 1;

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: File not found");
		}
		log4j.debug("Drop point 1: " + drops.get("drop1").toString());
		log4j.debug("Drop point 2: " + drops.get("drop2").toString());
		return drops;
	}
	
	

}
