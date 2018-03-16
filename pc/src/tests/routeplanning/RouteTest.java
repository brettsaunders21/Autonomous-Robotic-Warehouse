package tests.routeplanning;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
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
		routeLogger.setLevel(Level.ALL);
		BlockingQueue<Point> coordinates = new LinkedBlockingQueue<Point>();
		BlockingQueue<Action> directions = new LinkedBlockingQueue<Action>();
		Pose startPose = Pose.POS_X;
		int myStartTime = 0;
		coordinates.add(new Point(0,0));
		directions.add(Action.WAIT);
		new Route(coordinates, directions, startPose, myStartTime, new Point(0,0));
		routeLogger.setLevel(Level.OFF);
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

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionConstructorCoordinatesTest() {
		Point[] tp = new Point[] {new Point(1,0), new Point(2,0), new Point(2,0), new Point(3,0), new Point(4,0)};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(4,0), Pose.POS_X, new Route[] {}, 0);

		Route r3 = new Route(r1, r2, Action.PICKUP);
		Point[] ps = r3.getCoordinatesArray();
		for (int i = 0; i<ps.length; i++) {
			logger.debug("ps " +ps[i]);
		}
		assertArrayEquals(tp, ps);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionConstructorDirectionsTest1() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.PICKUP, Action.LEFT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(2,2), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2, Action.PICKUP);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionConstructorDirectionsTest2() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.DROPOFF, Action.FORWARD, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(4,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2, Action.DROPOFF);
		Action[] as = r3.getDirectionArray();
		Point[] ps = r3.getCoordinatesArray();
		logger.debug(r1.getFinalPose());
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
			logger.debug(ps[i]);
		}
		assertArrayEquals(tp, as);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionConstructorDirectionsTest3() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.CANCEL, Action.RIGHT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(2,2), new Point(2,0), Pose.NEG_Y, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2, Action.CANCEL);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionConstructorDirectionsTest4() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.SHUTDOWN, Action.BACKWARD, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = new Route(r1, r2, Action.SHUTDOWN);
		Action[] as = r3.getDirectionArray();
		for (int i = 0; i<as.length; i++) {
			logger.debug(as[i]);
		}
		assertArrayEquals(tp, as);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionTestFail1() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		new Route(r1, r2, Action.FORWARD);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionTestFail2() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		new Route(r1, r2, Action.BACKWARD);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionTestFail3() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		new Route(r1, r2, Action.LEFT);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionTestFail4() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		new Route(r1, r2, Action.RIGHT);
	}

	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionArrayList() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.BACKWARD, Action.FORWARD, Action.RIGHT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(0,0), new Point(0,2), Pose.POS_X, new Route[] {}, 0);
		ArrayList<Route> rs = new ArrayList<Route>();
		rs.add(r1);
		rs.add(r2);
		rs.add(r3);
		Route r = new Route(rs);
		Action[] as = r.getDirectionArray();
		assertArrayEquals(tp, as);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionArrayListFail() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(0,1), new Point(0,2), Pose.POS_X, new Route[] {}, 0);
		ArrayList<Route> rs = new ArrayList<Route>();
		rs.add(r1);
		rs.add(r2);
		rs.add(r3);
		new Route(rs);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test
	public void conjunctionActionArrayList() {
		Action[] tp = new Action[] {Action.FORWARD, Action.FORWARD, Action.PICKUP, Action.BACKWARD, Action.FORWARD, Action.DROPOFF, Action.RIGHT, Action.FORWARD};
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(0,0), new Point(0,2), Pose.POS_X, new Route[] {}, 0);
		ArrayList<Action> as = new ArrayList<Action>();
		as.add(Action.PICKUP);
		as.add(Action.DROPOFF);
		ArrayList<Route> rs = new ArrayList<Route>();
		rs.add(r1);
		rs.add(r2);
		rs.add(r3);
		logger.debug(rs.size() + " " + as.size());
		Route r = new Route(rs, as);
		Action[] array = r.getDirectionArray();
		assertArrayEquals(tp, array);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionArrayListFailWrongSizeArray() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		Route r2 = aStar.generateRoute(new Point(2,0), new Point(0,0), Pose.POS_X, new Route[] {}, 0);
		Route r3 = aStar.generateRoute(new Point(0,0), new Point(0,2), Pose.POS_X, new Route[] {}, 0);
		ArrayList<Action> as = new ArrayList<Action>();
		as.add(Action.PICKUP);
		ArrayList<Route> rs = new ArrayList<Route>();
		rs.add(r1);
		rs.add(r2);
		rs.add(r3);
		logger.debug(rs.size() + " " + as.size());
		new Route(rs, as);
	}
	
	/*conjunction constructor combines coordinates and additional instruction correctly*/
	@Test(expected = IllegalArgumentException.class)
	public void conjunctionActionArrayListFailOnlyOneRoute() {
		Route r1 = aStar.generateRoute(new Point(0,0), new Point(2,0), Pose.POS_X, new Route[] {}, 0);
		ArrayList<Action> as = new ArrayList<Action>();
		ArrayList<Route> rs = new ArrayList<Route>();
		rs.add(r1);
		logger.debug(rs.size() + " " + as.size());
		new Route(rs, as);
	}
}