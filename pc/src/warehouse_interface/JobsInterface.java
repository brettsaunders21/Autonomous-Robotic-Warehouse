package warehouse_interface;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import interfaces.Robot;
import job.Item;
import job.Job;
import job.JobList;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class JobsInterface extends JFrame implements Runnable {

	private JPanel contentPane;
	private JPanel cancelJobPane;
	private JLabel totalReward;
	private JTextField textField;
	private Thread thread;
	private Robot[] robots;
	private List<Job> completedJobs;
	GridLayout mainLayout;
	GridLayout cancelJobLayout = new GridLayout(2, 0);
	String[][] completedJobsData = new String[20][4];
	String[][] itemsInformationData = new String[30][3];
	String[][] robotInfoData = new String[1][4];
	String[][] robot1Data = new String[1][5];
	String[][] robot2Data = new String[1][5];
	String[][] robot3Data = new String[1][5];
	private float allRewardSum;
	private JTable t;
	String itemsCsvFile = "src/job/csv/items.csv";
	JobList jobList;

	public JobsInterface(Robot[] robots, ArrayList<Job> completedJobs, JobList jobList) {
		mainLayout = new GridLayout((8 + robots.length * 2), 0);
		setFrame();
		setPanes();
		this.robots = robots;
		this.completedJobs = jobList.jobsCompleted;
		this.jobList = jobList;
		for (int i = 0; i < robots.length; i++) {
			currentInfo(robots[i].getRobotName(), i);
		}
		itemsInformation();
		completedJobs();
		totalReward();
		cancelJobs();
		thread = new Thread(this);
		thread.start();
	}

	// Table with information about each robot
	public void currentInfo(String robotName, int robotIndex) {
		JLabel label2 = new JLabel(robotName);
		add(label2);
		String[] fields = { "CurrentJobs", "Total Weight", "Total Reward", "Robot Coordinates",
				"All items in that job" };
		contentPane.setLayout(mainLayout);
		if (robotIndex == 0)
			t = new JTable(robot1Data, fields);
		else if (robotIndex == 1)
			t = new JTable(robot2Data, fields);
		else if (robotIndex == 2)
			t = new JTable(robot3Data, fields);
		else
			t = new JTable(robotInfoData, fields);
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);
	}

	// Table with all completed jobs
	public void completedJobs() {
		contentPane.setLayout(mainLayout);
		JLabel label = new JLabel("Completed jobs");
		add(label);
		String[] fields = { "Job ID", "Total Weight", "Total Reward", "All Items" };
		t = new JTable(completedJobsData, fields);
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}

	// Table with all items and information about each of them
	public void itemsInformation() {
		JLabel label2 = new JLabel("Items information");
		add(label2);
		String[] fields = { "Item Name", "Reward", "Weight" };
		t = new JTable(itemsInformationData, fields);
		String line = "";
		int lineNumber = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(itemsCsvFile));
			try {
				while ((line = br.readLine()) != null) {
					String[] items = line.split(",");
					itemsInformationData[lineNumber][0] = items[0];
					itemsInformationData[lineNumber][1] = items[1];
					itemsInformationData[lineNumber][2] = items[2];
					lineNumber++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}

	// Total reward from all robots
	public void totalReward() {
		totalReward = new JLabel("Total reward: " + Float.toString(allRewardSum));
		contentPane.add(totalReward);
	}

	// Button to cancel particular job
	public void cancelJobs() {
		cancelJobPane.setLayout(cancelJobLayout);
		JLabel label2 = new JLabel("Cancel job");
		add(label2);
		textField = new JTextField();
		textField.setText("Type your job ID here");
		JButton cancelButton = new JButton("Cancel job");
		cancelJobPane.add(textField);
		cancelJobPane.add(cancelButton);
		contentPane.add(cancelJobPane);
		cancelButton.addActionListener(new Action());
		textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				textField.setText("");
			}

			public void focusLost(FocusEvent e) {

			}
		});

	}

	// To set a frame
	public void setFrame() {
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.revalidate();
		setBounds(1200, 20, 700, 850);
		setResizable(true);
		setVisible(true);
	}

	// To set panes
	public void setPanes() {
		mainLayout.setVgap(-20);
		contentPane = new JPanel();
		cancelJobPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(-10, 5, 0, 5));
		setContentPane(contentPane);
	}

	// Method that updates information about every robot
	public void setRobotInfo(int indexOfRobot) {
		String allItems = "";
		try {
			if (indexOfRobot == 0) {
				robot1Data[0][0] = Integer.toString(robots[indexOfRobot].getActiveJob().getID());
				robot1Data[0][1] = Float.toString(robots[indexOfRobot].getActiveJob().getWEIGHT());
				robot1Data[0][2] = Float.toString(robots[indexOfRobot].getActiveJob().getREWARD());
				robot1Data[0][3] = "[" + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().x))
						+ ", " + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().y)) + "]";
				for (Item i : robots[indexOfRobot].getActiveJob().getITEMS())
					allItems += i.getID() + ",";
				robot1Data[0][4] = allItems.substring(0, allItems.length() - 1);
				allItems = "";
			} else if (indexOfRobot == 1) {
				robot2Data[0][0] = Integer.toString(robots[indexOfRobot].getActiveJob().getID());
				robot2Data[0][1] = Float.toString(robots[indexOfRobot].getActiveJob().getWEIGHT());
				robot2Data[0][2] = Float.toString(robots[indexOfRobot].getActiveJob().getREWARD());
				robot2Data[0][3] = "[" + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().x))
						+ ", " + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().y)) + "]";
				for (Item i : robots[indexOfRobot].getActiveJob().getITEMS())
					allItems += i.getID() + ",";
				robot2Data[0][4] = allItems.substring(0, allItems.length() - 1);
				allItems = "";
			} else if (indexOfRobot == 2) {
				robot3Data[0][0] = Integer.toString(robots[indexOfRobot].getActiveJob().getID());
				robot3Data[0][1] = Float.toString(robots[indexOfRobot].getActiveJob().getWEIGHT());
				robot3Data[0][2] = Float.toString(robots[indexOfRobot].getActiveJob().getREWARD());
				robot3Data[0][3] = "[" + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().x))
						+ ", " + Integer.toString(Math.round(robots[indexOfRobot].getCurrentPosition().y)) + "]";
				for (Item i : robots[indexOfRobot].getActiveJob().getITEMS())
					allItems += i.getID() + ",";
				robot3Data[0][4] = allItems.substring(0, allItems.length() - 1);
				allItems = "";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// Method that updates the information about completed jobs
	public void setCompletedJobsInfo() {
		completedJobs = jobList.getJobsCompleted();
		try {
			for (int i = 0; i < completedJobs.size(); i++) {
				completedJobsData[i][0] = Integer.toString(completedJobs.get(i).getID());
				completedJobsData[i][1] = Float.toString(completedJobs.get(i).getWEIGHT());
				completedJobsData[i][2] = Float.toString(completedJobs.get(i).getREWARD());
				String allItems = "";
				for (Item item : completedJobs.get(i).getITEMS()){
					allItems += item.getID();
					allItems += ",";
				}
				completedJobsData[i][3] = allItems.substring(0, allItems.length() - 1);
			}
		} catch (NullPointerException e) {
		}
	}

	// Method that updates total reward
	public void updateTotalReward() {
		for (int i = 0; i < robots.length; i++) {
			try {
				allRewardSum += robots[i].currentReward();
			} catch (NullPointerException e) {

			}
		}
		totalReward.setText("Total reward: " + Float.toString(allRewardSum));
	}

	// Thread that updated all information
	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < robots.length; i++) {
				setRobotInfo(i);
			}
			setCompletedJobsInfo();
			updateTotalReward();
			contentPane.repaint();
			contentPane.revalidate();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Button listener that cancels the job
	class Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			for (int indexOfRobot = 0; indexOfRobot < robots.length; indexOfRobot++) {
				try {
					String jobTocancel = Integer.toString(robots[indexOfRobot].getActiveJob().getID());
					if (textField.getText().equals(jobTocancel)) {
						jobList.cancelJob(Integer.parseInt(textField.getText()));
					}
				} catch (Exception ee) {
					// TODO: handle exception
				}

			}
		}
	}

}
