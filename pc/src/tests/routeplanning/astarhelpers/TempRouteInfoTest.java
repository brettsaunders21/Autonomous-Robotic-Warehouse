package tests.routeplanning.astarhelpers;

import java.util.ArrayList;

import org.junit.Test;

import lejos.geom.Point;
import routeplanning.astarhelpers.TempRouteInfo;

public class TempRouteInfoTest {
	
	@Test
	public void normalConstructorTest() {
		ArrayList<Point> p = new ArrayList<Point>();
		p.add(new Point(0,0));
		ArrayList<Integer> i = new ArrayList<Integer>();
		i.add(1);
		new TempRouteInfo(p, i);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void noElementTest() {
		new TempRouteInfo(new ArrayList<Point>(), new ArrayList<Integer>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void nonEqualQueueTest() {
		ArrayList<Point> p = new ArrayList<Point>();
		p.add(new Point(0,0));
		ArrayList<Integer> i = new ArrayList<Integer>();
		new TempRouteInfo(p, i);
	}
}