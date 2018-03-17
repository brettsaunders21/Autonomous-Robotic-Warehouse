package tests.routeplanning;

import org.junit.Ignore;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.AStar;
import routeplanning.Map;
import routeplanning.Route;
import static org.junit.Assert.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import interfaces.Action;
import interfaces.Pose;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */

public class AStarTest {
	private final static Logger logger = Logger.getLogger(AStarTest.class);
	private final static Logger aStarLogger = Logger.getLogger(AStar.class);
	
	/*private Map map = Map.createTestMap(12,8, new Point[] {new Point(1,2), new Point(1,3), new Point(1,4), new Point(1,5), new Point(1,6),
			new Point(4,2), new Point(4,3), new Point(4,4), new Point(4,5), new Point(4,6),
			new Point(7,2), new Point(4,3), new Point(7,4), new Point(7,5), new Point(7,6),
			new Point(10,2), new Point(10,3), new Point(10,4), new Point(10,5), new Point(10,6)});*/
	
	private Map map = Map.generateRealWarehouseMap();
	
	private AStar aStar;
	
	/*Initialises each parameter for each test*/
	public AStarTest() {
		aStar = new AStar(map);
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
	}

	//Starts facing left and wants to travel left
	@Test(timeout=1000)
	public void orientationAdjustWait() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {}, 0);
		assertEquals(Action.FORWARD,r.getDirections().peek());
	}
	
	//Starts facing up and wants to travel left
	@Test(timeout=1000)
	public void orientationAdjustRight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {}, 0);
		assertEquals(Action.LEFT,r.getDirections().peek());
	}
	
	//Starts facing down and wants to travel left
	@Test(timeout=1000)
	public void orientationAdjustLeft() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {}, 0);
		assertEquals(Action.RIGHT,r.getDirections().peek());
	}
	
	//Starts facing right and wants to travel left
	@Test(timeout=1000)
	public void orientationAdjust180() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(Action.BACKWARD,r.getDirections().peek());
	}

	//Starts facing negativeX
	@Test(timeout=1000)
	public void startPoseNegX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(Pose.NEG_X,r.getStartPose());
	}

	//Starts facing positiveX
	@Test(timeout=1000)
	public void startPosePosX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {}, 0);
		assertEquals(Pose.POS_X,r.getStartPose());
	}
	
	//Starts facing negativeY
	@Test(timeout=1000)
	public void startPoseNegY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {}, 0);
		assertEquals(Pose.NEG_Y,r.getStartPose());
	}

	//Starts facing positiveY
	@Test(timeout=1000)
	public void startPosePosY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {}, 0);
		assertEquals(Pose.POS_Y,r.getStartPose());
	}

	//travels between two points that share a common axis
	@Test(timeout=1000)
	public void routeLengthStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5,r.getLength());
	}

	//travels between two points that share a common axis
	@Test(timeout=1000)
	public void directionsStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] {Action.BACKWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//travels between two points that share a common axis
	@Test(timeout=1000)
	public void coordinatesStraight() {	
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0)};
		assertArrayEquals(ps,r.getCoordinates().toArray());
	}

	//travels between two points that are do not share a common axis
	@Test(timeout=1000)
	public void routeLengthNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5,r.getLength());
	}

	//travels between two points that are do not share a common axis
	@Test(timeout=1000)
	public void directionsNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] {Action.BACKWARD, Action.FORWARD, Action.LEFT, Action.RIGHT, Action.LEFT};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//travels between two points that are do not share a common axis
	@Test(timeout=1000)
	public void coordinatesNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(2,1), new Point(3,1), new Point(3,2)};
		assertArrayEquals(ps,r.getCoordinates().toArray());
	}

	//reverse route of routeLengthNoObstructionTurn
	@Test(timeout=1000)
	public void routeLengthNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5,r.getLength());
	}

	//reverse route of directionsNoObstructionTurn
	@Test(timeout=1000)
	public void directionsNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] {Action.FORWARD, Action.LEFT, Action.FORWARD, Action.RIGHT, Action.FORWARD};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//reverse route of coordinatesNoObstructionTurn
	@Test(timeout=1000)
	public void coordinatesNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] {new Point(2,2), new Point(2,1), new Point(2,0), new Point(1,0), new Point(0,0)};
		assertArrayEquals(ps,r.getCoordinates().toArray());
	}                      

	//Route finding around an obstacle
	@Test(timeout=1000)
	public void coordinatesObstacleAvoid() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(2,6), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] {new Point(0,1), new Point(0,2), new Point(0,3), new Point(0,4), new Point(0,5), new Point(0,6), new Point(1,6), new Point(2,6)};
		assertArrayEquals(ps,r.getCoordinates().toArray());
	}

	//tests that two robots will not occupy the same space
	@Test(timeout=1000)
	public void multiRobotRouteCoords1(){
		Route r1 = aStar.generateRoute(new Point(3, 1), new Point(6,0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(2, 0), new Point(5,0), Pose.POS_X, rs, 0);
		
		
		Point[] ps1 = new Point[] {new Point(3,0), new Point(4,0), new Point(5,0), new Point(6,0)};
		assertArrayEquals(ps1,r1.getCoordinates().toArray());
		
		Point[] ps2 = new Point[] {new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0)};
		assertArrayEquals(ps2,r2.getCoordinates().toArray());
	}
	
	//tests that two robots will not occupy the same space, when three robots are present in the grid
	@Test(timeout=1000)
	public void multiRobotRouteCoords2(){
		Route r1 = aStar.generateRoute(new Point(3, 1), new Point(6,0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(2, 0), new Point(5,0), Pose.POS_X, rs, 0);
		rs = new Route[2];
		rs[0] = r1;
		rs[1] = r2;
		Route r3 = aStar.generateRoute(new Point(1,0), new Point(4,0), Pose.POS_X, rs, 0);
		
		Point[] ps1 = new Point[] {new Point(3,0), new Point(4,0), new Point(5,0), new Point(6,0)};
		assertArrayEquals(ps1,r1.getCoordinates().toArray());
		
		Point[] ps2 = new Point[] {new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0)};
		assertArrayEquals(ps2,r2.getCoordinates().toArray());
		
		Point[] ps3 = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0)};
		assertArrayEquals(ps3,r3.getCoordinates().toArray());
	}
	
	//tests that two robots will not occupy the same space
	@Test(timeout=1000)
	public void multiRobotRouteCoords3() {
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
		Route r1 = aStar.generateRoute(new Point(0, 0), new Point(1,0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(1, 0), new Point(0,1), Pose.POS_X, rs, 0);
		
		
		Point[] ps1 = new Point[] {new Point(1,0)};
		assertArrayEquals(ps1,r1.getCoordinates().toArray());
		
		Point[] ps2 = new Point[] {new Point(0,0), new Point(0,1)};
		
		assertArrayEquals(ps2, r2.getCoordinates().toArray());
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
	}
	
	@Ignore
	@Test
	public void whatAreCoords() {
		boolean[][] b = map.obstructions();
		logger.debug(map.getWidth() + " " + map.getHeight());
		for (int x = 0; x<map.getWidth(); x++) {
			for(int y = 0; y<map.getHeight(); y++) {
				if (!b[x][y]) {
					logger.debug(new Point(x,y));
				}
			}
		}
		assertTrue(map.withinMapBounds(new Point(11,7)));
	}

	@Test
	public void test1(){
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
		Route r1 = aStar.generateRoute(new Point(0, 0), new Point(5,1), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		Route r3 = aStar.generateRoute(new Point(5,1), new Point(5,5) , r1.getFinalPose(), new Route[] {}, r1.getLength()+1);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.PICKUP);		

		r3 = aStar.generateRoute(new Point(5,5), new Point(4,7) , r1.getFinalPose(), new Route[] {}, r1.getLength()+1);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);
		

		Route r2 = aStar.generateRoute(new Point(5, 6), new Point(5,5), Pose.POS_X, new Route[] {}, 0);
		r2 = new Route(r2, Action.PICKUP);

		r3 = aStar.generateRoute(new Point(5,5), new Point(6,5) , r2.getFinalPose(), new Route[] {}, r2.getLength()+1);
		r2 = new Route(r2, r3);
		r2 = new Route(r2, Action.PICKUP);

		r3 = aStar.generateRoute(new Point(6,5), new Point(5,1) , r2.getFinalPose(), new Route[] {}, r2.getLength()+1);
		r2 = new Route(r2, r3);
		r2 = new Route(r2, Action.PICKUP);
		
		r3 = aStar.generateRoute(new Point(5,1), new Point(4,7) , r2.getFinalPose(), new Route[] {}, r2.getLength()+1);
		r2 = new Route(r2, r3);
		r2 = new Route(r2, Action.DROPOFF);
		
	
		Point[] ps1 = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0), new Point(5,1), new Point(5,1), new Point(5,2), new Point(5,3), new Point(5,4), new Point(5,5), new Point(5,5), new Point(5,6), new Point(4,6), new Point(4,7), new Point(4,7)};
		assertArrayEquals(ps1,r1.getCoordinatesArray());
		Point[] ps2 = new Point[] {new Point(5,5), new Point(5,5), new Point(6,5), new Point(6,5), new Point(6,4), new Point(6,3), new Point(6,2), new Point(5,2), new Point(5,1), new Point(5,1), new Point(5,2), new Point(5,3), new Point(5,4), new Point(5,5), new Point(5,6), new Point(4,6), new Point(4,7), new Point(4,7)};
		
		assertArrayEquals(ps2, r2.getCoordinatesArray());
		for (int i = 0; i<r1.getLength(); i++) {
			System.out.println(r1.getPoseAt(i));
		}
		
		
		Action[] as1 = new Action[] {Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.LEFT, Action.PICKUP, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.PICKUP, Action.FORWARD, Action.LEFT, Action.RIGHT, Action.DROPOFF};
		assertArrayEquals(as1,r1.getDirectionArray());
		
		//Point[] as2 = new Action[] {new Point(5,5), new Point(5,5), new Point(6,5), new Point(6,5), new Point(6,4), new Point(6,3), new Point(6,2), new Point(5,2), new Point(5,1), new Point(5,1), new Point(5,2), new Point(5,3), new Point(5,4), new Point(5,5), new Point(5,6), new Point(4,6), new Point(4,7), new Point(4,7)};
		
		//assertArrayEquals(ps2, r2.getCoordinatesArray());
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
	}
}