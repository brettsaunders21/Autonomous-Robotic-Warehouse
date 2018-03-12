package warehouse_interface;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WarehouseInterface extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2909589395503239057L;
	private Image bg = new ImageIcon("src/FullMap.jpg").getImage();
	private Image robot1 = new ImageIcon("src/robotOne.png").getImage();
	private Image robot2 = new ImageIcon("src/robotTwo.png").getImage();
	private Image robot3 = new ImageIcon("src/robotThree.png").getImage();
	private Thread thread;
	private int zeroOnRobotXAxis = 60;
	private int zeroOnRobotYAxis = 730;
	private int moveByXAxis = 90;
	private int moveByYAxis = -93;
	private int r1LocationOnXAxis;
	private int r2LocationOnXAxis;
	private int r3LocationOnXAxis;
	private int r1LocationOnYAxis;
	private int r2LocationOnYAxis;
	private int r3LocationOnYAxis;
	
	public WarehouseInterface(){		
		this.setName("WarehouseInterface");
		this.setSize(1200, 850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);					
		thread = new Thread(this);
		thread.start();				
	}
		
	public void paint(Graphics g){
		g.drawImage(bg, 0, 0, null);
		g.drawImage(robot1, zeroOnRobotXAxis, zeroOnRobotYAxis, null);
		//Two more robots 
		//g.drawImage(robot2, zeroOnXRobot2, zeroOnYRobot2, null);		
		//g.drawImage(robot3, zeroOnXRobot3, zeroOnYRobot3, null);
		JLabel lblRobot = new JLabel();
			this.add(lblRobot);		
	}
	 

	@Override
	public void run() {
		
		//For future integration: This class receives an array of all of the robots. Imagine we have 3 robots there (r1, r2, r3)
		
		while(true){
					
			//Pseudocode for future integration
			//r1:       r1LocationOnXAxis = zeroOnRobotXAxis + moveByXAxis *r1.getXCoordinate
			//r1:       r1LocationOnYAxis = zeroOnRobotYAxis + moveByYAxis *r1.getYCoordinate
			//r2:		r2LocationOnXAxis = zeroOnRobotXAxis + moveByXAxis *r2.getXCoordinate
			//r2:		r2LocationOnYAxis = zeroOnRobotYAxis + moveByYAxis *r2.getYCoordinate
			//r3:		r3LocationOnXAxis = zeroOnRobotXAxis + moveByXAxis *r3.getXCoordinate
			//r3:		r3LocationOnYAxis = zeroOnRobotYAxis + moveByYAxis *r3.getYCoordinate
			revalidate();
			repaint();
			
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}



