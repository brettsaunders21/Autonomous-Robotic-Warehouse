
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class JobsInterface extends JFrame {

	private JPanel contentPane;
	private JTable t;
	
	
	public JobsInterface() {
		setFrame();		 
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);			
		completedJobs();	
		itemInformation();
		
	}
	
	public void completedJobs(){
		JLabel label = new JLabel("Completed jobs");
		add(label);
		contentPane.setLayout(null);
		contentPane.setName("Completed jobs");
		getContentPane().setLayout(new FlowLayout());
		String[] fields = {"Robot Name", "Item Name", "Weight of Item", "Reward"};
		Object [][] data = {{"Robot1", "aa", "0.321", "13.2"},
							{"Robot3", "ba", "0.221", "3.2"},
							{"Robot2", "ba", "2.221", "3.2"},
							{"Robot1", "ca", "6.221", "3.2"},
							{"Robot2", "ha", "4.221", "3.2"}, 
							{"Robot3", "ba", "0.221", "3.2"}};
		t = new JTable(data, fields);
		t.setPreferredScrollableViewportSize(new Dimension(400, 60));
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);
		
	
	}
	
	public void itemInformation(){
		JLabel label2 = new JLabel("Item information");
		add(label2);
		contentPane.setLayout(null);
		contentPane.setName("Completed jobs");
		getContentPane().setLayout(new FlowLayout());
		String[] fields = {"Item Name", "Weight", "Reward"};
		Object [][] data = {{"aa", "0.321", "13.2"},
							{"ab", "0.221", "3.2"},
							{"bc", "2.221", "3.2"},
							{"ef", "6.221", "3.2"},
							{"dj", "4.221", "3.2"}, 
							{"rc", "0.221", "3.2"}};
		t = new JTable(data, fields);
		t.setPreferredScrollableViewportSize(new Dimension(400, 60));
		t.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(t);
		getContentPane().add(scrollPane);
		contentPane.add(scrollPane);
		
	}
	public void setFrame(){
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1200, 0, 540, 850);
		setVisible(true);
	}
}
