package tests.routeplanning;

import static org.junit.Assert.assertArrayEquals;

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
	
	private AStar aStar = new AStar(Map.generateRealWarehouseMap());
	
	public RouteTest() {
		logger.setLevel(Level.OFF);
		aStarLogger.setLevel(Level.OFF);
		routeLogger.setLevel(Level.OFF);
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
	}
}