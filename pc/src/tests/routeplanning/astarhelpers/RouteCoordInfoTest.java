package tests.routeplanning.astarhelpers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lejos.geom.Point;
import routeplanning.astarhelpers.RouteCoordInfo;

public class RouteCoordInfoTest{
	private RouteCoordInfo rInfo;
	
	public RouteCoordInfoTest() {
		rInfo = new RouteCoordInfo(new Point(1,0), new Point(0,0), 2, 1, 0);
	}
	
	/*checks that the input point is the same as that returned by method*/
	@Test
	public void getThisPointTest() {
		assertEquals(new Point(1,0),rInfo.getThisPoint());
	}

	/*checks that the input point is the same as that returned by method*/
	@Test
	public void getOriginPointTest() {
		assertEquals(new Point(0,0), rInfo.getOriginPoint());
	}

	/*checks that the input distance is the same as that returned by method*/
	@Test
	public void getDistFromStartTest() {
		assertEquals(1,rInfo.getDistFromStart());
	}

	/*checks that the input distance is the same as that returned by method*/
	@Test
	public void getDistToGoalTest() {
		assertEquals(2, rInfo.getDistToGoal(), 0.001);
	}
	

	/*checks that the distance returned by method is the sum of the input distances*/
	@Test
	public void getTotalDistTest() {
		assertEquals(3, rInfo.getTotalPointDist(), 0.001);
	}
	
	/*checks that a negative distance cannot be entered*/
	@Test(expected = IllegalArgumentException.class)
	public void negativeDistToGoTest() {
		new RouteCoordInfo(new Point(0,0), new Point(0,0), -1, 1, 0);
	}

	/*checks that a negative distance cannot be entered*/
	@Test(expected = IllegalArgumentException.class)
	public void negativeDistFromStartTest() {
		new RouteCoordInfo(new Point(0,0), new Point(0,0), 1, -1, 0);
	}
}