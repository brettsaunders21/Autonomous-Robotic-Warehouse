import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class JobsInterface extends JFrame {

	private JPanel contentPane;
	private JTable t;
	GridLayout experimentLayout = new GridLayout(13, 0);
	public JobsInterface() {
		setFrame();
		setResizable(true);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		completedJobs();
		itemInformation();
		currentInfo("Robot 1");
		currentInfo("Robot 2");
		currentInfo("Robot 3");
		cancelJobs();
	}

	public void completedJobs() {
		

		contentPane.setLayout(experimentLayout);
		JLabel label = new JLabel("Completed jobs");
		add(label);

		contentPane.setName("Completed jobs");
		String[] fields = { "Robot Name", "Item Name", "Weight of Item", "Reward" };
		Object[][] data = { { "Robot1", "aa", "0.321", "13.2", "" }, { "Robot3", "ba", "0.221", "3.2" },
				{ "Robot2", "ba", "2.221", "3.2" }, { "Robot1", "ca", "6.221", "3.2" },
				{ "Robot2", "ha", "4.221", "3.2" }, { "Robot3", "ba", "0.221", "3.2" } };
		t = new JTable(data, fields);
		t.setPreferredScrollableViewportSize(new Dimension(10, 70));
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}

	public void itemInformation() {
		JLabel label2 = new JLabel("Item information");
		add(label2);
		contentPane.setName("Completed jobs");;
		String[] fields = { "Item Name", "Weight", "Reward" };
		Object[][] data = { { "aa", "0.321", "13.2" }, { "ab", "0.221", "3.2" }, { "bc", "2.221", "3.2" },
				{ "ef", "6.221", "3.2" }, { "dj", "4.221", "3.2" }, { "rc", "0.221", "3.2" } };
		t = new JTable(data, fields);
		//t.setPreferredScrollableViewportSize(new Dimension(170, 170));
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
		Object[][] data = { { "32", "5.321", "13.2", "ac, ad, fc, th, fd, kj" } };
		t = new JTable(data, fields);
		t.setPreferredScrollableViewportSize(new Dimension(170, 70));
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);

	}
	
	public void cancelJobs(){
		JLabel label2 = new JLabel("Cancel job");
		add(label2);
		JTextField textField = new JTextField();
	//textField.setPreferredSize(new Dimension(20, 20));
		JButton cancelButton = new JButton("Cancel job");
		contentPane.add(textField);
		contentPane.add(cancelButton);
		
	}

	public void setFrame() {
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.revalidate();
		setBounds(1200, 20, 580, 800);
		setVisible(true);
	}
}
