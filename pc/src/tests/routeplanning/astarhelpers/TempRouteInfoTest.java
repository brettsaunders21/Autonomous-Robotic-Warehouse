package tests.routeplanning.astarhelpers;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.astarhelpers.TempRouteInfo;

public class TempRouteInfoTest {
	private TempRouteInfo rInfo;

	//creates a simple example of a TempRouteInfo object
	public TempRouteInfoTest() {
		ArrayList<Point> coords = new ArrayList<Point>();
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		coords.add(new Point(1,1));
		dirs.add(1);
		rInfo = new TempRouteInfo(coords, dirs);
	}
	
	//checks input coordinates are returned by getter
	@Test
	public void getCoordsTest() {
		assertArrayEquals(new Point[] {new Point(1,1)}, rInfo.getCoords().toArray());
	}
	
	//checks input direction values are returned by getter
	@Test
	public void getDirsTest() {
		assertArrayEquals(new Integer[] {1}, rInfo.getDirs().toArray());
	}
	
	//checks IllegalArgumentException thrown when lists are of different sizes
	@Test(expected = IllegalArgumentException.class)
	public void exceptionTest1() {
		ArrayList<Point> coords = new ArrayList<Point>();
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		coords.add(new Point(0,0));
		new TempRouteInfo(coords, dirs);
	}
	
	//checks IllegalArgumentException thrown when lists are empty
	@Test(expected = IllegalArgumentException.class)
	public void exceptionTest2() {
		ArrayList<Point> coords = new ArrayList<Point>();
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		new TempRouteInfo(coords, dirs);
	}
}