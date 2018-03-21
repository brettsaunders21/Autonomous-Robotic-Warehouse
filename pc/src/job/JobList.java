package job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import interfaces.Robot;

/*All jobs will be stored in this list and accessed by all classes*/

public class JobList {
	private JobSelection jS;
	private List<Job> jobs;

	public JobList(JobSelection _jS) {
		jS = _jS;
		jobs = Collections.synchronizedList(new ArrayList<Job>(jS.prioritize()));
	}

	public synchronized Job getNewJob(Robot robot) {
		return jS.getJob(jobs, robot);
	}

	public synchronized void replaceJobList(List<Job> newList) {
		jobs = newList;
		return;
	}

	public synchronized List<Job> getJobList() {
		return jobs;
	}

	public synchronized boolean cancelJob(int jobID) {
		for (Job job : jobs) {
			if (job.getID() == jobID) {
				job.setCanceled(true);
				return true;
			}
		}
		return false;
	}

	public Job getJob(int id) {
		for (Job job : jobs) {
			if (job.getID() == id) {
				return job;
			}
		}
		return null;
	}
}
