package warehouse_interface;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import interfaces.Robot;
import lejos.geom.Point;

public class WarehouseInterface extends JFrame implements Runnable{

	private Image bg = new ImageIcon("src/warehouse_interface/Map.jpg").getImage();	
	private Image robotMarco = new ImageIcon("src/warehouse_interface/nxtMarco.png").getImage();
	private Image robotSpike = new ImageIcon("src/warehouse_interface/nxtSPIKE.png").getImage();
	private Image robotJeremy = new ImageIcon("src/warehouse_interface/nxtJEREMY.png").getImage();
	private Image extraRobot = new ImageIcon("src/warehouse_interface/nxtExtra.png").getImage();
	private Image[] arrayOfImages = {robotMarco, robotSpike, robotJeremy, extraRobot};
	private Thread thread;
	private int zeroOnRobotXAxis = 60;
	private int zeroOnRobotYAxis = 700;
	private int moveByXAxis = 90;
	private int moveByYAxis = -93;
	private Robot[] robots;
		
	//Constructor that takes a list of robots as a parameter
	public WarehouseInterface(Robot[] robots){
		setFrame();		
		this.robots = robots;	
		thread = new Thread(this);
		thread.start();	
	}
	
	//To set a frame	
	public void setFrame(){
		this.setName("WarehouseInterface");
		this.setSize(1200, 850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//To draw all robots on a map
	public void paint(Graphics g){
		g.drawImage(bg, 0, 0, null);		
		for(int i = 0; i < robots.length; i++){
			//If robot is called "Marco", give him a special image for Marco
			if(robots[i].getRobotName().toLowerCase().equals("marco"))
				g.drawImage(arrayOfImages[0], zeroOnRobotXAxis+Math.round(robots[i].getCurrentPosition().x)*moveByXAxis, zeroOnRobotYAxis+Math.round(robots[i].getCurrentPosition().y)*moveByYAxis, null);
			//If robot is called "Spike", give him a special image for Spike
			else if(robots[i].getRobotName().toLowerCase().equals("spike"))
				g.drawImage(arrayOfImages[1], zeroOnRobotXAxis+Math.round(robots[i].getCurrentPosition().x)*moveByXAxis, zeroOnRobotYAxis+Math.round(robots[i].getCurrentPosition().y)*moveByYAxis, null);
			//If robot is called "Jeremy", give him a special image for Jeremy
			else if(robots[i].getRobotName().toLowerCase().equals("jeremy"))
				g.drawImage(arrayOfImages[2], zeroOnRobotXAxis+Math.round(robots[i].getCurrentPosition().x)*moveByXAxis, zeroOnRobotYAxis+Math.round(robots[i].getCurrentPosition().y)*moveByYAxis, null);
			//If else, give a default picture for robot without a name below picture
			else
				g.drawImage(arrayOfImages[3], zeroOnRobotXAxis+Math.round(robots[i].getCurrentPosition().x)*moveByXAxis, zeroOnRobotYAxis+Math.round(robots[i].getCurrentPosition().y)*moveByYAxis, null);			
		}
		JLabel lblRobot = new JLabel();
			this.add(lblRobot);		
	}
	 
	//A thread to redraw map with updated coordinates
	@Override
	public void run() {	
		while(true){
			//check and update the current position of each robot on a map
			for(int i = 0; i < robots.length; i++){
				robots[i].setCurrentPosition(new Point(robots[i].getCurrentPosition().x, robots[i].getCurrentPosition().y));
			}
			revalidate();
			repaint();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	
}



