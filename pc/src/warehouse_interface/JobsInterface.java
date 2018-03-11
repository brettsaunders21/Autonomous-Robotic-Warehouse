import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class JobsInterface extends JFrame implements Runnable {

	private JPanel contentPane;
	private JPanel cancelJobPane;
	private Thread thread;
	GridLayout mainLayout = new GridLayout(12, 0);
	GridLayout cancelJobLayout = new GridLayout(2, 0);
	int weight = 2;
	Object[][] completedJobsData = { { "Robot1", "aa", weight, "13.2", "" }, { "Robot3", "ba", "0.221", "3.2" },
			{ "Robot2", "ba", "2.221", "3.2" }, { "Robot1", "ca", "6.221", "3.2" }, { "Robot2", "ha", "4.221", "3.2" },
			{ "Robot3", "ba", "0.221", "3.2" } };
	Object[][] robotInfoData = { { "32", "5.321", "13.2", "ac, ad, fc, th, fd, kj" } };
	private JTable t;

	public JobsInterface() {
		setFrame();
		setPanes();
		currentInfo("Robot 1");
	//	currentInfo("Robot 2");
	//	currentInfo("Robot 3");
		itemsInformation();
		completedJobs();
		cancelJobs();
		thread = new Thread(this);
		thread.start();
	}

	public void completedJobs() {
		contentPane.setLayout(mainLayout);
		JLabel label = new JLabel("Completed jobs");
		add(label);
		contentPane.setName("Completed jobs");
		String[] fields = { "Robot Name", "Item Name", "Weight of Item", "Reward" };
		t = new JTable(completedJobsData, fields);
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}

	public void itemsInformation() {
		JLabel label2 = new JLabel("Items information");
		add(label2);
		contentPane.setName("Completed jobs");
		String[] fields = { "Item Name", "Weight", "Reward" };
		Object[][] data = { { "aa", "0.321", "13.2" }, { "ab", "0.221", "3.2" }, { "bc", "2.221", "3.2" },
				{ "ef", "6.221", "3.2" }, { "dj", "4.221", "3.2" }, { "rc", "0.221", "3.2" } };
		t = new JTable(data, fields);
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}

	public void currentInfo(String robotName) {
		JLabel label2 = new JLabel(robotName);
		add(label2);
		contentPane.setName("Completed jobs");
		String[] fields = { "CurrentJobs", "Total Weight", "Total Reward", "All items in that job" };		
		contentPane.setLayout(mainLayout);
		t = new JTable(robotInfoData, fields);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);
	}

	public void cancelJobs() {
		cancelJobPane.setLayout(cancelJobLayout);
		JLabel label2 = new JLabel("Cancel job");
		add(label2);
		JTextField textField = new JTextField();
		textField.setText("Type your job ID here");
		JButton cancelButton = new JButton("Cancel job");
		cancelJobPane.add(textField);
		cancelJobPane.add(cancelButton);
		contentPane.add(cancelJobPane);
		textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				textField.setText("");
			}
			public void focusLost(FocusEvent e) {
				textField.setText("Type your job ID here");
			}
		});

	}

	public void setFrame() {
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.revalidate();
		setBounds(1200, 20, 700, 850);
		setResizable(true);
		setVisible(true);
	}

	
	public void setPanes() {
		mainLayout.setVgap(-20);
		contentPane = new JPanel();
		cancelJobPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(-10, 5, 0, 5));
		setContentPane(contentPane);
	}

	@Override
	public void run() {
		while (true) {
			setRobotInfo();
			setAllItemsInfo();
			setCompletedJobsInfo();
			contentPane.repaint();
			contentPane.revalidate();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setRobotInfo(){
		//Pseudocode for future integration
		//robotInfoData[0][0] = Job.getID()
		//robotInfoData[0][1] = Job.getTOTAL_WEIGHT()
		//robotInfoData[0][2] = Job.getTOTAL_REWARD()
		//String allItems = "";
		//for(String s: Job.getITEMS(){
		//	allItems += s+",";
		//}
		//robotInfoData[0][3] = allItems;
	}
	
	public void setAllItemsInfo(){
		
	}
	
	public void setCompletedJobsInfo(){
		
	}

}
