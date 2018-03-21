package tests.routeplanning;

import static org.junit.Assert.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import interfaces.Action;
import interfaces.Pose;
import lejos.geom.Point;
import routeplanning.*;

public class RouteTest{
	private final static Logger logger = Logger.getLogger(RouteTest.class);
	private final static Logger aStarLogger = Logger.getLogger(AStar.class);
	private final static Logger routeLogger = Logger.getLogger(Route.class);
	
	private final static Level thisLevel = Level.OFF;
	private final static Level aStarLevel = Level.OFF;
	private final static Level routeLevel = Level.OFF;
	
	
	private AStar aStar = new AStar(Map.generateRealWarehouseMap());
	private Route testRoute;
	
	public RouteTest() {
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		coordinates.add(new Point(0,1));
		coordinates.add(new Point(0,2));
		coordinates.add(new Point(0,3));
		directions.add(Action.LEFT);
		directions.add(Action.FORWARD);
		directions.add(Action.FORWARD);
		Pose startPose = Pose.POS_X;
		int startTime = 0;
		Point startPoint = new Point(0,0);
		testRoute = new Route(coordinates, directions, startPose, startTime, startPoint);
		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
	}
	
	//checks the given array of coordinates is returned
	@Test
	public void getCoordsTest() {
		assertArrayEquals(new Point[] {new Point(0,1), new Point(0,2), new Point(0,3)}, testRoute.getCoordinates().toArray());
	}
	
	//checks the given array of coordinates is returned
	@Test
	public void getCoordsArrayTest() {
		assertArrayEquals(new Point[] {new Point(0,1), new Point(0,2), new Point(0,3)}, testRoute.getCoordinatesArray());
	}
	
	//checks the given queue of directions is returned
	@Test
	public void getDirectionTest() {
		assertArrayEquals(new Action[] {Action.LEFT, Action.FORWARD, Action.FORWARD}, testRoute.getDirections().toArray());
	}

	//checks the given array of directions is returned
	@Test
	public void getDirectionArrayTest() {
		assertArrayEquals(new Action[] {Action.LEFT, Action.FORWARD, Action.FORWARD}, testRoute.getDirectionArray());
	}

	//checks the given start pose is returned
	@Test
	public void getStartPoseTest() {
		assertEquals(Pose.POS_X, testRoute.getStartPose());
	}
	
	//checks the correct route length is calculated
	@Test
	public void getLengthTest() {
		assertEquals(3, testRoute.getLength());
	}

	//checks the correct pose at the given time interval is calculated
	@Test
	public void getPoseAtTest() {
		assertEquals(Pose.POS_Y, testRoute.getPoseAt(2));
	}
	
	//checks the correct final pose is calculated
	@Test
	public void getFinalPoseTest() {
		assertEquals(Pose.POS_Y, testRoute.getFinalPose());
	}
	
	//checks the given start time is returned
	@Test
	public void getStartTimeTest() {
		assertEquals(0, testRoute.getStartTime());
	}
	
	//checks the given start point is returned
	@Test
	public void getStartPointTest() {
		assertEquals(new Point(0,0), testRoute.getStartPoint());
	}
	
	/*normal constructor usage*/
	@Test
 	public void standardConstructorTest(){
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		Pose startPose = Pose.POS_X;
		int myStartTime = 0;
		coordinates.add(new Point(0,0));
		directions.add(Action.WAIT);
		new Route(coordinates, directions, startPose, myStartTime, new Point(0,0));

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
		}
	
	/*queues are of different length*/
	@Test(expected = IllegalArgumentException.class)
	public void standardConstructorTestInvalidQueues(){
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		Pose startPose = Pose.POS_X;
		int myStartTime = 0;
		coordinates.add(new Point(0,0));
		new Route(coordinates, directions, startPose, myStartTime, new Point(0,0));

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
	}
	
	/*conjunction constructor combines coordinates correctly*/
	@Test
	public void conjunctionConstructorCoordinatesTest() {
		Point[] tp = new Point[] {new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0)};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(4,0), Pose.POS_X, new Route[] {}, 0);

		Route r3 = new Route(r1, r2);
		Point[] ps = r3.getCoordinatesArray();
		for (int i = 0; i<ps.length; i++) {
			logger.debug("ps " +ps[i]);
		}
		assertArrayEquals(tp, ps);

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
	}
	
	/*conjunction constructor combines coordinates correctly*/
	@Test
	public void conjunctionConstructorDirectionsTest1() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.LEFT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(2,2), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
	}
	
	/*conjunction constructor combines coordinates correctly*/
	@Test
	public void conjunctionConstructorDirectionsTest2() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.FORWARD, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(4,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2);
		Action[] as = r3.getDirectionArray();
		Point[] ps = r3.getCoordinatesArray();
		logger.debug(r1.getFinalPose());
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
			logger.debug(ps[i]);
		}
		assertArrayEquals(tp, as);

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
	}
	
	/*conjunction constructor combines coordinates correctly*/
	@Test
	public void conjunctionConstructorDirectionsTest3() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.RIGHT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(2,2), new Point(2,0), Pose.NEG_Y, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);
	

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
		}
	
	/*conjunction constructor combines coordinates correctly*/
	@Test
	public void conjunctionConstructorDirectionsTest4() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.BACKWARD, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);
	

		logger.setLevel(thisLevel);
		aStarLogger.setLevel(aStarLevel);
		routeLogger.setLevel(routeLevel);
		}
}