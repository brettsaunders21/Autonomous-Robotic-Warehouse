package job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import interfaces.Robot;

/*All jobs will be stored in this list and accessed by all classes*/

public class JobList {
	private JobSelection jS;
	private List<Job> jobs;
	public List<Job> jobsCompleted;
	private ConcurrentHashMap<Integer, ArrayList<Robot>> jobInProgressMap;
	
	public JobList(JobSelection _jS, Robot[] robots){
		jS = _jS;
		jobs = Collections.synchronizedList(new ArrayList<Job>(jS.prioritize()));
		jobInProgressMap = new ConcurrentHashMap<Integer, ArrayList<Robot>>();
		jobsCompleted = Collections.synchronizedList(new ArrayList<>());
	}

	public synchronized Job getNewJob(Robot[] robots){
		return jS.getJob(jobs, robots);
	}
	
	public synchronized void replaceJobList(List<Job> newList){
		jobs = newList;
		return;
	}
	
	public synchronized List<Job> getJobList(){
		return jobs;
	}
	
	public synchronized boolean cancelJob(int jobID){
		for (Job job : jobs) {
			if (job.getID() == jobID) {
				job.setCanceled(true);
				return true;
			}
		}
		return false;
	}
	
	public Job getJob(int id){
		for (Job job : jobs) {
			if (job.getID() == id) {
				return job;
			}
		}
		return null;
	}
	
	public void addJobToProgressMap(Job j, Robot r) {
		ArrayList<Robot> robotMap = new ArrayList<>();
		robotMap.add(r);
		if (!jobInProgressMap.containsKey(j.getID())) {
			jobInProgressMap.put(j.getID(), robotMap);
		}
	}
	
	public void addRobotToJob(Job j, Robot r) {
		int jobID = j.getID();
		if (jobInProgressMap.containsKey(jobID)) {
			ArrayList<Robot> robotMap = jobInProgressMap.get(jobID);
			robotMap.add(r);
			jobInProgressMap.put(jobID, robotMap);
		}
	}
	
	public void removeRobotFromJob(Job j, Robot r) {
		int jobID = j.getID();
		if (jobInProgressMap.containsKey(jobID)) {
			ArrayList<Robot> robotMap = jobInProgressMap.get(jobID);
			robotMap.remove(r);
			jobInProgressMap.put(jobID, robotMap);
		}
		if (jobInProgressMap.get(jobID).isEmpty()) {
			jobsCompleted.add(j);
			jobInProgressMap.remove(j);
		}
	}
	
	public ArrayList<Job> getJobsCompleted() {	
		ArrayList<Job> jobsCompleteAL = new ArrayList<>(jobsCompleted);
		return jobsCompleteAL;
	}


}

