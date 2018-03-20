package tests.routeplanning;

import org.apache.log4j.Logger;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.Map;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */

@RunWith(value = Parameterized.class)
public class MapTestSuccess {
	final static Logger logger = Logger.getLogger(MapTestSuccess.class);
	public int width;
	public int height;
	public int pointx;
	public int pointy;

	
	/*Creates a map and with a single point wall at coordinates*/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {
				{ 2, 1, 1, 0 }, //  width, height, obstructionX, obstructionY
				{ 2, 1, 0, 0 }, //  width, height, obstructionX, obstructionY
				{ 4, 3, 1, 0 }, //  width, height, obstructionX, obstructionY
		});
	}
	
	/*Initialises each parameter for each test*/
	public MapTestSuccess(int width, int height, int pointx, int pointy) {
		this.width = width;
		this.height = height;
		this.pointx = pointx;
		this.pointy = pointy;
	}

	//checks that all the above parameters are valid inputs
	@Test
	public void constructorTest() {
		logger.debug("w: "+width+" h: "+height+" p: ("+pointx+","+pointy+")");
		Map.createTestMap(width, height, new Point[] { new Point(pointx, pointy) });
	}
	
	//checks that the generateRealWarehouseMap() method produces a warehouse of the expected layout
	@Test
	public void warehouseConstructorTest() {
		Map map = Map.generateRealWarehouseMap();
		boolean[][] p = map.getPassable();
		assertEquals(12, p.length);
		assertEquals(8, p[0].length);
		boolean[][] obstr = new boolean[12][8];
		for (int x = 0; x<12; x++) {
			for (int y = 0; y<8; y++) {
				obstr[x][y] = false;
			}
		}
		for (int x = 1; x<11; x=x+3) {
			for (int y = 1; y<6; y++) {
				obstr[x][y] = true;
			}
		}
		assertArrayEquals(obstr, p);
	}
}