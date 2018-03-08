import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class WarehouseInterface extends JFrame implements Runnable{
	private Image bg = new ImageIcon("src/FullMap.jpg").getImage();
	private Image robot1 = new ImageIcon("src/robotOne.png").getImage();
	private Image robot2 = new ImageIcon("src/robotTwo.png").getImage();
	private Image robot3 = new ImageIcon("src/robotThree.png").getImage();
	private Thread thread;
	private int zeroOnXRobot1 = 60;
	private int zeroOnXRobot2 = 60;
	private int zeroOnXRobot3 = 60;
	private int zeroOnYRobot1 = 730;
	private int zeroOnYRobot2 = 730;
	private int zeroOnYRobot3 = 730;
	private int moveByXAxis = 90;
	private int moveByYAxis = -93;
	
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
		g.drawImage(robot1, zeroOnXRobot1, zeroOnYRobot1, null);
		g.drawImage(robot2, zeroOnXRobot2, zeroOnYRobot2, null);		
		//To change the starting position do zeroOnXRobot + moveByXAxis * number
		g.drawImage(robot3, zeroOnXRobot3, zeroOnYRobot3, null);
		JLabel lblRobot = new JLabel();
			this.add(lblRobot);
		
	}
	 

	@Override
	public void run() {
		
		//Robot r1 = new Robot();
		//Robot r2 = new Robot();
		//Robot r3 = new Robot();
		
		while(true){
			
				//Just to see hot the robot moves
				zeroOnXRobot1 = zeroOnXRobot1 + 5;
				if(zeroOnXRobot1>1000){
					zeroOnXRobot1 = 60;					
				}		
			//Pseudocode
			//r1:       zeroOnXRobot1 += moveByXAxis *r1.getXCoordinate
			//r1:       zeroOnYRobot1 += moveByYAxis *r1.getYCoordinate
			//r2:		zeroOnXRobot2 += moveByXAxis *r2.getXCoordinate
			//r2:		zeroOnYRobot2 += moveByYAxis *r2.getYCoordinate
			//r3:		zeroOnXRobot3 += moveByXAxis *r3.getXCoordinate
			//r3:		zeroOnYRobot3 += moveByYAxis *r3.getYCoordinate
			revalidate();
			repaint();
			
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new WarehouseInterface();
		JobsInterface t = new JobsInterface();
		t.setVisible(true);
	}
}



