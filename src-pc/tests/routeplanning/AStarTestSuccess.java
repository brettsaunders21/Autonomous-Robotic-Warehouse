package tests.routeplanning;

import org.apache.log4j.Logger;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.AStar;
import routeplanning.Map;
import routeplanning.Route;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
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
	
	/**/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {});
	}
	
	/*Initialises each parameter for each test*/
	public AStarTestSuccess() {
		aStar = new AStar(map);
		aStarLogger.setLevel(Level.OFF);
	}

	/*Starts facing left and wants to travel left*/
	@Test
	public void orientationAdjustWait() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {});
		assertEquals(Action.WAIT,r.getOrientationAdjust());
	}
	
	/*Starts facing up and wants to travel left*/
	@Test
	public void orientationAdjustRight() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {});
		assertEquals(Action.TURN_RIGHT,r.getOrientationAdjust());
	}
	
	/*Starts facing down and wants to travel left*/
	@Test
	public void orientationAdjustLeft() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {});
		assertEquals(Action.TURN_LEFT,r.getOrientationAdjust());
	}
	
	/*Starts facing right and wants to travel left*/
	@Test
	public void orientationAdjust180() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(Action.TURN_180,r.getOrientationAdjust());
	}

	/*Starts facing negativeX*/
	@Test
	public void startPoseNegX() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(Pose.NEG_X,r.getStartPose());
	}

	/*Starts facing negativeX*/
	@Test
	public void startPosePosX() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_X, new Route[] {});
		assertEquals(Pose.POS_X,r.getStartPose());
	}
	
	/*Starts facing negativeX*/
	@Test
	public void startPoseNegY() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_Y, new Route[] {});
		assertEquals(Pose.NEG_Y,r.getStartPose());
	}

	/*Starts facing negativeX*/
	@Test
	public void startPosePosY() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.POS_Y, new Route[] {});
		assertEquals(Pose.POS_Y,r.getStartPose());
	}

	/*Starts facing negativeX*/
	@Test
	public void routeLengthStraight() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	/*Starts facing negativeX*/
	@Test
	public void directionsStraight() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	/*Starts facing negativeX*/
	@Test
	public void coordinatesStraight() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(5,0), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0), new Point(5,0)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

	/*Starts facing negativeX*/
	@Test
	public void routeLengthNoObstructionTurn() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	/*Starts facing negativeX*/
	@Test
	public void directionsNoObstructionTurn() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.FORWARD, Action.RIGHT, Action.LEFT, Action.RIGHT};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	/*Starts facing negativeX*/
	@Test
	public void coordinatesNoObstructionTurn() {
		Route r = aStar.generateRoute(new Point(0,0), new Point(3,2), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(1,0), new Point(2,0), new Point(2,1), new Point(3,1), new Point(3,2)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

	/*Starts facing negativeX*/
	@Test
	public void routeLengthNoObstructionTurnReverse() {
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		assertEquals(5,r.getRouteLength());
	}

	/*Starts facing negativeX*/
	@Test
	public void directionsNoObstructionTurnReverse() {
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		Action[] ds = new Action[] {Action.FORWARD, Action.RIGHT, Action.LEFT, Action.FORWARD, Action.RIGHT};
		assertArrayEquals(ds,r.getDirections().toArray());
	}
	
	/*Starts facing negativeX*/
	@Test
	public void coordinatesNoObstructionTurnReverse() {
		Route r = aStar.generateRoute(new Point(3,2), new Point(0,0), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(2,2), new Point(2,1), new Point(1,1), new Point(0,1), new Point(0,0)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
	}

	/*Starts facing negativeX*/
	@Test
	public void obstacleAvoid() {
		aStarLogger.setLevel(Level.DEBUG);
		Route r = aStar.generateRoute(new Point(0,0), new Point(2,6), Pose.NEG_X, new Route[] {});
		Point[] ps = new Point[] {new Point(2,2), new Point(2,1), new Point(1,1), new Point(0,1), new Point(0,0)};
		assertArrayEquals(ps,r.getRouteCoordinates().toArray());
		aStarLogger.setLevel(Level.OFF);
	}

}