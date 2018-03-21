package tests.routeplanning;

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
 */

public class AStarTest {
	private final static Logger logger = Logger.getLogger(AStarTest.class);
	private final static Logger aStarLogger = Logger.getLogger(AStar.class);
	private final static Level myLevel = Level.OFF;
	private final static Level aStarLevel = Level.OFF;

	private Map map = Map.generateRealWarehouseMap();
	private AStar aStar;

	/* Initialises each parameter for each test */
	public AStarTest() {
		aStar = new AStar(map);
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing left and wants to travel left
	@Test(timeout = 1000)
	public void orientationAdjustWait() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.POS_X, new Route[] {}, 0);
		assertEquals(Action.FORWARD, r.getDirections().peek());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing up and wants to travel left
	@Test(timeout = 1000)
	public void orientationAdjustRight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_Y, new Route[] {}, 0);
		assertEquals(Action.LEFT, r.getDirections().peek());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing down and wants to travel left
	@Test(timeout = 1000)
	public void orientationAdjustLeft() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.POS_Y, new Route[] {}, 0);
		assertEquals(Action.RIGHT, r.getDirections().peek());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing right and wants to travel left
	@Test(timeout = 1000)
	public void orientationAdjust180() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(Action.BACKWARD, r.getDirections().peek());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing negativeX
	@Test(timeout = 1000)
	public void startPoseNegX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(Pose.NEG_X, r.getStartPose());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing positiveX
	@Test(timeout = 1000)
	public void startPosePosX() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.POS_X, new Route[] {}, 0);
		assertEquals(Pose.POS_X, r.getStartPose());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing negativeY
	@Test(timeout = 1000)
	public void startPoseNegY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_Y, new Route[] {}, 0);
		assertEquals(Pose.NEG_Y, r.getStartPose());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Starts facing positiveY
	@Test(timeout = 1000)
	public void startPosePosY() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.POS_Y, new Route[] {}, 0);
		assertEquals(Pose.POS_Y, r.getStartPose());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that share a common axis
	@Test(timeout = 1000)
	public void routeLengthStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5, r.getLength());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that share a common axis
	@Test(timeout = 1000)
	public void directionsStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] { Action.BACKWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD };
		assertArrayEquals(ds, r.getDirections().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that share a common axis
	@Test(timeout = 1000)
	public void coordinatesStraight() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(5, 0), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] { new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0),
				new Point(5, 0) };
		assertArrayEquals(ps, r.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that are do not share a common axis
	@Test(timeout = 1000)
	public void routeLengthNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(3, 2), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5, r.getLength());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that are do not share a common axis
	@Test(timeout = 1000)
	public void directionsNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(3, 2), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] { Action.BACKWARD, Action.FORWARD, Action.LEFT, Action.RIGHT, Action.LEFT };
		assertArrayEquals(ds, r.getDirections().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// travels between two points that are do not share a common axis
	@Test(timeout = 1000)
	public void coordinatesNoObstructionTurn() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(3, 2), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] { new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(3, 1),
				new Point(3, 2) };
		assertArrayEquals(ps, r.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// reverse route of routeLengthNoObstructionTurn
	@Test(timeout = 1000)
	public void routeLengthNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3, 2), new Point(0, 0), Pose.NEG_X, new Route[] {}, 0);
		assertEquals(5, r.getLength());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// reverse route of directionsNoObstructionTurn
	@Test(timeout = 1000)
	public void directionsNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3, 2), new Point(0, 0), Pose.NEG_X, new Route[] {}, 0);
		Action[] ds = new Action[] { Action.FORWARD, Action.LEFT, Action.FORWARD, Action.RIGHT, Action.FORWARD };
		assertArrayEquals(ds, r.getDirections().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// reverse route of coordinatesNoObstructionTurn
	@Test(timeout = 1000)
	public void coordinatesNoObstructionTurnReverse() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(3, 2), new Point(0, 0), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] { new Point(2, 2), new Point(2, 1), new Point(2, 0), new Point(1, 0),
				new Point(0, 0) };
		assertArrayEquals(ps, r.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// Route finding around an obstacle
	@Test(timeout = 1000)
	public void coordinatesObstacleAvoid() {
		logger.trace("");
		Route r = aStar.generateRoute(new Point(0, 0), new Point(2, 6), Pose.NEG_X, new Route[] {}, 0);
		Point[] ps = new Point[] { new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4), new Point(0, 5),
				new Point(0, 6), new Point(1, 6), new Point(2, 6) };
		assertArrayEquals(ps, r.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// tests that two robots will not occupy the same space
	@Test(timeout = 1000)
	public void multiRobotRouteCoords1() {
		logger.setLevel(Level.OFF);
		Route r1 = aStar.generateRoute(new Point(3, 1), new Point(6, 0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(2, 0), new Point(5, 0), Pose.POS_X, rs, 0);

		for (int i = 0; i < r1.getLength(); i++) {
			logger.debug(r1.getCoordinatesArray()[i]);
		}

		for (int i = 0; i < r2.getLength(); i++) {
			logger.debug(r2.getCoordinatesArray()[i]);
		}
		Point[] ps1 = new Point[] { new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0) };
		assertArrayEquals(ps1, r1.getCoordinates().toArray());

		Point[] ps2 = new Point[] { new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0) };
		assertArrayEquals(ps2, r2.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// tests that two robots will not occupy the same space, when three robots are
	// present in the grid
	@Test(timeout = 1000)
	public void multiRobotRouteCoords2() {
		Route r1 = aStar.generateRoute(new Point(3, 1), new Point(6, 0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(2, 0), new Point(5, 0), Pose.POS_X, rs, 0);
		rs = new Route[2];
		rs[0] = r1;
		rs[1] = r2;
		Route r3 = aStar.generateRoute(new Point(1, 0), new Point(4, 0), Pose.POS_X, rs, 0);

		Point[] ps1 = new Point[] { new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0) };
		assertArrayEquals(ps1, r1.getCoordinates().toArray());

		Point[] ps2 = new Point[] { new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0) };
		assertArrayEquals(ps2, r2.getCoordinates().toArray());

		Point[] ps3 = new Point[] { new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0) };
		assertArrayEquals(ps3, r3.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// tests that two robots will not occupy the same space
	@Test(timeout = 1000)
	public void multiRobotRouteCoords3() {
		aStarLogger.setLevel(Level.OFF);
		logger.setLevel(Level.OFF);
		Route r1 = aStar.generateRoute(new Point(0, 0), new Point(1, 0), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(1, 0), new Point(0, 0), Pose.POS_X, rs, 0);

		Point[] ps1 = new Point[] { new Point(1, 0) };
		
		logger.debug(r1.getCoordinates());
		
		assertArrayEquals(ps1, r1.getCoordinates().toArray());

		Point[] ps2 = new Point[] {new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4),
				new Point(2, 5), new Point(2, 6), new Point(1, 6), new Point(0, 6), new Point(0, 5), new Point(0, 4),
				new Point(0, 3), new Point(0, 2), new Point(0, 1), new Point(0, 0) };

		logger.debug(r2.getCoordinates());
		
		assertArrayEquals(ps2, r2.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// tests that two robots will avoid each other
	@Test
	public void multiRobotRouteCoords4() {
		Route r1 = aStar.generateRoute(new Point(2, 5), new Point(2, 2), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(2, 2), new Point(2, 4), Pose.POS_X, rs, 0);

		Point[] ps1 = new Point[] { new Point(2, 4), new Point(2, 3), new Point(2, 2) };

		logger.debug(r1.getCoordinates());

		assertArrayEquals(ps1, r1.getCoordinates().toArray());

		Point[] ps2 = new Point[] { new Point(2, 3), new Point(3, 3), new Point(3, 4), new Point(2, 4) };

		logger.debug(r2.getCoordinates());

		assertArrayEquals(ps2, r2.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// tests that two robots will avoid each other in a more complicated encounter
	@Test(timeout = 1000)
	public void multiRobotRouteCoords5() {
		Route r1 = aStar.generateRoute(new Point(0, 0), new Point(0, 5), Pose.POS_X, new Route[] {}, 0);
		Route[] rs = new Route[1];
		rs[0] = r1;
		Route r2 = aStar.generateRoute(new Point(0, 5), new Point(0, 0), Pose.POS_X, rs, 0);

		Point[] ps1 = new Point[] { new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4),
				new Point(0, 5) };

		logger.debug(r1.getCoordinates());

		assertArrayEquals(ps1, r1.getCoordinates().toArray());

		Point[] ps2 = new Point[] { new Point(0, 6), new Point(1, 6), new Point(2, 6), new Point(2, 5), new Point(2, 4),
				new Point(2, 3), new Point(2, 2), new Point(2, 1), new Point(2, 0), new Point(1, 0), new Point(0, 0) };

		logger.debug(r2.getCoordinates());

		assertArrayEquals(ps2, r2.getCoordinates().toArray());
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// checks that a robot will correctly avoid other robots when re-calculating route
	@Test
	public void adjustTest1() {
		Route r1 = aStar.generateRoute(new Point(3, 3), new Point(5, 6), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(5, 6), new Point(8, 3), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r3 = aStar.generateRoute(new Point(8, 3), new Point(5, 0), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r3 = aStar.generateRoute(new Point(5, 0), new Point(2, 2), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r3 = aStar.generateRoute(new Point(2, 2), new Point(2, 4), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps1 = new Point[] { new Point(3, 4), new Point(3, 5), new Point(3, 6), new Point(4, 6), new Point(5, 6),
				new Point(5, 6), new Point(6, 6), new Point(7, 6), new Point(8, 6), new Point(8, 5), new Point(8, 4),
				new Point(8, 3), new Point(8, 3), new Point(8, 2), new Point(8, 1), new Point(8, 0), new Point(7, 0),
				new Point(6, 0), new Point(5, 0), new Point(5, 0), new Point(4, 0), new Point(3, 0), new Point(3, 1),
				new Point(2, 1), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 4) };
		assertArrayEquals(ps1, r1.getCoordinatesArray());

		for (int i = 0; i < 6; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps2 = new Point[] { new Point(6, 6), new Point(7, 6), new Point(8, 6), new Point(8, 5), new Point(8, 4),
				new Point(8, 3), new Point(8, 3), new Point(8, 2), new Point(8, 1), new Point(8, 0), new Point(7, 0),
				new Point(6, 0), new Point(5, 0), new Point(5, 0), new Point(4, 0), new Point(3, 0), new Point(3, 1),
				new Point(2, 1), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 4) };
		assertArrayEquals(ps2, r1.getCoordinatesArray());

		for (int i = 0; i < 7; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps3 = new Point[] { new Point(8, 2), new Point(8, 1), new Point(8, 0), new Point(7, 0), new Point(6, 0),
				new Point(5, 0), new Point(5, 0), new Point(4, 0), new Point(3, 0), new Point(3, 1), new Point(2, 1),
				new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 4) };
		assertArrayEquals(ps3, r1.getCoordinatesArray());

		for (int i = 0; i < 7; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps4 = new Point[] { new Point(4, 0), new Point(3, 0), new Point(3, 1), new Point(2, 1), new Point(2, 2),
				new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 4) };
		assertArrayEquals(ps4, r1.getCoordinatesArray());

		for (int i = 0; i < 6; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps5 = new Point[] { new Point(2, 3), new Point(2, 4), new Point(2, 4) };
		assertArrayEquals(ps5, r1.getCoordinatesArray());

		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// checks that a robot will correctly generate a route when facing the same direction as its next travel direction
	@Test
	public void adjustTest2() {
		Route r1 = aStar.generateRoute(new Point(3, 3), new Point(3, 6), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(3, 6), new Point(5, 6), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps1 = new Point[] { new Point(3, 4), new Point(3, 5), new Point(3, 6), new Point(3, 6), new Point(4, 6),
				new Point(5, 6), new Point(5, 6) };
		assertArrayEquals(ps1, r1.getCoordinatesArray());

		for (int i = 0; i < 4; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps2 = new Point[] { new Point(4, 6), new Point(5, 6), new Point(5, 6) };
		assertArrayEquals(ps2, r1.getCoordinatesArray());
		
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	// checks that a robot will correctly generate a route when at an angle relative to its next travel direction
	@Test
	public void adjustTest3() {
		Route r1 = aStar.generateRoute(new Point(3, 3), new Point(3, 6), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(3, 6), new Point(1, 6), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps1 = new Point[] { new Point(3, 4), new Point(3, 5), new Point(3, 6), new Point(3, 6), new Point(2, 6),
				new Point(1, 6), new Point(1, 6) };
		assertArrayEquals(ps1, r1.getCoordinatesArray());

		for (int i = 0; i < 4; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps2 = new Point[] { new Point(2, 6), new Point(1, 6), new Point(1, 6) };
		assertArrayEquals(ps2, r1.getCoordinatesArray());
		
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	//checks that a robot will correctly generate a route when moving from the same point in any direction relative to its current orientation
	@Test
	public void adjustTest4() {
		Route r1 = aStar.generateRoute(new Point(3, 3), new Point(3, 6), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(3, 6), new Point(3, 4), Pose.POS_X, new Route[] {}, 0);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps1 = new Point[] { new Point(3, 4), new Point(3, 5), new Point(3, 6), new Point(3, 6), new Point(3, 5),
				new Point(3, 4), new Point(3, 4) };
		assertArrayEquals(ps1, r1.getCoordinatesArray());

		for (int i = 0; i < 4; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps2 = new Point[] { new Point(3, 5), new Point(3, 4), new Point(3, 4) };
		assertArrayEquals(ps2, r1.getCoordinatesArray());
		
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}

	@Test
	public void adjustTest5() {
		logger.setLevel(Level.DEBUG);
		Route r1 = aStar.generateRoute(new Point(4, 7), new Point(5, 5), Pose.POS_Y, new Route[] {}, 15);
		Route r3 = aStar.generateRoute(new Point(5, 5), new Point(6, 5), Pose.NEG_Y, new Route[] {}, 15);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r3 = aStar.generateRoute(new Point(6, 5), new Point(5, 1), Pose.POS_X, new Route[] {}, 16);
		r1 = new Route(r1, Action.PICKUP);
		r1 = new Route(r1, r3);
		r1 = new Route(r1, Action.DROPOFF);

		logger.debug(r1.getStartPose());
		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps1 = new Point[] {new Point(4,6), new Point(5,6), new Point(5,5), new Point(5,5), new Point(6, 5), new Point(6, 5), new Point(6, 4), new Point(6, 3), new Point(6, 2),
				new Point(5, 2), new Point(5, 1), new Point(5,1) };
		assertArrayEquals(ps1, r1.getCoordinatesArray());

		for (int i = 0; i <4 ; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}
		logger.debug(r1.getDirectionArray()[3]);
		logger.debug(r1.getPoseAt(3));

		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getStartPose());
		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps2 = new Point[] {  new Point(6, 5), new Point(6, 5), new Point(6, 4), new Point(6, 3), new Point(6, 2),
				new Point(5, 2), new Point(5, 1), new Point(5,1) };
		assertArrayEquals(ps2, r1.getCoordinatesArray());
		
		for (int i = 0; i <2 ; i++) {
			r1.getDirections().poll();
			r1.getCoordinates().poll();
		}

		logger.debug(r1.getDirectionArray()[1]);
		logger.debug(r1.getPoseAt(1));
		r1 = aStar.adjustForCollisions(r1, new Route[0], 0);

		logger.debug(r1.getStartPose());
		logger.debug(r1.getDirections());
		logger.debug(r1.getCoordinates());

		Point[] ps3 = new Point[] {  new Point(6, 4), new Point(6, 3), new Point(6, 2),
				new Point(5, 2), new Point(5, 1), new Point(5,1) };
		assertArrayEquals(ps3, r1.getCoordinatesArray());

		
		logger.setLevel(myLevel);
		aStarLogger.setLevel(aStarLevel);
	}
}