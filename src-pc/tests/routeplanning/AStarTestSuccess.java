package tests.routeplanning;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.AStar;
import routeplanning.Map;
import routeplanning.Route;
import static org.junit.Assert.*;
import interfaces.Action;

import org.apache.log4j.Level;
import interfaces.Pose;

public class AStarTestSuccess {
	final static Logger logger = Logger.getLogger(AStarTestSuccess.class);
	final static Logger aStarLogger = Logger.getLogger(AStar.class);
	
	private Map map = new Map(12,8, new Point[] {new Point(1,2), new Point(1,3), new Point(1,4), new Point(1,5), new Point(1,6),
			new Point(4,2), new Point(4,3), new Point(4,4), new Point(4,5), new Point(4,6),
			new Point(7,2), new Point(4,3), new Point(7,4), new Point(7,5), new Point(7,6),
			new Point(10,2), new Point(10,3), new Point(10,4), new Point(10,5), new Point(10,6)});
	
	private AStar aStar;
	
	/*Initialises each parameter for each test*/
	public AStarTestSuccess() {
		aStar = new AStar(map);
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
	}

	//Starts facing left and wants to travel left
	@Test
	public void orientationAdjustWait() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {});
		assertEquals(Action.WAIT,r.getOrientationAdjust());
	}
	
	//Starts facing up and wants to travel left
	@Test
	public void orientationAdjustRight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {});
		assertEquals(Action.TURN_RIGHT,r.getOrientationAdjust());
	}
	
	//Starts facing down and wants to travel left
	@Test
	public void orientationAdjustLeft() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {});
		assertEquals(Action.TURN_LEFT,r.getOrientationAdjust());
	}
	
	//Starts facing right and wants to travel left
	@Test
	public void orientationAdjust180() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(Action.TURN_180,r.getOrientationAdjust());
	}

	//Starts facing negativeX
	@Test
	public void startPoseNegX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(Pose.NEG_X,r.getStartPose());
	}

	//Starts facing positiveX
	@Test
	public void startPosePosX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {});
		assertEquals(Pose.POS_X,r.getStartPose());
	}
	
	//Starts facing negativeY
	@Test
	public void startPoseNegY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {});
		assertEquals(Pose.NEG_Y,r.getStartPose());
	}

	//Starts facing positiveY
	@Test
	public void startPosePosY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {});
		assertEquals(Pose.POS_Y,r.getStartPose());
	}

	//travels between two points that share a common axis
	@Test
	public void routeLengthStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	//travels between two points that share a common axis
	@Test
	public void directionsStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//travels between two points that share a common axis
	@Test
	public void coordinatesStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

	//travels between two points that are do not share a common axis
	@Test
	public void routeLengthNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	//travels between two points that are do not share a common axis
	@Test
	public void directionsNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.FORWARD, Action.RIGHT, Action.LEFT, Action.RIGHT};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//travels between two points that are do not share a common axis
	@Test
	public void coordinatesNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(2,1), new Point(3,1), new Point(3,2)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

	//reverse route of routeLengthNoObstructionTurn
	@Test
	public void routeLengthNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	//reverse route of directionsNoObstructionTurn
	@Test
	public void directionsNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.RIGHT, Action.LEFT, Action.FORWARD, Action.RIGHT};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	//reverse route of coordinatesNoObstructionTurn
	@Test
	public void coordinatesNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(2,2), new Point(2,1), new Point(1,1), new Point(0,1), new Point(0,0)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}                      

	//Route finding around an obstacle
	@Test
	public void coordinatesObstacleAvoid() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0,0), new Point(2,6), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(0,1), new Point(0,2), new Point(0,3), new Point(0,4), new Point(0,5), new Point(0,6), new Point(0,7), new Point(1,7), new Point(2,7), new Point(2,6)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

}