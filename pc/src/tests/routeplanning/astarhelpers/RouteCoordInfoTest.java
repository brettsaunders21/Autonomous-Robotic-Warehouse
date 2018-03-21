package tests.routeplanning.astarhelpers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.astarhelpers.RouteCoordInfo;

public class RouteCoordInfoTest{
	private RouteCoordInfo rInfo;
	
	//initialises a RouteCoordInfo object
	public RouteCoordInfoTest() {
		rInfo = new RouteCoordInfo(new Point(1,0), new Point(0,0), 2, 1);
	}
	
	//checks that the point given is returned by getter
	@Test
	public void getThisPointTest() {
		assertEquals(new Point(1,0),rInfo.getThisPoint());
	}
	
	//checks that the origin point given is returned by getter
	@Test
	public void getOriginPointTest() {
		assertEquals(new Point(0,0), rInfo.getOriginPoint());
	}
	
	//checks that the distance specified from the start is returned by getter
	@Test
	public void getDistFromStartTest() {
		assertEquals(1,rInfo.getDistFromStart());
	}
	
	
	//checks that the distance to the goal given is returned by getter to within 0.001
	@Test
	public void getDistToGoalTest() {
		assertEquals(2, rInfo.getDistToGoal(), 0.001);
	}
	
	//checks that the total distance returns the sum of distance from start and distance to goal, to within 0.001
	@Test
	public void getTotalDistTest() {
		assertEquals(3, rInfo.getTotalPointDist(), 0.001);
	}
	
	//checks that a negative distance will throw an IllegalArgumentException
	@Test(expected = IllegalArgumentException.class)
	public void negativeDistToGoTest() {
		new RouteCoordInfo(new Point(0,0), new Point(0,0), -1, 1);
	}
	
	//checks that a negative distance will throw an IllegalArgumentException
	@Test(expected = IllegalArgumentException.class)
	public void negativeDistFromStartTest() {
		new RouteCoordInfo(new Point(0,0), new Point(0,0), 1, -1);
	}
}