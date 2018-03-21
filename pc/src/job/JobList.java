package job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*All jobs will be stored in this list and accessed by all classes*/

public class JobList {
	private JobSelection jS;
	private List<Job> jobsTracking;

	public JobList(JobSelection _jS) {
		jS = _jS;
		jobsTracking = Collections.synchronizedList(new ArrayList<Job>(jS.prioritize()));
	}


	public synchronized List<Job> getJobList() {
		return jobsTracking;
	}
	
	public synchronized void remove(int jobID) {
		for (int i = 0; i < jobsTracking.size()-1; i++) {
			if (jobsTracking.get(i).getID() == jobID) {
				jobsTracking.remove(i);
			}
		}
	}

	public synchronized boolean cancelJob(int jobID) {
		for (Job job : jobsTracking) {
			if (job.getID() == jobID) {
				job.setCanceled(true);
				return true;
			}
		}
		return false;
	}

	public Job getJob(int id) {
		for (Job job : jobsTracking) {
			if (job.getID() == id) {
				return job;
			}
		}
		return null;
	}
}
