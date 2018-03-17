package job;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.plaf.synth.SynthInternalFrameUI;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import lejos.geom.Point;

/*
 * @author Samuel Chorvat <sxc1101@student.bham.ac.uk>
 */

public class JobInput {
	static Logger log4j = Logger.getLogger("jobs.JobSelection");

	private HashMap<String, ArrayList<Float>> itemRewardsWeights = new HashMap<>();
	private HashMap<String, ArrayList<Float>> itemLocations = new HashMap<>();
	private HashMap<Integer, ArrayList<String>> availableOrders = new HashMap<>();
	private HashMap<String, Double> betaValuesFromTraining = new HashMap<>();
	private HashMap<Integer, ArrayList<String>> trainingJobs = new HashMap<>();
	private ArrayList<Point> dropoffPoints = new ArrayList<>();
	



	// Change this depending on your file path
	String filePath = "/home/students/bxs714/Warehouse/rp-team1.1/pc/";
	String itemRewardsWeightsFile =  filePath + "src/job/csv/items.csv";
	String itemLocationsFile = filePath + "src/job/csv/locations.csv";
	String availableOrdersFile = filePath +  "src/job/csv/jobs.csv";
	String dropsFile = filePath +  "src/job/csv/drops.csv";
	String betaValuesFromTrainingFile = filePath + "src/job/csv/betaValuesFromTraining.csv";
	String trainingJobsFile = filePath +  "src/job/csv/training_jobs.csv";
	

	private String line = "";
	private String csvSplitBy = ",";



	public JobInput() {
		log4j.setLevel(Level.OFF);
	}
	
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
			System.out.println("File not found");
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
			log4j.debug("File not found");
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
			log4j.debug("File not found");
		}
		log4j.debug("What items it contains + qty : " + availableOrders.get(10000).toString());
		return availableOrders;

	}
	
	public ArrayList<Point> getDrops() {
		try (BufferedReader br = new BufferedReader(new FileReader(dropsFile))) {
			int i = 1;
			while ((line = br.readLine()) != null) {
                
				String[] data = line.split(csvSplitBy);
				Point dropOffPoint = new Point(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
				dropoffPoints.add(dropOffPoint);
				
				i += 1;

			}

		} catch (IOException e) {
			e.printStackTrace();
			log4j.debug("File not found");
		}
		log4j.debug("Drop point 1: " + dropoffPoints.get(0).toString());
		log4j.debug("Drop point 2: " + dropoffPoints.get(1).toString());
		return dropoffPoints;
	}
	
	
	
	public HashMap<String,Double> getBetaValues() {
		try (BufferedReader br = new BufferedReader(new FileReader(betaValuesFromTrainingFile))) {
			while ((line = br.readLine()) != null) {
                
				String[] data = line.split(csvSplitBy);

				
				
				betaValuesFromTraining.put(data[0],Double.parseDouble(data[1]));

			}

		} catch (IOException e) {
			e.printStackTrace();
			log4j.debug("File not found");
		}
		log4j.debug(betaValuesFromTraining.toString());
		return betaValuesFromTraining;
	}
	
	
	//This is method for TrainingAlg and JobSelect
	
	private ArrayList<String> itemNamesList = new ArrayList<String>();
	public ArrayList<String>  itemNames() {
		
		try (BufferedReader br = new BufferedReader(new FileReader(itemRewardsWeightsFile))) {

			while ((line = br.readLine()) != null) {
		    String[] data = line.split(csvSplitBy);
            itemNamesList.add(data[0]);

			}

		} catch (IOException e) {
			e.printStackTrace();
			log4j.debug("File not found");
		}
		log4j.debug(itemNamesList.toString());
		return itemNamesList;
	}
	
	public HashMap<Integer, ArrayList<String>> getTrainingJobs() {
		try (BufferedReader br = new BufferedReader(new FileReader(trainingJobsFile))) {

			while ((line = br.readLine()) != null) {

				ArrayList<String> itemQty = new ArrayList<String>();
				String[] data = line.split(csvSplitBy);
				int dataSize = data.length;
				for (int i = 1; i < dataSize; i++) {
					itemQty.add(data[i]);
				}

				trainingJobs.put(Integer.parseInt(data[0]), itemQty);

			}

		} catch (IOException e) {
			e.printStackTrace();
			log4j.debug("File not found");
		}
		log4j.debug("What items it contains + qty : " + trainingJobs.get(10100).toString());
		return trainingJobs;

	}
	
	
	

}